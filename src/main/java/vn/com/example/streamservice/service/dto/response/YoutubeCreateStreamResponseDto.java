package vn.com.example.streamservice.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeCreateStreamResponseDto {
    
    private String backupIngestionAddress;

    private String ingestionAddress;

    private String streamName;

    private String streamId;
}
