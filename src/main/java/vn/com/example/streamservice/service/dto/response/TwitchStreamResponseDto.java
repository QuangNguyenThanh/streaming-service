package vn.com.example.streamservice.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwitchStreamResponseDto {
    private Long userId;
    private String displayName;
    private String imageUrl;
    private String streamKey;
}
