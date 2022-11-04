package com.example.speachtotext.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

@Service
public class AudioRecorderService {


    private boolean isRecording;

    private AudioFormat audioFormat;
    private DataLine.Info dataInfo;
    private TargetDataLine targetDataLine;

    public AudioRecorderService(){

        try {
             audioFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100,
                    16,
                    2,
                    4,
                    44100,
                    false
            );

             dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)){
                System.out.println("Not supported");
            }

             targetDataLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void startRecording(){

        isRecording = true;

        try {
            targetDataLine.open();
            targetDataLine.start();

            Thread audioRecordingThread = new Thread(){
                @Override public void run(){
                    AudioInputStream recordingStream = new AudioInputStream(targetDataLine);
                    File outputFile = new File("record.wav");
                    try {
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    }catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                }
            };
            audioRecordingThread.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void stopRecording(){
        targetDataLine.stop();
        targetDataLine.close();
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }


}
