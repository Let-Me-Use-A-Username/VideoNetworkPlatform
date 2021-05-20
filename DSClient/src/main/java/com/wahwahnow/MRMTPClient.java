package com.wahwahnow;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.mrmtp.rpc.Util;
import org.mrmtp.rpc.filetransfer.FileTransfer;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MRMTPClient {

    private static Gson gson = new Gson();
    private static int HEADER_BUFFER_SIZE = 2048;

    public static int uploadVideo(String artistHash, String videoHash, String brokerAddress, String filepath){
        Socket socket = null;
        String[] parts = brokerAddress.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        File videoFile = new File(filepath);

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(brokerAddress);
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
            socket = new Socket(host, port);

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

            return 200;
        }catch (IOException e){ return 502; }
    }

    public static VideoFragments getVideoDetails(String artistHash, String videoHash,  String brokerAddress){
        Socket socket = null;
        String[] parts = brokerAddress.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        try{
            socket = new Socket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            MRMTPHeader header = new MRMTPHeader();
            header.setDestination(brokerAddress);
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

    public static void getFragments(VideoFragments videoFragments, String artistHash, String videoHash,  String brokerAddress, String directory){

        Socket socket = null;
        String[] parts = brokerAddress.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        MRMTPHeader request = new MRMTPHeader();
        request.setDestination(brokerAddress);
        request.setKeepAlive(true);
        request.setConnection(200);
        request.setContentType("json");
        request.setMethodType(MethodConstants.GET);
        request.setMethod("/artist/video/fragments");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", artistHash);
        resBody.put("video", videoHash);


        try{
            socket = new Socket(host, port);
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
                        fragmentsTask(videoFragments, artistHash, videoHash, fragmentModel, brokerAddress, BUFFER_SIZE, directory);
                    } catch (IOException ignored) { }
                }).start();
            }));

        }catch (IOException ignored){ }

    }

    public static void fragmentsTask(VideoFragments videoFragments, String artistHash, String videoHash, FragmentModel fragmentModel,  String brokerAddress,  int BUFFER_SIZE, String directory) throws IOException {
        Socket socket = null;
        String[] parts = brokerAddress.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        MRMTPHeader request = new MRMTPHeader();
        request.setDestination(brokerAddress);
        request.setKeepAlive(true);
        request.setConnection(200);
        request.setContentType("json");
        request.setMethodType(MethodConstants.GET);
        request.setMethod("/artist/video/fragment/mp4");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", artistHash);
        resBody.put("video", videoHash);
        resBody.put("fragmentID", fragmentModel.getFragmentID());

        try {
            socket = new Socket(host, port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            request.setBody(gson.toJson(resBody));
            request.setContentLength(request.getBody().length());
            out.write(MRMTPBuilder.getMRMTPBuffer(request, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            FileTransfer.downloadFile(directory+"/"+fragmentModel.getFragmentID()+".mp4", BUFFER_SIZE, (int) fragmentModel.getFragmentSize(), socket);
            //byte[] fragment = FileTransfer.downloadStream()
            //videoFragments.put(fragmentID.getFragmentID(), data);

            out.close();
            in.close();
            socket.close();

        }catch (IOException ignored){ }

    }

    public static boolean checkAlive(String brokerAddress){
        Socket socket = null;
        String[] parts = brokerAddress.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(brokerAddress);
        header.setKeepAlive(true);
        header.setConnection(200);
        header.setContentType("");
        header.setMethodType(MethodConstants.GET);
        header.setMethod("/checkAlive");
        header.setContentLength(0);

        try{
            socket = new Socket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(MRMTPBuilder.getMRMTPBuffer(header, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            out.close();
            in.close();
            socket.close();

            return true;

        }catch (IOException e){ return false; }
    }

}
