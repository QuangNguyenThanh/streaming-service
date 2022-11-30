package vn.com.example.streamservice.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.LiveStreamSnippet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveStreamYoutubeRequestDto {

    @JsonProperty("cdn")
    private CdnSettings cdnSettings;
    
    @JsonProperty("snippet")
    private LiveStreamSnippet snippet;
    
    @JsonProperty("kind")
    private String kind;
}
