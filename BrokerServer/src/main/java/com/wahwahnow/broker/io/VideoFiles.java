package com.wahwahnow.broker.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoFiles {

    public static void storeData(byte[] data, int numberInLine, String videoName, String directory) throws IOException {

        // If user's directory doesn't exist create it {directory}
        // If song's directory doesn't exist create it {videoName}
        Files.createDirectories(Paths.get("root/temp/"));
        String filepath = "root/temp/"+directory+"_"+videoName+".mp4";
        System.out.println("Wrote data. Number of Line: "+numberInLine);
        // open File and write it: filename is {chunkNum.mp4} and the data are {chunk}
        File videoFile = new File(filepath);
        FileOutputStream out = numberInLine ==0? new FileOutputStream(videoFile, false) : new FileOutputStream(videoFile, true);
        out.write(data);
        out.flush();
        out.close();
    }

    public static void storeVideoMetadata(String videoName, String directory){

    }

}
