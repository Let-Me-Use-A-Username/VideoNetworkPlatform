package com.videomole;

import com.videomole.Models.VideoDirectory;
import com.videomole.controllers.DatabaseController;

import java.util.HashMap;

// Eager initialization singleton
public class BrokerFileSystem {

    private static final BrokerFileSystem instance = new BrokerFileSystem();

    private BrokerFileSystem(){

    }

    public static synchronized BrokerFileSystem getInstance(){
        return instance;
    }

    private HashMap<String, String> users; // KEY: {String: userID: user} -> Value: {String: userFolder}
    private HashMap<String, VideoDirectory[]> userVideos; // KEY: {String: userID: user} -> Value: {VideoModels}
    private HashMap<String, String> videos; // KEY: {String: videoID: video} -> Value: {String: videoPath}
    private long chunkMaxSize = 1048576;
    private DatabaseController db;

    public long getChunkMaxSize() {
        return chunkMaxSize;
    }

    public void setChunkMaxSize(long chunkMaxSize) {
        this.chunkMaxSize = chunkMaxSize;
    }

    public synchronized DatabaseController getDB(){
        return db;
    }

    public synchronized void setDatabase(String filename, String osURL){
        db = new DatabaseController(filename, osURL);
    }

}
