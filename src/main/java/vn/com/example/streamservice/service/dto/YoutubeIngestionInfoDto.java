package vn.com.example.streamservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeIngestionInfoDto {


       private String backupIngestionAddress;

       private String ingestionAddress;

       private String streamName;

       private String streamId;
}
