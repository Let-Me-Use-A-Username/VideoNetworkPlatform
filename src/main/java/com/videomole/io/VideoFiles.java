package com.videomole.io;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class VideoFiles {

    public static void readMP4(String mp4filepath) throws ImageProcessingException, IOException {
        File mp4File = new File(mp4filepath);
        Metadata metadata = Mp4MetadataReader.readMetadata(mp4File);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }

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
