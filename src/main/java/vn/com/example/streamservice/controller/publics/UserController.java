package vn.com.example.streamservice.controller.publics;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.UserService;
import vn.com.example.streamservice.service.dto.response.UserDetailsResponseDto;
import vn.com.example.streamservice.service.dto.response.UserPlatformResponseDto;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "API to getUserDetails")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/user/user-details", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<UserDetailsResponseDto> getUserDetails(@RequestHeader String sessionId) {
        return ResponseEntity.ok().body(userService.getUserDetails(sessionId));
    }
    
    @ApiOperation(value = "API to get platform of user")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(value = "/user/platform", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<List<UserPlatformResponseDto>> getPlatfomUser(@RequestHeader Long userId) {
        return ResponseEntity.ok().body(userService.getPlatfomUser(userId));
    }
}
