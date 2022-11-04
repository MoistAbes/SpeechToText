package com.example.speachtotext.service;

import com.example.speachtotext.client.AssemblyAiClientFacade;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GlobalKeyListenerService implements NativeKeyListener {

    private final AudioRecorderService audioRecorderService;
    private final AssemblyAiClientFacade assemblyAiClientFacade;



    public void nativeKeyPressed(NativeKeyEvent e) {
       // System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE && !audioRecorderService.isRecording()){
            System.out.println("started recording");
            audioRecorderService.startRecording();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
       // System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE && audioRecorderService.isRecording()){
            System.out.println("stopped recording");
            audioRecorderService.stopRecording();

            Platform.runLater(() -> {
                copyText(assemblyAiClientFacade.process());
                try {
                    playSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                System.out.println("dzieje sie platform: " + AssemblyAiClientFacade.text);

            });

        }
    }

    public void registerNativeKeyListeners() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerService(audioRecorderService, assemblyAiClientFacade));
    }

    private void copyText(String text){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
        System.out.println("Co jest w clipboard: " +clipboard.getString());
    }

    public static synchronized void playSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        File file = new File("boing.wav");

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.

        clip.start();
    }

}
