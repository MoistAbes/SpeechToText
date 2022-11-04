package com.example.speachtotext.client;

import com.example.speachtotext.dto.TranscriptDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
@RequiredArgsConstructor
public class AssemblyAiClientFacade {

    private final AssemblyAiClient assemblyAiClient;
    public static String text;

    private static final Logger LOGGER = LoggerFactory.getLogger(AssemblyAiClientFacade.class);


    public String process() {

        String url = null;
        try {
            url = assemblyAiClient.uploadAudioFileTest();
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        TranscriptDto transcriptDto = assemblyAiClient.transcript(url);

        while (!transcriptDto.getStatus().equals("completed")) {
            transcriptDto = assemblyAiClient.getTranscript(transcriptDto.getId());
            LOGGER.info(transcriptDto.toString());
        }

        text = transcriptDto.getText();

        return text;
    }


}
