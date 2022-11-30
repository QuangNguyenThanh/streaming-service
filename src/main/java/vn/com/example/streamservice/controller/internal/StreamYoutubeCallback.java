package vn.com.example.streamservice.controller.internal;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.YoutubeStreamService;

@RestController
@RequiredArgsConstructor
public class StreamYoutubeCallback {

    private final YoutubeStreamService youtubeStreamService;

    @ApiOperation(value = "API to login")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/Callback", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<String> youtubeCallBackGetChannelInfo(@RequestParam String code, @RequestParam String state)
            throws IOException {
        return ResponseEntity.ok().body(youtubeStreamService.youtubeCallBackGetToken(code, state));
    }

}