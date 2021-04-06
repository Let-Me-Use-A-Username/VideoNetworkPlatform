package com.raccoon;

import com.drew.imaging.ImageProcessingException;
import com.videomole.io.VideoFiles;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main(String[] args){

        String filepath = Thread.currentThread().getContextClassLoader().getResource("root/HelloThere.mp4")
                .getPath();

        try{
            //VideoFiles.readMP4(filepath);

            File file = new File(filepath);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            byte[] fileContent = in.readAllBytes();   //Files.readAllBytes(file.toPath());
            System.out.println("test");
            ArrayList<byte[]> chunks = getChunks(fileContent);
            for(int i = 0; i < chunks.size(); i++){
                VideoFiles.storeChunk(chunks.get(i), "skyrim", "alex", i, "mp4");
            }

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    private static final int MEGABYTE_SIZE = 1048576;

    private static ArrayList<byte[]> getChunks(byte[] byteArray){
        int byteChunksBuffers = byteArray.length / MEGABYTE_SIZE;
        int remainderBytes = byteArray.length % MEGABYTE_SIZE;
        if(remainderBytes != 0) byteChunksBuffers++;

        ArrayList<byte[]> arrayList = new ArrayList<>();

        if(byteChunksBuffers == 1) {
            arrayList.add(byteArray);
            return arrayList;
        }


        for(int i = 0; i < byteChunksBuffers; i++){
            if(i + 1 != byteChunksBuffers){
                byte[] temp = new byte[MEGABYTE_SIZE];
                System.arraycopy(byteArray, i * MEGABYTE_SIZE, temp, 0, MEGABYTE_SIZE);
                arrayList.add(temp);
            } else {
                System.out.println("Testtest");
                int remainder = byteArray.length - i * MEGABYTE_SIZE;
                System.out.println("ByteArray length: "+byteArray.length+"\nRemainder: "+remainder);
                byte[] temp = new byte[(int)remainder];
                System.arraycopy(byteArray, i * MEGABYTE_SIZE, temp, 0, remainder);
                arrayList.add(temp);
            }
        }

        return arrayList;
    }
}
