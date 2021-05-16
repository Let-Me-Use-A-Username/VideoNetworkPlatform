package com.wahwahnow.broker;

import com.wahwahnow.broker.controllers.DatabaseController;
import com.wahwahnow.broker.models.ServerNode;
import com.wahwahnow.broker.models.VideoDirectory;
import org.mrmtp.rpc.router.Router;

import java.util.HashMap;

// Eager initialization singleton
public class BrokerData {

    private static final BrokerData instance = new BrokerData();

    private BrokerData(){
            brokerRouter = new Router();
        }

    public static synchronized BrokerData getInstance(){
        return instance;
    }

    private Router brokerRouter;
    private long chunkMaxSize = 1048576;
    private DatabaseController db;
    private ServerNode serverNode;
    private String brokerID;

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

    public synchronized void setDatabase(String filename, String osURL){
        db = new DatabaseController(filename, osURL);
    }

}
