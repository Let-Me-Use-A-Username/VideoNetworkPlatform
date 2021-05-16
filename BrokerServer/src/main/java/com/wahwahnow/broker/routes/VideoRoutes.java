package com.wahwahnow.broker.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wahwahnow.broker.BrokerData;
import com.wahwahnow.broker.controllers.BrokerFetch;
import com.wahwahnow.broker.controllers.DatabaseController;
import com.wahwahnow.broker.controllers.VideoController;
import com.wahwahnow.broker.database.Queries;
import com.wahwahnow.broker.io.VideoFiles;
import com.wahwahnow.broker.models.FragmentModel;
import com.wahwahnow.broker.models.VideoDirectory;
import org.mrmtp.rpc.Util;
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
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class VideoRoutes {

    private static Gson gson = new Gson();
    private static VideoController videoController = new VideoController();
    private static int VIDEO_BUFFER = 65536;
    private static int HEADER_BUFFER_SIZE = 2048;

    public static void init(){

        // Get artist video 1135267
        BrokerData.getInstance().getBrokerRouter().GET("/artist/video", (socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            JsonObject jsObj = JsonParser.parseString(mrmtpHeader.getBody()).getAsJsonObject();
            String artistHash = jsObj.get("artist").getAsString();
            String videoHash = jsObj.get("video").getAsString();

            String videoDirectory = "./"+BrokerData.getInstance().getBrokerID()+"/"+artistHash+"/"+videoHash;
            // get total fragment files from the video directory
            int totalFragments = org.mrmtp.rpc.filetransfer.FileReader.getTotalDirectoryFiles(videoDirectory);
            // get the id of the last fragment
            int lastFragmentID = com.wahwahnow.broker.io.FileReader.getLastFragmentID(videoDirectory);

            // Create a response header
            MRMTPHeader responseHeader = new MRMTPHeader();
            responseHeader.setDestination(socket.getRemoteSocketAddress().toString());
            responseHeader.setSource(BrokerData.getInstance().getServerNode().out());
            responseHeader.setKeepAlive(true);
            responseHeader.setConnection(200);
            responseHeader.setContentType("json");
            responseHeader.setMethodType(MethodConstants.GET);
            responseHeader.setMethod("/artist/video");
            responseHeader.setBody("{ \"totalChunks\": "+totalFragments+", \"lastFragmentID\": "+lastFragmentID+" }");

            out.write(MRMTPBuilder.getMRMTPBuffer(responseHeader, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            out.close();
            in.close();
            socket.close();

        });

        // Send video buffers
        BrokerData.getInstance().getBrokerRouter().GET("/artist/video/fragments", ((socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            String body = mrmtpHeader.getBody();
            JsonObject jsObj = JsonParser.parseString(body).getAsJsonObject();

            boolean hasCurrentSeek = jsObj.has("currentSeek");
            // if client send buffering: true as a value send 6 chunks
            boolean buffering = jsObj.get("buffering").getAsBoolean();
            boolean hasFragments = jsObj.has("fragments");
            String artist = jsObj.get("artist").getAsString();
            String video  = jsObj.get("video").getAsString();

            // Create a response header
            MRMTPHeader responseHeader = new MRMTPHeader();
            responseHeader.setDestination(socket.getRemoteSocketAddress().toString());
            responseHeader.setSource(BrokerData.getInstance().getServerNode().out());
            responseHeader.setKeepAlive(true);
            responseHeader.setConnection(200);
            responseHeader.setContentType("json");
            responseHeader.setMethodType(MethodConstants.GET);
            responseHeader.setMethod("/artist/video/fragments");
            ArrayList<String> totalMissingFragments = new ArrayList<>();

            // send 6 (or all if less than 6) fragments
            if(buffering && !hasFragments){
                totalMissingFragments = getFragmentNumberToStillGet(null, artist, video);
                responseHeader.setBody(
                        getVideoFragmentsJson(totalMissingFragments, artist, video, 6, VIDEO_BUFFER)
                );
            }
            // check if we should send 3 more
            else if(hasFragments && hasCurrentSeek){
                int currentSeek = jsObj.get("currentSeek").getAsInt();
                Type listType = new TypeToken<List<FragmentModel>>(){}.getType();
                List<FragmentModel> fragments = gson.fromJson(jsObj.get("fragments"), listType);
                if(sendNext(currentSeek, fragments)) {
                    totalMissingFragments = getFragmentNumberToStillGet(fragments, artist, video);
                    responseHeader.setBody(
                            getVideoFragmentsJson(totalMissingFragments, artist, video, 3, VIDEO_BUFFER)
                    );
                }
            } else {
                responseHeader.setBody("");
            }

            responseHeader.setContentLength(responseHeader.getBody().length());
            out.write(MRMTPBuilder.getMRMTPBuffer(responseHeader, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            out.close();
            in.close();
            socket.close();

        }));

        // Send video fragment
        BrokerData.getInstance().getBrokerRouter().GET("/artist/video/fragment/mp4", ((socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            String body = mrmtpHeader.getBody();
            JsonObject jsObj = JsonParser.parseString(body).getAsJsonObject();

            String artist = jsObj.get("artist").getAsString();
            String video  = jsObj.get("video").getAsString();
            int fragmentID = jsObj.get("fragmentID").getAsInt();

            String filepath = "./"+BrokerData.getInstance().getBrokerID()+"/" + artist + "/" + video + "/" + fragmentID +"_" + (fragmentID+10) + ".mp4";

            if(com.wahwahnow.broker.io.FileReader.exists(filepath)){
                int fileSize = (int) FileReader.getFileSize(filepath);
                FileTransfer.uploadFile(filepath, VIDEO_BUFFER, fileSize, socket);
            }

            out.close();
            in.close();
            socket.close();
        }));

        // Upload video
        BrokerData.getInstance().getBrokerRouter().POST("/artist/video", (socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            JsonObject jsObj = JsonParser.parseString(mrmtpHeader.getBody()).getAsJsonObject();

            // Get json values
            String artist = jsObj.get("artist").getAsString();
            String video = jsObj.get("video").getAsString();
            int FILE_SIZE = jsObj.get("fileSize").getAsInt();

            // check if we have it on the database
            List<VideoDirectory> videoList = BrokerData.getInstance().getDB().getVideos(Queries.getVideo(video));

            // Create a response header
            MRMTPHeader responseHeader = new MRMTPHeader();
            responseHeader.setDestination(socket.getRemoteSocketAddress().toString());
            responseHeader.setSource(BrokerData.getInstance().getServerNode().out());
            responseHeader.setKeepAlive(true);


            if(videoList.size() > 0){
                responseHeader.setConnection(200);
                responseHeader.setContentType("json");
                responseHeader.setMethodType(MethodConstants.POST);
                responseHeader.setMethod("/artist/video");
                responseHeader.setBody("{ \"bufferSize\": "+VIDEO_BUFFER+" }");

                // Send response
                out.write(MRMTPBuilder.getMRMTPBuffer(responseHeader, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

                // create directory
                FileReader.createDirectoryPath("./"+BrokerData.getInstance().getBrokerID()+"/temp");

                String filepath = "./"+BrokerData.getInstance().getBrokerID()+"/temp/"+artist+"-"+video+".mp4";
                // Get video from client
                FileTransfer.downloadFile(filepath, VIDEO_BUFFER, FILE_SIZE, socket);
                // Start a thread (TODO: make a process and communicate with IPC to call ffmpeg on the file)
                new Thread(()->{
                    fragmentVideoFile(filepath, artist, video);
                }).start();

            } else {
                responseHeader.setConnection(404);
                responseHeader.setContentType("json");
                responseHeader.setMethodType(MethodConstants.POST);
                responseHeader.setMethod("/artist/video");

                // Send response
                out.write(MRMTPBuilder.getMRMTPBuffer(responseHeader, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);
            }

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

        // Delete artist
        BrokerData.getInstance().getBrokerRouter().DELETE("/artist", (socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write("The method to delete an artist has been called".getBytes(StandardCharsets.UTF_8));
            out.flush();
        });

        // Be notified by server
        BrokerData.getInstance().getBrokerRouter().POST("/notify/newVideo", ((socket, mrmtpHeader, s) -> {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            JsonObject jsObj = JsonParser.parseString(mrmtpHeader.getBody()).getAsJsonObject();

            String artistHash = jsObj.get("artist").getAsString();
            String videoHash = jsObj.get("video").getAsString();
            String brokerSource = jsObj.has("brokerSource")? jsObj.get("brokerSource").getAsString() : "";

            out.close();
            in.close();
            socket.close();

            System.out.println("I will work on artist: "+artistHash+"\t and video: "+videoHash);

            // Create directory
            FileReader.createDirectoryPath("./"+BrokerData.getInstance().getBrokerID()+"/"+artistHash+"/"+videoHash);
            // insert artist on sqlite
            BrokerData.getInstance().getDB().executeQuery(Queries.insertUploader(artistHash, "./"+BrokerData.getInstance().getBrokerID()+"/"+artistHash));
            // insert video on sqlite
            BrokerData.getInstance().getDB().executeQuery(Queries.insertVideo(artistHash, videoHash, "./"+BrokerData.getInstance().getBrokerID()+"/"+artistHash+"/"+videoHash));

            // if brokerSource is not empty string then we will fetch this from another broker
            if(!brokerSource.isBlank()){
                new Thread(()->{
                    String[] parts = brokerSource.split(":");
                    BrokerFetch.fetchVideo(parts[0], Integer.parseInt(parts[1]), videoHash);
                }).start();
            }

        }));

        BrokerData.getInstance().getBrokerRouter().GET("/checkAlive",  ((socket, mrmtpHeader, s) ->{
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            out.close();
            in.close();
            socket.close();
        }));

    }

    private static void fragmentVideoFile(String filepath, String artist, String video){
        File file = new File(filepath);
        // Fragmentation (upload)
        // Transfer some logic to the application server and some to the broker handler
        String videoDirectoryPath = "./"+BrokerData.getInstance().getBrokerID()+"/"+artist+"/"+video;
        VideoDirectory videoDirectory = new VideoDirectory();
        videoDirectory.setUploaderID(artist);
        videoDirectory.setVideoPath(videoDirectoryPath);
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

    // checks if we should fragments or not
    private static boolean sendNext(int currentSeek, List<FragmentModel> fragments){
        int currentSeekFragmentID = currentSeek / 10 * 10;
        int nextBuffer = 0;
        for(FragmentModel frag: fragments) nextBuffer += frag.getFragmentID() > currentSeekFragmentID? 1: 0;
        // if he has 3 or more on his buffer window front we dont send
        // else we send
        return nextBuffer <= 3;

    }

    // Build response header for the fragments
    private static String getVideoFragmentsJson(ArrayList<String> missingFragments, String artist, String video, int maxFragments, int BUFFER_SIZE){
        String directory = "./"+BrokerData.getInstance().getBrokerID()+"/"+artist+"/"+video+"/";
        // get total missing fragments (sorted)

        // Build body to send
        Map<String, Object> resBody = new HashMap<>();
        List<FragmentModel> fragmentList = new ArrayList<>();
        for(int i = 0; i < missingFragments.size() && i < maxFragments; i++){
            String fragmentID = missingFragments.get(i);
            int fragmentValue = Integer.parseInt(fragmentID.split("_")[0]);
            String filepath = directory + fragmentID;
            int fileSize = (int) FileReader.getFileSize(filepath);
            fragmentList.add(new FragmentModel(fragmentValue, fileSize));

        }
        resBody.put("bufferSize", BUFFER_SIZE);
        resBody.put("fragments", fragmentList);

        return gson.toJson(resBody);

    }

    // check directory if there more fragments to get and return how many he doesn't have
    private static ArrayList<String> getFragmentNumberToStillGet(List<FragmentModel> fragments, String artist, String video){
        String filepath = "./"+BrokerData.getInstance().getBrokerID()+"/"+artist+"/"+video;

        if(fragments == null) return com.wahwahnow.broker.io.FileReader.getRemainingFragments(filepath, -1);

        // Get last fragment ID
        int lastFragment = -1;
        for(FragmentModel frag: fragments){
            if(frag.getFragmentID() > lastFragment) lastFragment = frag.getFragmentID();
        }

        return com.wahwahnow.broker.io.FileReader.getRemainingFragments(filepath, lastFragment);
    }

}