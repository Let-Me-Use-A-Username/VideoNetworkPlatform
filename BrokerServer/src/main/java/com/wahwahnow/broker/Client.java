package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wahwahnow.broker.io.FileReader;
import com.wahwahnow.broker.io.VideoFiles;
import com.wahwahnow.broker.models.FragmentModel;
import com.wahwahnow.broker.models.VideoFragments;
import org.mrmtp.rpc.Util;
import org.mrmtp.rpc.filetransfer.FileTransfer;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private static Gson gson = new Gson();
    private static int HEADER_BUFFER_SIZE = 2048;

    private static void uploadVideo(String artistHash, String videoHash){
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
        header.setMethod("/artist/video");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", artistHash);
        resBody.put("video", videoHash);
        resBody.put("fileSize", (int) videoFile.length());
        header.setBody(gson.toJson(resBody));
        header.setContentLength(header.getBody().length());

        try{
            socket = new Socket(host, 3000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(MRMTPBuilder.getMRMTPBuffer(header, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);
            byte[] resBuf = Util.getNewBuffer(null, HEADER_BUFFER_SIZE);
            in.read(resBuf, 0, HEADER_BUFFER_SIZE);

            MRMTPHeader resHeader = MRMTPParser.parse(resBuf);

            JsonObject jsObj = JsonParser.parseString(resHeader.getBody()).getAsJsonObject();
            int BUFFER_SIZE = jsObj.get("bufferSize").getAsInt();

            FileTransfer.uploadFile(filepath, BUFFER_SIZE, (int) videoFile.length(), socket);

            System.out.println("Upload finished.");

            out.close();
            in.close();
            socket.close();

        }catch (IOException e){ e.printStackTrace(); }
    }

    private static VideoFragments getVideoDetails(String artistHash, String videoHash){
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
            header.setMethod("/artist/video");
            Map<String, Object> resBody = new HashMap<>();
            resBody.put("artist", artistHash);
            resBody.put("video", videoHash);
            header.setBody(gson.toJson(resBody));
            header.setContentLength(header.getBody().length());

            out.write(MRMTPBuilder.getMRMTPBuffer(header, 2048), 0, HEADER_BUFFER_SIZE);

            byte[] resBuf = Util.getNewBuffer(null, HEADER_BUFFER_SIZE);
            in.read(resBuf, 0, HEADER_BUFFER_SIZE);

            // response total chunks
            MRMTPHeader response = MRMTPParser.parse(resBuf);

            JsonObject jsObj = JsonParser.parseString(response.getBody()).getAsJsonObject();
            int totalChunks = jsObj.get("totalChunks").getAsInt();
            int lastFragmentID = jsObj.get("lastFragmentID").getAsInt();

            out.close();
            in.close();
            socket.close();

            VideoFragments videoFragments = new VideoFragments();
            videoFragments.setChunks(totalChunks);
            videoFragments.setLastFragmentID(lastFragmentID);

            return videoFragments;

        } catch (IOException e) {
            return null;
        }


    }

    public static void getFragments(VideoFragments videoFragments, String artistHash, String videoHash){

        Socket socket = null;
        String host = "192.168.84.1";

        MRMTPHeader request = new MRMTPHeader();
        request.setDestination(host+":3000");
        request.setKeepAlive(true);
        request.setConnection(200);
        request.setContentType("json");
        request.setMethodType(MethodConstants.GET);
        request.setMethod("/artist/video/fragments");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", "7ae32df5e881e91bba528875c2985cb50b19e581");
        resBody.put("video", "baa7cc4b59b64fbe619ff3696e068603d603329e");


        try{
            socket = new Socket(host, 3000);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            List<FragmentModel> fragmentIDs = null;
            int bufferSize = 0;

            // first contact
            if(videoFragments.getLastContactTimestamp() == 0){
                resBody.put("buffering", true);
                request.setBody(gson.toJson(resBody));
                request.setContentLength(request.getBody().length());
                out.write(MRMTPBuilder.getMRMTPBuffer(request, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

                byte[] resBuf = Util.getNewBuffer(null, HEADER_BUFFER_SIZE);
                in.read(resBuf, 0, HEADER_BUFFER_SIZE);

                MRMTPHeader response = MRMTPParser.parse(resBuf);

                JsonObject jsObj = JsonParser.parseString(response.getBody()).getAsJsonObject();
                Type listType = new TypeToken<List<FragmentModel>>(){}.getType();
                fragmentIDs = gson.fromJson(jsObj.get("fragments"), listType);
                bufferSize = jsObj.get("bufferSize").getAsInt();


            } else {
                long timestamp = System.currentTimeMillis();
                if(timestamp - videoFragments.getLastContactTimestamp() > 4) {
                    resBody.put("buffering", false);
                    resBody.put("currentSeek", videoFragments.getCurrentSeek());
                    request.setBody(gson.toJson(resBody));
                    request.setContentLength(request.getBody().length());
                    out.write(MRMTPBuilder.getMRMTPBuffer(request, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

                    byte[] resBuf = Util.getNewBuffer(null, HEADER_BUFFER_SIZE);
                    in.read(resBuf, 0, HEADER_BUFFER_SIZE);

                    MRMTPHeader response = MRMTPParser.parse(resBuf);

                    JsonObject jsObj = JsonParser.parseString(response.getBody()).getAsJsonObject();
                    Type listType = new TypeToken<List<FragmentModel>>(){}.getType();
                    fragmentIDs = gson.fromJson(jsObj.get("fragments"), listType);
                    bufferSize = jsObj.get("bufferSize").getAsInt();
                }  else {
                    return;
                }
            }

            int BUFFER_SIZE = bufferSize;

            out.close();
            in.close();
            socket.close();

            fragmentIDs.forEach((fragmentModel -> {
                new Thread(() -> {
                    try {
                        fragmentsTask(videoFragments, fragmentModel, BUFFER_SIZE);
                    } catch (IOException ignored) { }
                }).start();
            }));

        }catch (IOException ignored){ }

    }

    private static void fragmentsTask(VideoFragments videoFragments, FragmentModel fragmentModel, int BUFFER_SIZE) throws IOException {
        Socket socket = null;
        String host = "192.168.84.1";

        MRMTPHeader request = new MRMTPHeader();
        request.setDestination(host+":3000");
        request.setKeepAlive(true);
        request.setConnection(200);
        request.setContentType("json");
        request.setMethodType(MethodConstants.GET);
        request.setMethod("/artist/video/fragment/mp4");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", "7ae32df5e881e91bba528875c2985cb50b19e581");
        resBody.put("video", "baa7cc4b59b64fbe619ff3696e068603d603329e");
        resBody.put("fragmentID", fragmentModel.getFragmentID());

        try {
            socket = new Socket(host, 3000);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            request.setBody(gson.toJson(resBody));
            request.setContentLength(request.getBody().length());
            out.write(MRMTPBuilder.getMRMTPBuffer(request, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            org.mrmtp.rpc.filetransfer.FileReader.createDirectoryPath("./testVideo");
            VideoFiles.downloadFile("./testVideo/"+fragmentModel.getFragmentID()+".mp4", BUFFER_SIZE, (int) fragmentModel.getFragmentSize(), socket);
            //videoFragments.put(fragmentID.getFragmentID(), data);

            out.close();
            in.close();
            socket.close();

        }catch (IOException ignored){ }

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Run client");

        String artist = "7ae32df5e881e91bba528875c2985cb50b19e581";
        String video = "baa7cc4b59b64fbe619ff3696e068603d603329e";

        VideoFragments videoFragments = getVideoDetails(artist, video);
        if(videoFragments == null) {
            System.out.println("Something went wrong");
            System.exit(-1);
        }

        System.out.println("Total chunks are: "+videoFragments.getChunks()+"\tLast Fragment ID is: "+videoFragments.getLastFragmentID());

       // while(true){
         //   if(videoFragments.isLastFragment()) break;
            getFragments(videoFragments, artist, video);
            Thread.sleep(1000);
          //  if(videoFragments.isLastFragment()) break;
           // videoFragments.setCurrentSeek(videoFragments.getCurrentSeek() + 1);
        //}
//        for(Integer fragmentID: videoFragments.getChunkNumbers()){
//            org.mrmtp.rpc.filetransfer.FileReader.createDirectoryPath("./clientTest");
//            org.mrmtp.rpc.filetransfer.FileReader.writeFileData("./clientTest/"+fragmentID+".mp4", videoFragments.getFragment(fragmentID), 0);
//        }
    }

}