package com.raccoon.models;

public class VideoModel {

    String name;
    String id;
    String description;
    String[] tags;
    String uploader;
    int length;
    long views;

    public VideoModel(){}

    public VideoModel(String name, String id, String description, String[] tags, String uploader, int length, long views){
        this.name = name;
        this.id = id;
        this.description = description;
        this.tags = tags;
        this.uploader = uploader;
        this.length = length;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

}
