package com.raccoon.handlers;

import com.raccoon.loggers.SystemLogger;

import java.io.*;
import java.net.Socket;

public class MainServerHandler implements Runnable{

    Socket socket;

    PrintWriter out;
    BufferedReader in;
    boolean readyToReadPacket;

    public MainServerHandler(Socket socket){
        this.socket = socket;
        readyToReadPacket = false;
    }

    public void run(){
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            readyToReadPacket = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(readyToReadPacket) echo();
        try {
            if(out != null) out.close();
            if(in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void echo(){
        while(true) {
            try {
                String message = in.readLine();
                if(message == null) break;
                SystemLogger.Log("Client send:> "+message);
                out.println(message);
            } catch (IOException e) {
                break;
            }
        }
        SystemLogger.Log("Connection closed");
    }

}
