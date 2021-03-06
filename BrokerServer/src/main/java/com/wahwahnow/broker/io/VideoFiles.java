package com.wahwahnow.broker.io;

import org.mrmtp.rpc.filetransfer.FileReader;

import java.io.*;
import java.net.Socket;
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

    public static boolean uploadFile(String filepath, int FILE_BUFFER_SIZE, int FILE_SIZE, Socket socket) throws IOException {

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();


        int send = 0;
        while (send < FILE_SIZE){
            byte[] data = FileReader.readFileData(filepath, FILE_BUFFER_SIZE, FILE_SIZE, send);
            if(data == null) return false;
            out.write(data);
            out.flush();
            send += data.length;
        }

        System.out.println("Send "+send+" bytes");

        return true;

//        int chunks = FILE_SIZE / FILE_BUFFER_SIZE + (FILE_SIZE % FILE_BUFFER_SIZE == 0? 0 : 1);
//        for(int i = 0; i < chunks; i++){
//            byte[] data = FileReader.readFileData(filepath, FILE_BUFFER_SIZE, FILE_SIZE, i * FILE_BUFFER_SIZE);
//            if(data == null) return false;
//            out.write(data);
//            out.flush();
//        }

        //return true;
    }

    public static boolean downloadFile(String filepath, int FILE_BUFFER_SIZE, int FILE_SIZE, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        int collected = 0;
        int count;
        byte[] data = new byte[FILE_BUFFER_SIZE];
        while((count = in.read(data)) > 0 && collected < FILE_SIZE) {
            byte[] toStore = new byte[count];
            System.arraycopy(
                    data,
                    0,
                    toStore,
                    0,
                    count
            );
            FileReader.writeFileData(filepath, toStore, collected);
            collected += count;
            data = new byte[FILE_BUFFER_SIZE];
        }

        System.out.println("Received ");

        return true;
    }

    public static byte[] downloadStream(int FILE_BUFFER_SIZE, int FILE_SIZE, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        byte[] stream = new byte[FILE_SIZE];
        int streamCollected = 0;

        int chunks = FILE_SIZE / FILE_BUFFER_SIZE + (FILE_SIZE % FILE_BUFFER_SIZE == 0 ? 0 : 1);

        for(int i = 0; i < chunks; i++){
            byte[] data = new byte[FILE_BUFFER_SIZE];
            int received = in.read(data, 0, FILE_BUFFER_SIZE);
            System.out.println("Received "+received);
            System.arraycopy(
                    data,
                    0,
                    stream,
                    streamCollected,
                    received
            );
            streamCollected += received;
        }

        return stream;
    }

}
