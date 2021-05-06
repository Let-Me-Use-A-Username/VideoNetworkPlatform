package com.wahwahnow.broker;

import com.wahwahnow.broker.loggers.SystemLogger;
import org.mrmtp.rpc.Util;
import org.mrmtp.rpc.header.MRMTPHeader;
import org.mrmtp.rpc.header.MRMTPParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Handler implements Runnable{

    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private boolean openedStreams;
    private static int HEADER_BUFFER_SIZE = 2048;

    public Handler(Socket socket){
        this.socket = socket;
        openStreams();
    }

    private void openStreams(){
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            openedStreams = true;
        } catch (IOException ignore) {
            openedStreams = false;
        }
    }

    @Override
    public void run() {
        // process the header
        SystemLogger.Log("New connection from "+socket.getRemoteSocketAddress().toString());
        if(!openedStreams) return;
        try {
            StringBuilder sb = new StringBuilder();

            byte[] buffer = Util.getNewBuffer(null, 2048);;
            int bytesRead = in.read(buffer, 0, 2048);
            if(bytesRead == -1) return;

            MRMTPHeader header = MRMTPParser.parse(buffer);
            BrokerData.getInstance().getBrokerRouter().call(socket, header.getMethod(), header, "");

        } catch (IOException e) {
            SystemLogger.Log("Connection from "+socket.getInetAddress().getAddress().toString()+" closed.");
        }

    }

}
