package com.example.speachtotext.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadDto {

    @JsonProperty("upload_url")
    private String url;

}
