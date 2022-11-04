package com.example.speachtotext.client;

import com.google.gson.Gson;
import com.example.speachtotext.dto.TranscriptDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AssemblyAiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssemblyAiClient.class);

    private final RestTemplate restTemplate;

    @Value("${assemblyAi.api.key}")
    private String apiKey;
    @Value("${assemblyAi.api.upload.endpoint}")
    private String uploadApiEndpoint;
    @Value("${assemblyAi.api.transcript.endpoint}")
    private String transcriptApiEndpoint;


    //poki co nie uzywane
    public String uploadAudioFileTest() throws URISyntaxException, IOException, InterruptedException {

        File file = new File("record.wav");

        String filePath = file.getPath();

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/upload"))
                .header("Authorization", apiKey)
                .POST(HttpRequest.BodyPublishers.ofFile(Path.of(filePath)))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        //System.out.println(postResponse.body());
        //System.out.println(stringBuilder);

        return editResponse(postResponse.body());
    }



    public TranscriptDto transcript(String uploadUrl){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TranscriptDto transcriptDto = new TranscriptDto();
        transcriptDto.setAudio_url(uploadUrl);

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Gson gson = new Gson();
        String requestJson = gson.toJson(transcriptDto);

        //System.out.println(requestJson);

        URI url = UriComponentsBuilder.fromHttpUrl(transcriptApiEndpoint)
                .build()
                .encode()
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

         transcriptDto =
                restTemplate.postForObject(url, entity, TranscriptDto.class);

        //System.out.println(transcriptDto);

        return transcriptDto;

    }

    public TranscriptDto getTranscript(String transcriptId){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);


        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI url = UriComponentsBuilder.fromHttpUrl(transcriptApiEndpoint + "/" + transcriptId)
                .build()
                .encode()
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        try {
            ResponseEntity<TranscriptDto> webResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    TranscriptDto.class);


            return webResponse.getBody();

        }catch (RestClientException e){
            LOGGER.error(e.getMessage(), e);
            return new TranscriptDto();
        }
    }

    private String editResponse(String postResponse){

        StringBuilder stringBuilder = new StringBuilder(postResponse);
        stringBuilder.delete(0, 16);
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        return stringBuilder.toString();
    }



}
