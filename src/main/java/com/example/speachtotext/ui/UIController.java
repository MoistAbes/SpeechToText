package com.example.speachtotext.ui;

import com.example.speachtotext.client.AssemblyAiClient;
import com.example.speachtotext.service.AudioRecorderService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UIController {

    private final AudioRecorderService service;
    private final AssemblyAiClient assemblyAiClient;

    @FXML
    Label label;
    @FXML
    Label testLabel;

    @FXML
    public void initialize(){
    }

}
