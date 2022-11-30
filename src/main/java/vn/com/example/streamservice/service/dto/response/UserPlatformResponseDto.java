package vn.com.example.streamservice.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.example.streamservice.enums.PlatformType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlatformResponseDto {

    private String title;
    private String channelId;
    private String image;
    private PlatformType platformType;
    private String streamkey;
}
