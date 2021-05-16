package com.wahwahnow;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.net.Socket;
import java.util.HashMap;
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

}
