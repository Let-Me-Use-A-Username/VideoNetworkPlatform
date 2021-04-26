package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static Gson gson = new Gson();

    private static void uploadVideo(){
        Socket socket = null;
        String host = "192.168.84.1";

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(host+":3000");
        header.setKeepAlive(true);
        header.setConnection(200);
        header.setContentType("json");
        header.setMethodType(MethodConstants.POST);
        header.setMethod("/artist/videos");
        header.setBody(
                """
                    {
                        "artist": "7AE32DF5E881E91BBA528875C2985CB50B19E581",
                        "video": "BAA7CC4B59B64FBE619FF3696E068603D603329E"
                    }
                """
        );
        header.setContentLength(header.getBody().length());

        try{
            socket = new Socket(host, 3000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            byte[] header_buffer = new byte[8192];
            byte[] headerProduct = MRMTPBuilder.get(header).getBytes(StandardCharsets.UTF_8);
            System.arraycopy(
                    headerProduct,
                    0,
                    header_buffer,
                    0,
                    headerProduct.length
            );

            out.write(header_buffer, 0, 8192);

            byte[] resBuf = new byte[8192];
            in.read(resBuf, 0 , 8192);

            MRMTPHeader res = MRMTPParser.parse(new String(resBuf, StandardCharsets.UTF_8));
            System.out.println("Body is "+res.getBody());
            JsonObject jsObj = JsonParser.parseString(res.getBody()).getAsJsonObject();
            int dataSize = jsObj.get("chunkSize").getAsInt();

            // get video chunks
            String filepath = Thread.currentThread().getContextClassLoader().getResource("videos/Garfield.mp4").getPath();
            File videoFile = new File(filepath);
            FileInputStream fileInput = new FileInputStream(videoFile);
            int chunks = (int)videoFile.length() / dataSize + (videoFile.length() % dataSize != 0? 1 : 0);
            // send chunks to the server with files
            Map<String, Object> resBody = new HashMap<>();
            resBody.put("artist", "7ae32df5e881e91bba528875c2985cb50b19e581");
            resBody.put("video", "baa7cc4b59b64fbe619ff3696e068603d603329e");
            resBody.put("chunks", chunks);
            resBody.put("fileSize", (int) videoFile.length());
            header.setBody(gson.toJson(resBody));
            System.out.println("About to send "+header.getBody());
            byte[] header_buffer2 = new byte[8192];
            byte[] headerProduct2 = MRMTPBuilder.get(header).getBytes(StandardCharsets.UTF_8);
            System.arraycopy(
                    headerProduct2,
                    0,
                    header_buffer2,
                    0,
                    headerProduct2.length
            );
            out.write(header_buffer2, 0, 8192);
            int send = 0;
            for(int i = 0; i < chunks; i++){
                byte[] tempBuffer = new byte[dataSize];
                int len = (i == chunks - 1 && videoFile.length() % dataSize != 0)
                        ? (int) videoFile.length() % dataSize
                        : dataSize;
                send += len;
                fileInput.skip((long) i * dataSize);
                fileInput.read(tempBuffer, 0, len);
                System.out.println("About to send file of" + tempBuffer.length);
                out.write(tempBuffer);
                out.flush();
                System.out.println("FileSize is "+videoFile.length()+"\t\tChunk number is "+i+"\t\tLength will be "+len+"\t\tSend: "+send);
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){}
            }
            System.out.println("I am waiting.");
            // receive status
            in.read(resBuf, 0 , 8192);

            res = MRMTPParser.parse(new String(resBuf, StandardCharsets.UTF_8));
            jsObj = JsonParser.parseString(res.getBody()).getAsJsonObject();
            System.out.println("Has been uploaded: "+ jsObj.get("uploaded").getAsString());

            out.close();
            in.close();
            socket.close();

        }catch (IOException e){ e.printStackTrace(); }
    }

    public static void main(String[] args){
        System.out.println("Run client");
        uploadVideo();
    }

}