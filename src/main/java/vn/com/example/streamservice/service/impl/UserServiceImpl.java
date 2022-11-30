package vn.com.example.streamservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.com.example.streamservice.entity.User;
import vn.com.example.streamservice.entity.UserChannelTwitch;
import vn.com.example.streamservice.entity.UserChannelYoutube;
import vn.com.example.streamservice.entity.UserSession;
import vn.com.example.streamservice.enums.PlatformType;
import vn.com.example.streamservice.exception.BadRequestException;
import vn.com.example.streamservice.repository.UserRepository;
import vn.com.example.streamservice.repository.UserSessionRepository;
import vn.com.example.streamservice.service.UserService;
import vn.com.example.streamservice.service.dto.response.UserDetailsResponseDto;
import vn.com.example.streamservice.service.dto.response.UserPlatformResponseDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserSessionRepository userSessionRepository;

    private final UserRepository userRepository;

    @Override
    public UserDetailsResponseDto getUserDetails(String sessionId) {
        UserSession userSession = userSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new BadRequestException("Not found"));
        return UserDetailsResponseDto.builder().sessionid(userSession.getSessionId())
                .userId(userSession.getUser().getId()).email(userSession.getUser().getEmail()).build();
    }

    @Transactional
    @Override
    public List<UserPlatformResponseDto> getPlatfomUser(Long userId) {
        User userPlatform = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("user not found"));
        List<UserChannelYoutube> youtubeUserList = userPlatform.getUserChannelYoutubes();
        List<UserPlatformResponseDto> list = new ArrayList<>();
        youtubeUserList.stream().forEach(data -> {
            UserPlatformResponseDto dto = new UserPlatformResponseDto();
            dto.setTitle(data.getDisplayName());
            dto.setChannelId(data.getChannelId());
            dto.setImage(data.getImageUrl());
            dto.setPlatformType(PlatformType.YOUTUBE);
            dto.setStreamkey(data.getStreamKey());
            list.add(dto);
        });

        List<UserChannelTwitch> twitchUserList = userPlatform.getUserChannelTwitchs();
        twitchUserList.stream().forEach(data -> {
            UserPlatformResponseDto dto = new UserPlatformResponseDto();
            dto.setTitle(data.getDisplayName());
            dto.setChannelId(data.getChannelId().toString());
            dto.setImage(data.getImageUrl());
            dto.setPlatformType(PlatformType.TWITCH);
            dto.setStreamkey(data.getStreamKey());
            list.add(dto);
        });
        return list;
    }

}
