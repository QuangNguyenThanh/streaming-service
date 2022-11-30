package vn.com.example.streamservice.controller.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.TwitchStreamService;

@RestController
@RequiredArgsConstructor
public class TwitchOAuthController {
    private final TwitchStreamService twitchStreamService;

    @ApiOperation(value = "API to oAuth twtich")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/oauth_twitch", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<String> getOAuthTwitch(@RequestParam String code, @RequestParam String state){
        return ResponseEntity.ok().body(twitchStreamService.oAuthTwitch(code, state));
    }

}
