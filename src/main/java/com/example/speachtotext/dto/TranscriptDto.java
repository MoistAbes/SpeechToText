package com.example.speachtotext.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TranscriptDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("audio_url")
    private String audio_url;

    @JsonProperty("text")
    private String text;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error")
    private String error;


}
