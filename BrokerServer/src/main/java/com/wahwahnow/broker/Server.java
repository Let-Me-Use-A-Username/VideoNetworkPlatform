package com.wahwahnow.broker;

import com.wahwahnow.broker.io.HttpRouter;
import com.wahwahnow.broker.io.JsonHandler;
import com.wahwahnow.broker.loggers.SystemLogger;
import com.wahwahnow.broker.models.HttpJsonModel;
import com.wahwahnow.broker.models.HttpResponseData;
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
        BrokerData.getInstance().setBrokerID(serverInfo.getBrokerID());
        BrokerData.getInstance().setApplicationAddress(serverInfo.getApplicationServer());
        BrokerData.getInstance().setServerNode(serverInfo.getAddress(), serverInfo.getPort());
        BrokerData.getInstance().setNodeCopies(serverInfo.getNodeCopies());
        BrokerData.getInstance().setDatabase(serverInfo.getSqlite());
        BrokerData.getInstance().getDB().createNewDatabase();
        BrokerData.getInstance().getDB().initializeBrokerTables();
        if(openServer()) {
            new Thread(this::notifyAlive).start();
            accept();
        }
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

    private void notifyAlive() {
        while (true){
            try{
                HttpResponseData res = BrokerData.getInstance().getRouter().sendRequest(
                        HttpRouter.POST,
                        "http://"+BrokerData.getInstance().getApplicationAddress()+"/brokers/notify",
                        null,
                        HttpJsonModel.contentType(),
                        HttpJsonModel.postNotify(
                                BrokerData.getInstance().getServerNode().getAddress(),
                                BrokerData.getInstance().getServerNode().getPort(),
                                BrokerData.getInstance().getNodeCopies()
                        )
                );
                System.out.println(res.content +" "+res.statusCode);
            }catch (IOException  e){}
            // sleep and continue after 5 minutes
            try{
                Thread.sleep(5 * 60 * 1000);
            }catch (InterruptedException e){ }
        }
    }


    public static void main(String[] args){
        String filepath = Thread.currentThread().getContextClassLoader().getResource("config3.json").getPath();
        if(filepath == null) System.exit(-1);
        ServerNode serverNode = (ServerNode) JsonHandler.readJson(filepath, new ServerNode());
        assert serverNode != null;
        new Thread(new Server(serverNode)).start();
    }

}

