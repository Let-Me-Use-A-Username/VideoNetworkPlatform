package com.wahwahnow.broker;

import com.wahwahnow.broker.models.VideoDirectory;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import java.io.File;

public class Test {

    public static void main(String[] args){
        String artist = "7ae32df5e881e91bba528875c2985cb50b19e581";
        String video = "baa7cc4b59b64fbe619ff3696e068603d603329e";
        String filepath = "./temp/7ae32df5e881e91bba528875c2985cb50b19e581-baa7cc4b59b64fbe619ff3696e068603d603329e.mp4";
        File file = new File(filepath);

        VideoDirectory videoDirectory = new VideoDirectory();
        videoDirectory.setUploaderID(artist);
        videoDirectory.setVideoPath("root/"+artist+"/"+video);
        videoDirectory.setHits(0);
        videoDirectory.setId(video);
        MultimediaObject sourceObj = new MultimediaObject(file);
        try {
            videoDirectory.setFramerate(sourceObj.getInfo().getVideo().getFrameRate());
            videoDirectory.setHeight(sourceObj.getInfo().getVideo().getSize().getHeight());
            videoDirectory.setWidth(sourceObj.getInfo().getVideo().getSize().getWidth());
            videoDirectory.setLength(sourceObj.getInfo().getDuration());
        } catch (EncoderException e) {
            e.printStackTrace();
            return ;
        }

        System.out.println("Length of video is "+videoDirectory.getLength());
        int chunks = (int) ( videoDirectory.getLength() / (1000 * 10)) + (int) (videoDirectory.getLength() % (1000 * 10.0) != 0? 1 : 0);
        System.out.println("Chunks are "+chunks);
    }
}
