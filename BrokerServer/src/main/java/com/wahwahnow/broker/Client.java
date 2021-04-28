package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mrmtp.rpc.Util;
import org.mrmtp.rpc.filetransfer.FileTransfer;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static Gson gson = new Gson();

    private static void uploadVideo(){
        Socket socket = null;
        String host = "192.168.84.1";

        String filepath = Thread.currentThread().getContextClassLoader().getResource("videos/Garfield.mp4").getPath();
        File videoFile = new File(filepath);

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(host+":3000");
        header.setKeepAlive(true);
        header.setConnection(200);
        header.setContentType("json");
        header.setMethodType(MethodConstants.POST);
        header.setMethod("/artist/videos");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", "7ae32df5e881e91bba528875c2985cb50b19e581");
        resBody.put("video", "baa7cc4b59b64fbe619ff3696e068603d603329e");
        resBody.put("fileSize", (int) videoFile.length());
        header.setBody(gson.toJson(resBody));
        header.setContentLength(header.getBody().length());

        try{
            socket = new Socket(host, 3000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(MRMTPBuilder.getMRMTPBuffer(header), 0, 1024);
            byte[] resBuf = Util.getNewBuffer(null, 1024);
            in.read(resBuf, 0, 1024);

            MRMTPHeader resHeader = MRMTPParser.parse(resBuf);

            JsonObject jsObj = JsonParser.parseString(resHeader.getBody()).getAsJsonObject();
            int BUFFER_SIZE = jsObj.get("bufferSize").getAsInt();

            FileTransfer.uploadFile(filepath, BUFFER_SIZE, (int) videoFile.length(), socket);

            System.out.println("Upload finished.");

            out.close();
            in.close();
            socket.close();

        }catch (IOException | InterruptedException e){ e.printStackTrace(); }
    }

    private static void streamVideo(){
        Socket socket = null;
        String host = "192.168.84.1";

        try{
            socket = new Socket(host, 3000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            MRMTPHeader header = new MRMTPHeader();
            header.setDestination(host+":3000");
            header.setKeepAlive(true);
            header.setConnection(200);
            header.setContentType("json");
            header.setMethodType(MethodConstants.GET);
            header.setMethod("/artist/videos");
            Map<String, Object> resBody = new HashMap<>();
            resBody.put("artist", "7ae32df5e881e91bba528875c2985cb50b19e581");
            resBody.put("video", "baa7cc4b59b64fbe619ff3696e068603d603329e");
            header.setBody(gson.toJson(resBody));
            header.setContentLength(header.getBody().length());

            out.write(MRMTPBuilder.getMRMTPBuffer(header), 0, 1024);

            byte[] resBuf = Util.getNewBuffer(null, 1024);
            in.read(resBuf, 0, 1024);

            MRMTPHeader

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args){
        System.out.println("Run client");
        uploadVideo();
        streamVideo();
    }

}