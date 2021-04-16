package com.raccoon;

import com.raccoon.database.MainController;
import com.raccoon.models.VideoModel;
import com.videomole.Models.VideoDirectory;
import com.videomole.controllers.VideoController;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;


import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

public class Test {

    private static final String FileUrl = "Broker1.db";
    private static final String OSUrl = "D:/DS/brokers/db";

    public static void main(String[] args) {

        // Sqlite connection and setup
        MainController.createNewDatabase(FileUrl, OSUrl);



        // Fragmentation (upload)
        // Transfer some logic to the application server and some to the broker handler
//        VideoController ctrl = new VideoController();
//
//        VideoModel videoModel = new VideoModel();
//        String uploader = "_Amen_";
//        videoModel.setUploader(uploader);
//        uploader = Utils.sha1(uploader);
//        String videoName = "Pikachu Alex jones";
//        videoModel.setName(videoName);
//        videoName = Utils.sha1(videoName);
//        videoModel.setDescription("Mpla mpla");
//        videoModel.setViews(0);
//        videoModel.setId(videoName);
//        videoModel.setTags(new String[]{"Entertainment", "Parody"});
//        String timestamp = Instant.now().toString();
//        videoModel.setTimestamp(timestamp);
//
//        String filepath = Thread.currentThread().getContextClassLoader().getResource("root/Pica.mp4")
//                .getPath();
//
//        // get video length from source
//        File source = new File(filepath);
//        MultimediaObject sourceObj = new MultimediaObject(source);
//        try {
//            videoModel.setFrameRate(sourceObj.getInfo().getVideo().getFrameRate());
//            videoModel.setHeight(sourceObj.getInfo().getVideo().getSize().getHeight());
//            videoModel.setWidth(sourceObj.getInfo().getVideo().getSize().getWidth());
//            videoModel.setLength(sourceObj.getInfo().getDuration());
//        } catch (EncoderException e) {
//            e.printStackTrace();
//        }
//
//        int chunks = (int)videoModel.getLength() / 10000;
//        chunks = videoModel.getLength() % 10000 == 0? chunks : chunks + 1;
//
//        VideoDirectory videoDirectory = new VideoDirectory(videoModel, chunks, "root/"+uploader+"/"+videoName);
//        boolean created = ctrl.createVideoDirectory(source, videoDirectory);
//        if(created){
//            System.out.println("Fragmentation successful.");
//        }else{
//            System.out.println("Fragmentation failed.");
//        }

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
