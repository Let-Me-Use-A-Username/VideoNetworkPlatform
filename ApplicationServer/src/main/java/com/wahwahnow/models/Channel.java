package com.wahwahnow.models;

import java.util.ArrayList;

public class Channel {

    private String channelName;
    private String id;
    private String about;
    private ArrayList<String> hashtagsIDs;
    private int videosUploaded;
    private String dateCreated; //timestamp

    public Channel(String id, String channelName, String about, ArrayList<String> hashtagsIDs, int videosUploaded, String dateCreated) {
        this.channelName = channelName;
        this.id = id;
        this.about = about;
        this.hashtagsIDs = hashtagsIDs;
        this.videosUploaded = videosUploaded;
        this.dateCreated = dateCreated;
    }

    private void Channel(){}

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<String> getHashtagsIDs() {
        return hashtagsIDs;
    }

    public void setHashtagsIDs(ArrayList<String> hashtagsIDs) {
        this.hashtagsIDs = hashtagsIDs;
    }

    public int getVideosUploaded() {
        return videosUploaded;
    }

    public void setVideosUploaded(int videosUploaded) {
        this.videosUploaded = videosUploaded;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
