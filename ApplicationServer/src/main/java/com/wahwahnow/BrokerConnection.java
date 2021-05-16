package com.wahwahnow;

import com.google.gson.Gson;
import com.wahwahnow.models.Broker;
import org.mrmtp.rpc.header.MRMTPBuilder;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.methods.MethodConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BrokerConnection {

    private static Gson gson = new Gson();
    private static int HEADER_BUFFER_SIZE = 2048;

    // Notify broker about topic
    // if there is a source the broker will get the files from the source broker(the broker who will be send to the client in order to upload the video)
    public static void notifyBrokerTopic(String artistHash, String videoHash, Broker broker, Broker source){
        System.out.println("I will notify about "+videoHash+" broker "+broker.getBrokerAddress());

        Socket socket = null;
        String[] parts = broker.getBrokerAddress().split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(broker.getBrokerAddress());
        header.setKeepAlive(true);
        header.setConnection(200);
        header.setContentType("json");
        header.setMethodType(MethodConstants.POST);
        header.setMethod("/notify/newVideo");
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("artist", artistHash);
        resBody.put("video", videoHash);
        if(source != null) resBody.put("brokerSource", broker.getBrokerAddress());
        header.setBody(gson.toJson(resBody));
        header.setContentLength(header.getBody().length());

        try{
            socket = new Socket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(MRMTPBuilder.getMRMTPBuffer(header, HEADER_BUFFER_SIZE), 0, HEADER_BUFFER_SIZE);

            out.close();
            in.close();
            socket.close();

        }catch (IOException e){ e.printStackTrace(); }

    }

    public static boolean checkAlive(Broker broker){
        Socket socket = null;
        String[] parts = broker.getBrokerAddress().split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        MRMTPHeader header = new MRMTPHeader();
        header.setDestination(broker.getBrokerAddress());
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
