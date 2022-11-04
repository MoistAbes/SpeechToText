package com.example.speachtotext.ui;

import com.example.speachtotext.service.GlobalKeyListenerService;
import com.example.speachtotext.ui.ApplicationUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class StageListener implements ApplicationListener<ApplicationUI.StageReadyEvent> {

    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext ac;
    private final GlobalKeyListenerService example;

    public StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
                         @Value("classpath:/ui.fxml") Resource resource,
                         ApplicationContext ac,
                         GlobalKeyListenerService example
    ){
        this.applicationTitle = applicationTitle;
        this.fxml = resource;
        this.ac = ac;
        this.example = example;
    }

    @Override
    public void onApplicationEvent(ApplicationUI.StageReadyEvent event) {

        example.registerNativeKeyListeners();
        ExecutorService executor = Executors.newCachedThreadPool();

        Stage stage = event.getStage();
        try {
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(ac::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
