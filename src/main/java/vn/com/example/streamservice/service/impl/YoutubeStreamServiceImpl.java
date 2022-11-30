package vn.com.example.streamservice.service.impl;

import static vn.com.example.streamservice.constant.Constants.AUTHORIZATION;
import static vn.com.example.streamservice.constant.Constants.CONNECT_SUCCESS;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStreamSnippet;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.config.StreamProperties;
import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.entity.UserAuthYoutube;
import vn.com.example.streamservice.entity.UserChannelYoutube;
import vn.com.example.streamservice.entity.UserPlatform;
import vn.com.example.streamservice.enums.PlatformType;
import vn.com.example.streamservice.exception.BadRequestException;
import vn.com.example.streamservice.exception.UnauthorizedException;
import vn.com.example.streamservice.repository.UserAuthYoutubeRepository;
import vn.com.example.streamservice.repository.UserChannelYoutubeRepository;
import vn.com.example.streamservice.repository.UserPlatformRepository;
import vn.com.example.streamservice.repository.UserRepository;
import vn.com.example.streamservice.service.YoutubeStreamService;
import vn.com.example.streamservice.service.dto.RestTemplateBoardcastYoutubeDto;
import vn.com.example.streamservice.service.dto.RestTemplateLiveStreamYoutubeDto;
import vn.com.example.streamservice.service.dto.SnippetBoardcastDto;
import vn.com.example.streamservice.service.dto.YoutubeListItemsRestemplate;
import vn.com.example.streamservice.service.dto.request.BoardcastYoutubeRequestDto;
import vn.com.example.streamservice.service.dto.request.LiveStreamYoutubeRequestDto;
import vn.com.example.streamservice.service.dto.response.YoutubeChannelInfoResponseDto;
import vn.com.example.streamservice.service.dto.response.YoutubeCreateStreamResponseDto;

@Service
@RequiredArgsConstructor
public class YoutubeStreamServiceImpl implements YoutubeStreamService {

    private final StreamProperties streamProperties;

    private final RestTemplate restTemplate;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final UserRepository userRepository;
    private final UserAuthYoutubeRepository userAuthYoutubeRepository;
    private final UserChannelYoutubeRepository userChannelYoutubeRepository;
    private final UserPlatformRepository userPlatformRepository;

    private static final String CLIENT_SECRETS = "/json/client_secrets.json";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    @Override
    @Transactional
    public YoutubeChannelInfoResponseDto getChannelInfo(Long userId) throws GeneralSecurityException, IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        user.getUserAuthYoutubes().stream().forEach((data -> {
            if (Instant.now().isBefore(data.getExpiresSession())) {
                headers.set(AUTHORIZATION, "Bearer" + " " + data.getAccessToken());
            } else {
                data.setDeleted(true);
                userAuthYoutubeRepository.save(data);
                throw new BadRequestException("Session is expired");
            }
        }));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<YoutubeListItemsRestemplate> dataInfo = restTemplate.exchange(
                streamProperties.getYoutube().getUrl().getChannelInfo(), HttpMethod.GET, entity,
                YoutubeListItemsRestemplate.class);

        YoutubeChannelInfoResponseDto youtubeChannelInfoDto = new YoutubeChannelInfoResponseDto();

        dataInfo.getBody().getListYoutubeItemsDto().stream().forEach(data -> {
            youtubeChannelInfoDto.setChannelId(data.getChannelId());
            youtubeChannelInfoDto.setImage(data.getSnippet().getThumbnails().getYoutubeMediumDto().getImage());
            youtubeChannelInfoDto.setTitle(data.getSnippet().getTitle());
        });

        // Save UserChannelYoutube
        UserChannelYoutube userChannelYoutube = new UserChannelYoutube();
        userChannelYoutube.setChannelId(youtubeChannelInfoDto.getChannelId());
        userChannelYoutube.setDisplayName(youtubeChannelInfoDto.getTitle());
        userChannelYoutube.setImageUrl(youtubeChannelInfoDto.getImage());
        userChannelYoutube.setUser(user);
        userChannelYoutubeRepository.save(userChannelYoutube);

        // Save UserPlatfom
        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setPlatformType(PlatformType.YOUTUBE);
        userPlatform.setUser(user);
        userPlatformRepository.save(userPlatform);

        return youtubeChannelInfoDto;
    }

    @Override
    @Transactional
    public YoutubeCreateStreamResponseDto createLiveStream(Long userId, String channelId)
            throws GeneralSecurityException, IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));

        // Call api create stream key
        RestTemplateLiveStreamYoutubeDto restTemplateLiveStreamYoutubeDto = callApiCreateStreamKey(userId, channelId,
                user);

        // Call api create Boadcast
        RestTemplateBoardcastYoutubeDto restTemplateBoardcastYoutubeDto = callApiCreateBoardcast(user);

        // Call api to bind

        createBind(restTemplateBoardcastYoutubeDto.getBoardcastId(), restTemplateLiveStreamYoutubeDto.getStreamId(),
                user);

        return YoutubeCreateStreamResponseDto.builder()
                .backupIngestionAddress(restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo()
                        .getIngestionInfo().getBackupIngestionAddress())
                .streamId(restTemplateLiveStreamYoutubeDto.getStreamId())
                .ingestionAddress(restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo().getIngestionInfo()
                        .getIngestionAddress())
                .streamName(restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo().getIngestionInfo()
                        .getStreamName())
                .build();

    }

    @Override
    @Transactional
    public String youtubeCallBackGetToken(String code, String state) throws IOException {
        User user = userRepository.findById(Long.parseLong(state))
                .orElseThrow(() -> new UnauthorizedException("user not found"));
        // Load client secrets.
        InputStream in = YoutubeStreamServiceImpl.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        HttpTransport httpTransport = new NetHttpTransport();
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
                clientSecrets, SCOPES).build();
        TokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(streamProperties.getYoutube().getUrl().getCallBack()).execute();
        UserAuthYoutube userAuthYoutube = new UserAuthYoutube();
        userAuthYoutube.setAccessToken(tokenResponse.getAccessToken());
        userAuthYoutube
                .setExpiresSession((Instant.now().plus(tokenResponse.getExpiresInSeconds(), ChronoUnit.SECONDS)));
        userAuthYoutube.setUser(user);
        userAuthYoutubeRepository.save(userAuthYoutube);
        return CONNECT_SUCCESS;
    }

    @Override
    public String getLinkOAuth(Long userId) throws IOException {

        // Load client secrets.
        InputStream in = YoutubeStreamServiceImpl.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        HttpTransport httpTransport = new NetHttpTransport();
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
                clientSecrets, SCOPES).build();
        // open in browser

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8000/Callback").setState(userId.toString());

        return authorizationUrl.toString();
    }

    @Transactional
    private RestTemplateLiveStreamYoutubeDto callApiCreateStreamKey(Long userId, String channelId, User user) {
        // create request body
        LiveStreamYoutubeRequestDto request = new LiveStreamYoutubeRequestDto();

        CdnSettings cdn = new CdnSettings();
        cdn.setFrameRate("variable");
        cdn.setIngestionType("rtmp");
        cdn.setResolution("variable");
        request.setCdnSettings(cdn);

        LiveStreamSnippet snippet = new LiveStreamSnippet();
        snippet.setTitle("Streaming with Sparkminds");
        snippet.setDescription("Streaming with Sparkminds");

        request.setKind("youtube#liveStream");
        request.setSnippet(snippet);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        user.getUserAuthYoutubes().stream().forEach((data -> {
            if (Instant.now().isBefore(data.getExpiresSession()) && !data.isDeleted()) {
                headers.set(AUTHORIZATION, "Bearer" + " " + data.getAccessToken());
            } else {
                data.setDeleted(true);
                userAuthYoutubeRepository.save(data);
                throw new BadRequestException("Session is expired");
            }
        }));

        HttpEntity<LiveStreamYoutubeRequestDto> entity = new HttpEntity<>(request, headers);

        RestTemplateLiveStreamYoutubeDto restTemplateLiveStreamYoutubeDto = restTemplate.postForObject(
                streamProperties.getYoutube().getUrl().getCreateStream(), entity,
                RestTemplateLiveStreamYoutubeDto.class);
        if (restTemplateLiveStreamYoutubeDto == null) {
            throw new BadRequestException("Create fail");
        }

        UserChannelYoutube userChannelYoutube = userChannelYoutubeRepository.findByChannelIdAndUserId(channelId, userId)
                .orElseThrow(() -> new BadRequestException("User Channel Youtube not found"));

        userChannelYoutube.setBackupIngestionAddress(restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo()
                .getIngestionInfo().getBackupIngestionAddress());
        userChannelYoutube.setStreamId(restTemplateLiveStreamYoutubeDto.getStreamId());
        userChannelYoutube.setIngestionAddress(restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo()
                .getIngestionInfo().getIngestionAddress());
        userChannelYoutube.setStreamKey(
                restTemplateLiveStreamYoutubeDto.getRestemplateLiveStreamCdnInfo().getIngestionInfo().getStreamName());
        userChannelYoutubeRepository.save(userChannelYoutube);
        return restTemplateLiveStreamYoutubeDto;
    }

    private RestTemplateBoardcastYoutubeDto callApiCreateBoardcast(User user) {
        BoardcastYoutubeRequestDto request = new BoardcastYoutubeRequestDto();

        // Add the contentDetails object property to the LiveBroadcast object.
        LiveBroadcastContentDetails contentDetails = new LiveBroadcastContentDetails();
        contentDetails.setEnableAutoStart(true);
        request.setContentDetails(contentDetails);

        // Add the snippet object property to the LiveBroadcast object.
//        LiveBroadcastSnippet snippet = new LiveBroadcastSnippet();
        SnippetBoardcastDto snippet = new SnippetBoardcastDto();
        snippet.setScheduledStartTime("2024-10-10");
        snippet.setTitle("test");
        request.setSnippet(snippet);

        // Add the status object property to the LiveBroadcast object.
        LiveBroadcastStatus status = new LiveBroadcastStatus();
        status.setPrivacyStatus("private");
        request.setStatus(status);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        user.getUserAuthYoutubes().stream().forEach((data -> {
            if (Instant.now().isBefore(data.getExpiresSession()) && !data.isDeleted()) {
                headers.set(AUTHORIZATION, "Bearer" + " " + data.getAccessToken());
            } else {
                data.setDeleted(true);
                userAuthYoutubeRepository.save(data);
                throw new BadRequestException("Session is expired");
            }
        }));

        HttpEntity<BoardcastYoutubeRequestDto> entity = new HttpEntity<>(request, headers);

        RestTemplateBoardcastYoutubeDto restTemplateLiveStreamYoutubeDto = restTemplate.postForObject(
                streamProperties.getYoutube().getUrl().getCreateBoardcast(), entity,
                RestTemplateBoardcastYoutubeDto.class);

        return restTemplateLiveStreamYoutubeDto;
    }

    private void createBind(String boardcastId, String livestreamId, User user) {
        if (boardcastId == null && livestreamId == null) {
            throw new BadRequestException("boardcastId or livestreamId null");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        user.getUserAuthYoutubes().stream().forEach((data -> {
            if (Instant.now().isBefore(data.getExpiresSession()) && !data.isDeleted()) {
                headers.set(AUTHORIZATION, "Bearer" + " " + data.getAccessToken());
            } else {
                data.setDeleted(true);
                userAuthYoutubeRepository.save(data);
                throw new BadRequestException("Session is expired");
            }
        }));

        Map<String, String> params = new HashMap<>();
        params.put("boadcastId", boardcastId);
        params.put("streamId", livestreamId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.postForObject(streamProperties.getYoutube().getUrl().getBind(), entity,
                RestTemplateBoardcastYoutubeDto.class, params);
    }
}
