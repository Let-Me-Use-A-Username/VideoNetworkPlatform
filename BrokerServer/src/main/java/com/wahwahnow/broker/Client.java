package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private static Gson gson = new Gson();

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

            out.write(MRMTPBuilder.getMRMTPBuffer(header), 0, 1024);

            byte[] resBuf = Util.getNewBuffer(null, 1024);
            in.read(resBuf, 0, 1024);

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

            // first contact
            if(videoFragments.getLastContactTimestamp() == 0){
                request.setBody(gson.toJson(resBody));
                request.setContentLength(request.getBody().length());
                out.write(MRMTPBuilder.getMRMTPBuffer(request), 0, 1024);

                byte[] resBuf = Util.getNewBuffer(null, 1024);
                in.read(resBuf, 0, 1024);

                MRMTPHeader response = MRMTPParser.parse(resBuf);

                fragmentsTask(response, videoFragments, socket);
                return;
            }

            long timestamp = System.currentTimeMillis();
            if(timestamp - videoFragments.getLastContactTimestamp() > 4) {
                resBody.put("currentSeek", videoFragments.getCurrentSeek());
                request.setBody(gson.toJson(resBody));
                request.setContentLength(request.getBody().length());
                out.write(MRMTPBuilder.getMRMTPBuffer(request), 0, 1024);

                byte[] resBuf = Util.getNewBuffer(null, 1024);
                in.read(resBuf, 0, 1024);

                MRMTPHeader response = MRMTPParser.parse(resBuf);

                fragmentsTask(response, videoFragments, socket);
            }

        }catch (IOException e){ }

    }

    private static void fragmentsTask(MRMTPHeader response, VideoFragments videoFragments, Socket socket) throws IOException {
        JsonObject jsObj = JsonParser.parseString(response.getBody()).getAsJsonObject();

        if(!jsObj.has("fragments")) {
            videoFragments.setLastContactTimestamp(System.currentTimeMillis());
            return;
        }

        Type listType = new TypeToken<List<FragmentModel>>(){}.getType();
        List<FragmentModel> fragmentIDs = gson.fromJson(jsObj.get("fragments"), listType);

        int BUFFER_SIZE = jsObj.get("bufferSize").getAsInt();

        videoFragments.setLastContactTimestamp(System.currentTimeMillis());
        for(int i = 0; i < fragmentIDs.size(); i++){
            byte[] data = FileTransfer.downloadStream(BUFFER_SIZE, (int) fragmentIDs.get(i).getFragmentSize(),socket);
            videoFragments.put(fragmentIDs.get(i).getFragmentID(), data);
        }
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

//        while(true){
//            if(videoFragments.isLastFragment()) break;
//            getFragments(videoFragments, artist, video);
//            Thread.sleep(1000);
//            if(videoFragments.isLastFragment()) break;
//            videoFragments.setCurrentSeek(videoFragments.getCurrentSeek() + 1);
//        }
//        for(Integer fragmentID: videoFragments.getChunkNumbers()){
//            FileReader.createDirectoryPath("./clientTest");
//            FileReader.writeFileData("./clientTest/"+fragmentID, videoFragments.getFragment(fragmentID), 0);
//        }
    }

}