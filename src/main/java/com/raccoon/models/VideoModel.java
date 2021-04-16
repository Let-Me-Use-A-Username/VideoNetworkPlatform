package com.raccoon.models;

import java.time.format.DateTimeFormatter;

public class VideoModel {

    String name;
    String id;
    String description;
    String[] tags;
    String uploader;
    float length;
    float frameRate;
    int height;
    int width;
    String timestamp;
    long views;

    public VideoModel(){}

    public VideoModel(String name, String id, String description, String[] tags, String uploader, float length, float frameRate, int height, int width, String timestamp, long views){
        this.name = name;
        this.id = id;
        this.description = description;
        this.tags = tags;
        this.uploader = uploader;
        this.length = length;
        this.frameRate = frameRate;
        this.height = height;
        this.width = width;
        this.timestamp = timestamp;
        this.views = views;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }


}
