package com.wahwahnow.broker.models;

import java.util.HashMap;

public class VideoFragments {

    private int currentSeek;
    private boolean paused;
    private int chunks;
    private int lastFragmentID;
    private long lastContactTimestamp;
    private HashMap<Integer, byte[]> chunkArray;

    public VideoFragments(){
        currentSeek = 0;
        paused = false;
        chunks = 0;
        lastContactTimestamp = 0;
        chunkArray = new HashMap<>();
    }

    public int getCurrentSeek(){
        return currentSeek;
    }

    public void setCurrentSeek(int currentSeek) {
        this.currentSeek = currentSeek;
    }

    public void setLastContactTimestamp(long timestamp){
        lastContactTimestamp = timestamp;
    }

    public long getLastContactTimestamp(){
        return lastContactTimestamp;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public Integer[] getChunkNumbers(){
        return chunkArray.keySet().toArray(new Integer[0]);
    }

    public byte[] getFragment(int i){
        return chunkArray.get(i);
    }

    public void put(int fragmentID, byte[] data){
        chunkArray.put(fragmentID, data);
    }

    public int getLastFragmentID(){
        return lastFragmentID;
    }

    public void setLastFragmentID(int lastFragmentID){
        this.lastFragmentID = lastFragmentID;
    }

    public int getNextID(){
        if(currentSeek == 0) return 0;
        int next = currentSeek / 10 * 10 + 10;
        return next > lastFragmentID? -1 : next;
    }

    public byte[] getNextFragment(){
        if(currentSeek == 0) return chunkArray.get(0);
        int next = currentSeek / 10 * 10 + 10;
        if(next > lastFragmentID) return null;
        return chunkArray.get(next);
    }

    public boolean hasFragments(){
        return chunkArray.values().size() != 0;
    }

    public boolean isLastFragment(){
        return currentSeek / 10 * 10 == lastFragmentID;
    }


}
