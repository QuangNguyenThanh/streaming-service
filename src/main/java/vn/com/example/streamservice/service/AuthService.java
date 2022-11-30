package vn.com.example.streamservice.service;

import vn.com.example.streamservice.service.dto.ResetPasswordDto;
import vn.com.example.streamservice.service.dto.UserDto;
import vn.com.example.streamservice.service.dto.request.LoginDto;
import vn.com.example.streamservice.service.dto.request.SignupDto;
import vn.com.example.streamservice.service.dto.response.LoginResultDto;

public interface AuthService {

    void logout(String sessionId);

    LoginResultDto login(LoginDto loginDto);

    UserDto signup(SignupDto signupDto);

    Boolean resetPassword(ResetPasswordDto dto);

}
