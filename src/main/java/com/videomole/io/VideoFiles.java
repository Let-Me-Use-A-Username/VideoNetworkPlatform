package com.videomole.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class VideoFiles {

    public static void storeChunk(byte[] chunk, String videoName, String directory, int chunkNum, String filetype) throws IOException {

        // If user's directory doesn't exist create it {directory}
        // If song's directory doesn't exist create it {videoName}
        Files.createDirectories(Paths.get("root/"+directory+"/"+videoName));

        // open File and write it: filename is {chunkNum.mp4} and the data are {chunk}
        Path chunkPath = Paths.get("root/"+directory+"/"+videoName+"/"+chunkNum+"."+filetype);

        BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(chunkPath.toFile()));
        write.write(chunk);
        write.flush();
        write.close();

    }

    public static void storeVideoMetadata(String videoName, String directory){

    }

}
