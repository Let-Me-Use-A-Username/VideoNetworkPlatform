package com.wahwahnow.broker;

import com.wahwahnow.broker.controllers.DatabaseController;
import com.wahwahnow.broker.models.ServerNode;
import com.wahwahnow.broker.models.VideoDirectory;
import org.mrmtp.rpc.router.Router;

import java.util.HashMap;

// Eager initialization singleton
public class BrokerData {

        private static final com.wahwahnow.broker.BrokerData instance = new com.wahwahnow.broker.BrokerData();

        private BrokerData(){
            brokerRouter = new Router();
        }

    public static synchronized BrokerData getInstance(){
        return instance;
    }

    private Router brokerRouter;
    private HashMap<String, String> users; // KEY: {String: userID: user} -> Value: {String: userFolder}
    private HashMap<String, VideoDirectory[]> userVideos; // KEY: {String: userID: user} -> Value: {VideoModels}
    private HashMap<String, String> videos; // KEY: {String: videoID: video} -> Value: {String: videoPath}
    private long chunkMaxSize = 1048576;
    private DatabaseController db;
    private ServerNode serverNode;

    public synchronized Router getBrokerRouter(){
        return brokerRouter;
    }

    public long getChunkMaxSize() {
        return chunkMaxSize;
    }

    public void setChunkMaxSize(long chunkMaxSize) {
        this.chunkMaxSize = chunkMaxSize;
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
