package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Table
@Entity(name = "channel")
public class Channel implements Serializable {

    @Id
    private String id;
    @Column(nullable = false, name="channel_name", unique = true)
    private String channelName;
    private String about;
    private Integer videosUploaded;
    private Integer dateCreated; //timestamp
    private String userID;


    public Channel(String id, String channelName, String about, Integer videosUploaded, Integer dateCreated, String userID) {
        this.channelName = channelName;
        this.id = id;
        this.about = about;
        this.videosUploaded = videosUploaded;
        this.dateCreated = dateCreated;
        this.userID = userID;
    }

    public Channel(){}

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


    public Integer getVideosUploaded() {
        return videosUploaded;
    }

    public void setVideosUploaded(Integer videosUploaded) {
        this.videosUploaded = videosUploaded;
    }

    public Integer getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Integer dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
