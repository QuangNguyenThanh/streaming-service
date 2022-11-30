package vn.com.example.streamservice.controller.publics;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.YoutubeStreamService;
import vn.com.example.streamservice.service.dto.response.YoutubeChannelInfoResponseDto;
import vn.com.example.streamservice.service.dto.response.YoutubeCreateStreamResponseDto;

@RestController
@RequiredArgsConstructor
public class StreamYoutubePublicController {

    private final YoutubeStreamService youtubeStreamService;

    @ApiOperation(value = "API get channel info")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/youtube/channel-info", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<YoutubeChannelInfoResponseDto> getChannelInfo(@RequestHeader Long userId)
            throws GeneralSecurityException, IOException {
        return ResponseEntity.ok().body(youtubeStreamService.getChannelInfo(userId));
    }

    @ApiOperation(value = "API to create live streams")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/youtube/create-live-streams", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<YoutubeCreateStreamResponseDto> createLiveStream(@RequestHeader Long userId,
            @RequestHeader String channelId) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok().body(youtubeStreamService.createLiveStream(userId, channelId));
    }

    @ApiOperation(value = "API to get Link oAuth")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/youtube/oauth-youtube", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<String> getLinkOAuth(@RequestHeader Long userId) throws IOException {
        return ResponseEntity.ok().body(youtubeStreamService.getLinkOAuth(userId));
    }
}
