package com.wahwahnow.broker.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wahwahnow.broker.BrokerData;
import com.wahwahnow.broker.io.VideoFiles;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;
import org.mrmtp.rpc.methods.MethodConstants;
import org.mrmtp.rpc.router.MethodCall;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class VideoRoutes {

    private static Gson gson = new Gson();
    private static int VIDEO_BUFFER = 1048576;

    public static void init(){
        // Get artist video (streaming)
        BrokerData.getInstance().getBrokerRouter().GET("/artist/video", new MethodCall() {
            @Override
            public void call(Socket socket, MRMTPHeader mrmtpHeader, String s) throws IOException {
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                String body = mrmtpHeader.getBody();
                JsonObject jsObj = JsonParser.parseString(body).getAsJsonObject();

                String artist = jsObj.get("artist").getAsString();

                System.out.println("We got artist as :" +artist);

                out.write("The method to get the video has been called".getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        });

        // Upload video
        BrokerData.getInstance().getBrokerRouter().POST("/artist/videos", new MethodCall() {
            @Override
            public void call(Socket socket, MRMTPHeader mrmtpHeader, String s) throws IOException {
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                MRMTPHeader responseHeader = new MRMTPHeader();
                responseHeader.setDestination(socket.getRemoteSocketAddress().toString());
                responseHeader.setSource(BrokerData.getInstance().getServerNode().out());
                responseHeader.setKeepAlive(true);
                responseHeader.setConnection(200);
                responseHeader.setContentType("json");
                responseHeader.setMethodType(MethodConstants.GET);
                responseHeader.setMethod("/artist/video");
                responseHeader.setBody(
                        """
                            {
                                "chunkSize": 1048576
                            }
                        """
                );

                byte[] header_buffer = new byte[8192];
                byte[] headerProduct = MRMTPBuilder.get(responseHeader).getBytes(StandardCharsets.UTF_8);
                System.arraycopy(
                        headerProduct,
                        0,
                        header_buffer,
                        0,
                        headerProduct.length
                );

                out.write(header_buffer);
                out.flush();

                in.read(header_buffer, 0, 8192);
                MRMTPHeader inter = MRMTPParser.parse(new String(header_buffer, StandardCharsets.UTF_8));

                String body = inter.getBody();
                System.out.println("Client send "+body);
                JsonObject jsObj = JsonParser.parseString(body).getAsJsonObject();

                String artistHash = jsObj.get("artist").getAsString();
                String videoHash = jsObj.get("video").getAsString();
                int chunks = jsObj.get("chunks").getAsInt();
                int fileSize = jsObj.get("fileSize").getAsInt();
                for(int i = 0; i < chunks; i++){
                    int len = (i == chunks - 1 && fileSize % VIDEO_BUFFER != 0)
                            ? fileSize % VIDEO_BUFFER
                            : VIDEO_BUFFER;
                    byte[] buffer = new byte[VIDEO_BUFFER];
                    in.read(buffer, 0, len);
                    try{
                        VideoFiles.storeData(buffer, i, videoHash, artistHash);
                    }catch (IOException failedToWrite){
                        System.out.println("Failed to write file. Execute in another thread a video file deletion.");
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                responseHeader.setBody(
                        """
                            {
                                "uploaded": true,
                            }
                        """
                );

                byte[] headerProduct2 = MRMTPBuilder.get(responseHeader).getBytes(StandardCharsets.UTF_8);
                byte[] header_buffer2 = new byte[8192];
                System.arraycopy(
                        headerProduct2,
                        0,
                        header_buffer2,
                        0,
                        headerProduct2.length
                );
                System.out.println("Sending status to user");
                out.write(header_buffer2);
                out.flush();
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

}
