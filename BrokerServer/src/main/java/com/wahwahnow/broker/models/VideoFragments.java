package com.wahwahnow.broker.models;

import java.util.HashMap;

public class VideoFragments {

    private int currentSeek;
    private boolean paused;
    private int chunks;
    private int lastFragmentID;
    private HashMap<Integer, Byte[]> chunkArray;

    public VideoFragments(){
        currentSeek = 0;
        paused = false;
        chunks = 0;
        chunkArray = new HashMap<>();
    }

    public int getCurrentSeek(){
        return currentSeek;
    }

    public void setCurrentSeek(int currentSeek) {
        this.currentSeek = currentSeek;
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

    public Byte[] getFragment(int i){
        return chunkArray.get(i);
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

    public Byte[] getNextFragment(){
        if(currentSeek == 0) return chunkArray.get(0);
        int next = currentSeek / 10 * 10 + 10;
        if(next > lastFragmentID) return null;
        return chunkArray.get(next);
    }


}
