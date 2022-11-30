package vn.com.example.streamservice.service;

import java.util.List;

import vn.com.example.streamservice.service.dto.response.UserDetailsResponseDto;
import vn.com.example.streamservice.service.dto.response.UserPlatformResponseDto;

public interface UserService {

    UserDetailsResponseDto getUserDetails(String sessionId);

    List<UserPlatformResponseDto> getPlatfomUser(Long userId);

}
