package com.videomole;

import com.videomole.Models.VideoDirectory;

import java.util.HashMap;

// Eager initialization singleton
public class BrokerFileSystem {

    private static final BrokerFileSystem instance = new BrokerFileSystem();

    private BrokerFileSystem(){

    }

    synchronized public BrokerFileSystem getInstance(){
        return instance;
    }

    private HashMap<String, String> users; // KEY: {String: userID: user} -> Value: {String: userFolder}
    private HashMap<String, VideoDirectory[]> userVideos; // KEY: {String: userID: user} -> Value: {VideoModels}
    private HashMap<String, String> videos; // KEY: {String: videoID: video} -> Value: {String: videoPath}
    private long chunkMaxSize = 1048576;

    public long getChunkMaxSize() {
        return chunkMaxSize;
    }

    public void setChunkMaxSize(long chunkMaxSize) {
        this.chunkMaxSize = chunkMaxSize;
    }

}
