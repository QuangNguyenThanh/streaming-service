package vn.com.example.streamservice.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeSnippetDto {
    
    @JsonProperty("thumbnails")
    private YoutubeThumbanailsDto thumbnails;
    
    @JsonProperty("title")
    private String title;
    
}
