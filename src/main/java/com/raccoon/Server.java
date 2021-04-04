package com.raccoon;

import com.raccoon.handlers.MainServerHandler;
import com.raccoon.io.JsonHandler;
import com.raccoon.loggers.SystemLogger;
import com.raccoon.models.ServerNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable{

    ServerNode serverInfo;
    ServerSocket serverSocket;

    public Server(ServerNode serverInfo){
        this.serverInfo = serverInfo;
    }

    public void run(){
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
                new Thread(new MainServerHandler(newConnection)).start();
            } catch (IOException e) {
                SystemLogger.Log("E: Failed to establish a new connection");
                break;
            }
        }
    }


    public static void main(String[] args){
        System.out.print("Enter number of server config > ");
        Scanner in = new Scanner(System.in);
        String num = in.nextLine();
        int conf_num = 0;
        try{
            if(!num.isBlank()) conf_num = Integer.parseInt(num);
        }catch (Exception e){
            SystemLogger.Log("Didn't find number");
        }
        String filepath = Thread.currentThread().getContextClassLoader().getResource("config"+
                        (num.isBlank()? "" : conf_num)
                +".json").getPath();
        if(filepath == null) System.exit(-1);
        ServerNode serverNode = (ServerNode) JsonHandler.readJson(filepath, new ServerNode());
        assert serverNode != null;
        new Thread(new Server(serverNode)).start();
    }

}
