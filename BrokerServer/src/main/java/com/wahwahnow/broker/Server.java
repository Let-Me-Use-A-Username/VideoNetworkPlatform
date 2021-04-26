package com.wahwahnow.broker;

import com.wahwahnow.broker.io.JsonHandler;
import com.wahwahnow.broker.loggers.SystemLogger;
import com.wahwahnow.broker.models.ServerNode;
import com.wahwahnow.broker.routes.VideoRoutes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    ServerNode serverInfo;
    ServerSocket serverSocket;

    public Server(ServerNode serverInfo){
        this.serverInfo = serverInfo;
    }

    public void run(){
        VideoRoutes.init();
        BrokerData.getInstance().setServerNode(serverInfo.getAddress(), serverInfo.getPort());
        if(openServer()) accept();
        else SystemLogger.Log("Couldn't open server at "+serverInfo.out());
    }

    private boolean openServer(){
        try{
            serverSocket = new ServerSocket(serverInfo.getPort());
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    private void accept(){
        SystemLogger.Log("Server is running at "+serverInfo.out());
        while(true){
            try {
                Socket newConnection = serverSocket.accept();
                new Thread(new Handler(newConnection)).start();
            } catch (IOException e) {
                SystemLogger.Log("E: Failed to establish a new connection");
                break;
            }
        }
    }


    public static void main(String[] args){
        String filepath = Thread.currentThread().getContextClassLoader().getResource("config.json").getPath();
        if(filepath == null) System.exit(-1);
        ServerNode serverNode = (ServerNode) JsonHandler.readJson(filepath, new ServerNode());
        assert serverNode != null;
        new Thread(new Server(serverNode)).start();
    }

}

