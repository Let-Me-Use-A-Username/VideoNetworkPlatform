package com.wahwahnow.broker.models;

public class VideoDirectory {

    private String id;
    private String uploaderID;
    private int chunks;
    private String videoPath;
    private int height;
    private int width;
    private float length;
    private float framerate;
    private long hits;

    public VideoDirectory(){}

    public VideoDirectory(String id, String uploaderID, int chunks, String videoPath, int height, int width, float length, float framerate, long hits) {
        this.id = id;
        this.uploaderID = uploaderID;
        this.chunks = chunks;
        this.videoPath = videoPath;
        this.height = height;
        this.width = width;
        this.length = length;
        this.framerate = framerate;
        this.hits = hits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getFramerate() {
        return framerate;
    }

    public void setFramerate(float framerate) {
        this.framerate = framerate;
    }

    public long getHits(){
        return hits;
    }

    public void setHits(long hits){
        this.hits = hits;
    }


}
