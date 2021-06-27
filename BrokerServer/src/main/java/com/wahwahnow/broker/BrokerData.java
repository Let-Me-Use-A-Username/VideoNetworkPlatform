package com.wahwahnow.broker;

import com.wahwahnow.broker.controllers.DatabaseController;
import com.wahwahnow.broker.io.HttpRouter;
import com.wahwahnow.broker.models.ServerNode;
import org.mrmtp.rpc.router.Router;

import java.util.HashMap;

// Eager initialization singleton
public class BrokerData {

    private static final BrokerData instance = new BrokerData();

    private BrokerData(){
        httpRouter = new HttpRouter();
        brokerRouter = new Router();
        applicationAddress = "192.168.1.2:8080";
    }

    public static synchronized BrokerData getInstance(){
        return instance;
    }

    private Router brokerRouter;
    private long chunkMaxSize = 1048576;
    private DatabaseController db;
    private ServerNode serverNode;
    private String applicationAddress;
    private int nodeCopies;
    private String brokerID;
    private HttpRouter httpRouter;

    public synchronized void setNodeCopies(int nodeCopies){
        this.nodeCopies = nodeCopies;
    }

    public synchronized int getNodeCopies(){
        return nodeCopies;
    }

    public synchronized void setBrokerID(String brokerID){
        this.brokerID = brokerID;
    }

    public synchronized String getBrokerID(){
        return brokerID;
    }

    public synchronized Router getBrokerRouter(){
        return brokerRouter;
    }

    public ServerNode getServerNode(){
        return serverNode;
    }

    public void setServerNode(String address, int port){
        serverNode = new ServerNode(address, port);
    }

    public synchronized DatabaseController getDB(){
        return db;
    }

    public synchronized void setDatabase(String filepath){
        db = new DatabaseController(filepath);
    }

    public synchronized void setApplicationAddress(String address){
        applicationAddress = address;
    }

    public synchronized String getApplicationAddress(){
        return applicationAddress;
    }

    public synchronized HttpRouter getRouter(){
        return httpRouter;
    }

}
