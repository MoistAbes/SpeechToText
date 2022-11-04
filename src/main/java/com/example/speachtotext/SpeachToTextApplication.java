package com.example.speachtotext;

import com.example.speachtotext.ui.ApplicationUI;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeachToTextApplication {


    public static void main(String[] args) {
        Application.launch(ApplicationUI.class, args);
    }

}
