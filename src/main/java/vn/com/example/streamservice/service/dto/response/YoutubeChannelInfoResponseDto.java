package vn.com.example.streamservice.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeChannelInfoResponseDto {

    private String title;
    private String channelId;
    private String image;

}
