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
public class SnippetBoardcastDto {

    @JsonProperty("scheduledStartTime")
    private String scheduledStartTime;
    
    @JsonProperty("title")
    private String title;
}
