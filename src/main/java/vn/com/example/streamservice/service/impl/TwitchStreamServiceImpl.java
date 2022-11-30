package vn.com.example.streamservice.service.impl;

import static vn.com.example.streamservice.constant.Constants.AUTHORIZATION;
import static vn.com.example.streamservice.constant.Constants.AUTHORIZATION_CODE;
import static vn.com.example.streamservice.constant.Constants.BOARDCASTER_ID;
import static vn.com.example.streamservice.constant.Constants.CLIENT_ID;
import static vn.com.example.streamservice.constant.Constants.CLIENT_ID_USER;
import static vn.com.example.streamservice.constant.Constants.CLIENT_SECRET;
import static vn.com.example.streamservice.constant.Constants.CODE;
import static vn.com.example.streamservice.constant.Constants.CONNECT_SUCCESS;
import static vn.com.example.streamservice.constant.Constants.GRANT_TYPE;
import static vn.com.example.streamservice.constant.Constants.REDIRECT_URI;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.config.StreamProperties;
import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.entity.UserAuthTwitch;
import vn.com.example.streamservice.entity.UserChannelTwitch;
import vn.com.example.streamservice.entity.UserPlatform;
import vn.com.example.streamservice.enums.PlatformType;
import vn.com.example.streamservice.exception.BadRequestException;
import vn.com.example.streamservice.exception.UnauthorizedException;
import vn.com.example.streamservice.repository.UserAuthTwitchRepository;
import vn.com.example.streamservice.repository.UserChannelTwitchRepository;
import vn.com.example.streamservice.repository.UserPlatformRepository;
import vn.com.example.streamservice.repository.UserRepository;
import vn.com.example.streamservice.service.TwitchStreamService;
import vn.com.example.streamservice.service.dto.TwitchInfoDto;
import vn.com.example.streamservice.service.dto.TwitchOAuthDto;
import vn.com.example.streamservice.service.dto.TwitchObjectInfoDto;
import vn.com.example.streamservice.service.dto.TwitchObjectStreamKeyDto;
import vn.com.example.streamservice.service.dto.TwitchStreamKeyDto;

@Service
@RequiredArgsConstructor
public class TwitchStreamServiceImpl implements TwitchStreamService {
    private final StreamProperties streamProperties;

    private final RestTemplate restTemplate;
    private final UserAuthTwitchRepository userAuthTwitchRepository;
    private final UserChannelTwitchRepository userChannelTwitchRepository;
    private final UserRepository userRepository;
    private final UserPlatformRepository userPlatformRepository;

    @Override
    public String oAuthTwitch(String code, String state) {
        User user = userRepository.findById(Long.parseLong(state))
                .orElseThrow(() -> new UnauthorizedException("user not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(CLIENT_ID, streamProperties.getTwitch().getOauth().getClientId());
        map.add(CLIENT_SECRET, streamProperties.getTwitch().getOauth().getClientSecret());
        map.add(GRANT_TYPE, AUTHORIZATION_CODE);
        map.add(CODE, code);
        map.add(REDIRECT_URI, streamProperties.getTwitch().getOauth().getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // OAuth twitch
        Optional<TwitchOAuthDto> twitchOAuthOtp = Optional.ofNullable(restTemplate.postForObject(
                streamProperties.getTwitch().getUrl().getAuthentication(), request, TwitchOAuthDto.class));
        if (!twitchOAuthOtp.isPresent()) {
            throw new BadRequestException("optional null");
        }

        // Save token and expries date
        UserAuthTwitch userAuthTwitch = new UserAuthTwitch();
        userAuthTwitch.setAccessToken(twitchOAuthOtp.get().getAccessToken());
        userAuthTwitch.setExpiresDate(Instant.now().plus(twitchOAuthOtp.get().getExpiresIn(), ChronoUnit.SECONDS));
        userAuthTwitch.setUser(user);
        userAuthTwitchRepository.save(userAuthTwitch);

        // Get info
        TwitchInfoDto twitchInfoDto = getTwitchInfo(twitchOAuthOtp.get().getAccessToken());

        // Get key stream
        TwitchStreamKeyDto twitchKeyStreamDto = getKeyStreamTwitch(twitchOAuthOtp.get().getAccessToken(),
                twitchInfoDto.getUserId());

        // Save UserChannelTwitch
        UserChannelTwitch userChannelTwitch = new UserChannelTwitch();
        userChannelTwitch.setChannelId(twitchInfoDto.getUserId());
        userChannelTwitch.setDisplayName(twitchInfoDto.getDisplayName());
        userChannelTwitch.setImageUrl(twitchInfoDto.getImageUrl());
        userChannelTwitch.setStreamKey(twitchKeyStreamDto.getStreamKey());
        userChannelTwitch.setUser(user);
        userChannelTwitchRepository.save(userChannelTwitch);

        // Save UserPlatfom
        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setPlatformType(PlatformType.TWITCH);
        userPlatform.setUser(user);
        userPlatformRepository.save(userPlatform);

        return CONNECT_SUCCESS;
    }

    private TwitchInfoDto getTwitchInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID_USER, streamProperties.getTwitch().getOauth().getClientId());
        headers.set(AUTHORIZATION, "Bearer" + " " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TwitchObjectInfoDto> dataInfo = restTemplate.exchange(
                streamProperties.getTwitch().getUrl().getUserInfo(), HttpMethod.GET, entity, TwitchObjectInfoDto.class);

        if (Objects.isNull(dataInfo.getBody())) {
            throw new BadRequestException("data null");
        }
        TwitchInfoDto twitchInfoDto = new TwitchInfoDto();
        dataInfo.getBody().getData().stream().forEach(data -> {
            twitchInfoDto.setUserId(data.getUserId());
            twitchInfoDto.setDisplayName(data.getDisplayName());
            twitchInfoDto.setImageUrl(data.getImageUrl());
        });
        return twitchInfoDto;
    }

    private TwitchStreamKeyDto getKeyStreamTwitch(String accessToken, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID_USER, streamProperties.getTwitch().getOauth().getClientId());
        headers.set(AUTHORIZATION, "Bearer" + " " + accessToken);

        Map<String, Long> params = new HashMap<>();
        params.put(BOARDCASTER_ID, userId);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<TwitchObjectStreamKeyDto> dataStreamKey = restTemplate.exchange(
                streamProperties.getTwitch().getUrl().getStreamKey(), HttpMethod.GET, entity,
                TwitchObjectStreamKeyDto.class, params);
        if (Objects.isNull(dataStreamKey.getBody())) {
            throw new BadRequestException("data null");
        }
        TwitchStreamKeyDto twitchStreamKeyDto = new TwitchStreamKeyDto();
        dataStreamKey.getBody().getData().forEach(data -> {
            twitchStreamKeyDto.setStreamKey(data.getStreamKey());
        });
        return twitchStreamKeyDto;
    }
}
