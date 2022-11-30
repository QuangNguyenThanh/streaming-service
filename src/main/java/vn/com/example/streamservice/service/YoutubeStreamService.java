package vn.com.example.streamservice.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import vn.com.example.streamservice.service.dto.response.YoutubeChannelInfoResponseDto;
import vn.com.example.streamservice.service.dto.response.YoutubeCreateStreamResponseDto;

public interface YoutubeStreamService {

    YoutubeChannelInfoResponseDto getChannelInfo(Long userId) throws GeneralSecurityException, IOException;

    YoutubeCreateStreamResponseDto createLiveStream(Long userId, String channelId) throws GeneralSecurityException, IOException;

    String youtubeCallBackGetToken(String code, String state) throws IOException;

    String getLinkOAuth(Long userId) throws IOException;

}
