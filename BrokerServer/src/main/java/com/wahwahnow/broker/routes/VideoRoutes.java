package com.wahwahnow.broker.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wahwahnow.broker.BrokerData;
import com.wahwahnow.broker.controllers.VideoController;
import com.wahwahnow.broker.io.VideoFiles;
import com.wahwahnow.broker.models.VideoDirectory;
import org.mrmtp.rpc.filetransfer.FileTransfer;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;
import org.mrmtp.rpc.router.MethodCall;
import org.mrmtp.rpc.filetransfer.FileReader;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class VideoRoutes {

    private static Gson gson = new Gson();
    private static VideoController videoController = new VideoController();
    private static int VIDEO_BUFFER = 1048576;

    public static void init(){
        // Get artist video (streaming)
        BrokerData.getInstance().getBrokerRouter().GET("/artist/video", (socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            String body = mrmtpHeader.getBody();
            JsonObject jsObj = JsonParser.parseString(body).getAsJsonObject();

            String artist = jsObj.get("artist").getAsString();

            System.out.println("We got artist as :" +artist);

            out.write("The method to get the video has been called".getBytes(StandardCharsets.UTF_8));
            out.flush();
        });


        // Upload video
        BrokerData.getInstance().getBrokerRouter().POST("/artist/videos", (socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            JsonObject jsObj = JsonParser.parseString(mrmtpHeader.getBody()).getAsJsonObject();

            // Get json values
            String artist = jsObj.get("artist").getAsString();
            String video = jsObj.get("video").getAsString();
            int FILE_SIZE = jsObj.get("fileSize").getAsInt();

            // Create a response header
            MRMTPHeader responseHeader = new MRMTPHeader();
            responseHeader.setDestination(socket.getRemoteSocketAddress().toString());
            responseHeader.setSource(BrokerData.getInstance().getServerNode().out());
            responseHeader.setKeepAlive(true);
            responseHeader.setConnection(200);
            responseHeader.setContentType("json");
            responseHeader.setMethodType(MethodConstants.GET);
            responseHeader.setMethod("/artist/video");
            responseHeader.setBody("{ \"bufferSize\": "+VIDEO_BUFFER+" }");
            // Send response
            out.write(MRMTPBuilder.getMRMTPBuffer(responseHeader), 0, 1024);

            // create directory
            FileReader.createDirectoryPath("./temp");

            String filepath = "./temp/"+artist+"-"+video+".mp4";
            // Get video from client
            FileTransfer.downloadFile(filepath, VIDEO_BUFFER, FILE_SIZE, socket);
            // Start a thread (TODO: make a process and communicate with IPC to call ffmpeg on the file)
            Runnable task = () -> {
                fragmentVideoFile(filepath, artist, video);
            };
            new Thread(task).start();

        });

        // Delete video
        BrokerData.getInstance().getBrokerRouter().DELETE("/artist/video", new MethodCall() {
            @Override
            public void call(Socket socket, MRMTPHeader mrmtpHeader, String s) throws IOException {
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                out.write("The method to delete a video has been called".getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        });

        // Delete video
        BrokerData.getInstance().getBrokerRouter().DELETE("/artist", new MethodCall() {
            @Override
            public void call(Socket socket, MRMTPHeader mrmtpHeader, String s) throws IOException {
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                out.write("The method to delete an artist has been called".getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        });
    }

    private static void fragmentVideoFile(String filepath, String artist, String video){
        File file = new File(filepath);
        // Fragmentation (upload)
        // Transfer some logic to the application server and some to the broker handler
        // TODO: make root a Broker id
        String videoDirectoryPath = "./root/"+artist+"/"+video;
        VideoDirectory videoDirectory = new VideoDirectory();
        videoDirectory.setUploaderID(artist);
        videoDirectory.setVideoPath(videoDirectoryPath);
        videoDirectory.setHits(0);
        videoDirectory.setId(video);
        MultimediaObject sourceObj = new MultimediaObject(file);
        try {
            videoDirectory.setFramerate(sourceObj.getInfo().getVideo().getFrameRate());
            videoDirectory.setHeight(sourceObj.getInfo().getVideo().getSize().getHeight());
            videoDirectory.setWidth(sourceObj.getInfo().getVideo().getSize().getWidth());
            videoDirectory.setLength(sourceObj.getInfo().getDuration());
        } catch (EncoderException e) {
            e.printStackTrace();
            return ;
        }
        int chunks = (int) ( videoDirectory.getLength() / (1000 * 10)) + (int) (videoDirectory.getLength() % (1000 * 10.0) != 0? 1 : 0);
        videoDirectory.setChunks(chunks);

        boolean created = videoController.createVideoDirectory(file, videoDirectory);

        if(created){
            System.out.println("Fragmentation successful.");
        }else{
            System.out.println("Fragmentation failed.");
        }
    }

}
