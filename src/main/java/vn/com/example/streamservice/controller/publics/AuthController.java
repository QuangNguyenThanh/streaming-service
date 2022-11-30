package vn.com.example.streamservice.controller.publics;

import static vn.com.example.streamservice.constant.Constants.X_SESSION_ID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.service.AuthService;
import vn.com.example.streamservice.service.dto.ResetPasswordDto;
import vn.com.example.streamservice.service.dto.UserDto;
import vn.com.example.streamservice.service.dto.request.LoginDto;
import vn.com.example.streamservice.service.dto.request.SignupDto;
import vn.com.example.streamservice.service.dto.response.LoginResultDto;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "API to login")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @PostMapping(value = "/login", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<LoginResultDto> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @ApiOperation(value = "API to logout")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @PostMapping(value = "/logout", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<?> logout(@RequestHeader(X_SESSION_ID) String sessionId) {
        authService.logout(sessionId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "API to signup")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @PostMapping(value = "/signup", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok(authService.signup(signupDto));
    }


    @ApiOperation(value = "API to reset password")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successully"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @PostMapping(value = "/reset-password", produces = "application/vn.sparkminds.api-v1+json")
    public ResponseEntity<Boolean> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }
}
