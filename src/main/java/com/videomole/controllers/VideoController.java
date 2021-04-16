package com.videomole.controllers;

import com.videomole.Models.VideoDirectory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoController {

    public boolean createVideoDirectory(File tempSource, VideoDirectory videoDirectory) {

        // create users directory and video directory
        try {
            Files.createDirectories(Paths.get(videoDirectory.getVideoPath()));
        } catch (IOException e) {
            return false;
        }
        // fragment video
        boolean failure = false;
        for(int i = 0; i < videoDirectory.getChunks(); ++i) {
            int secOff = i*10;
            String targetName = videoDirectory.getVideoPath()+"/"+secOff+"_"+(secOff+10)+".mp4";
            System.out.println("Video target name is "+targetName);
            File target = new File(targetName);
            if(!fragmentation(tempSource, target, videoDirectory, 10.0f, i*10.0f)) {
                failure = true;
                break;
            }
        }
        // if failure delete everything and tempSource send status to application server
        if(failure){
            // delete stuff
            return false;
        }
        // if success delete tempSource send status to application server
        // delete tempSource
        return true;
    }

    public boolean fragmentation(File source, File target, VideoDirectory videoDirectory, float duration, float offset){

        try{
            AudioAttributes audio = new AudioAttributes();
            VideoAttributes video = new VideoAttributes();
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);
            attrs.setOffset(offset);
            attrs.setDuration(duration);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs, null);
        }catch (EncoderException e) {
           e.printStackTrace();
            // SystemLogger.Log("Failed to fragment "+videoDirectory.getVideoDetails().getName()+" at offset {"+offset+"}");
            return false;
        }

        return true;
    }

    public void deleteFailedFragmentation(){

    }

    public void stream(){

    }

}
