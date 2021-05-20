package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "video")
public class Video implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "TEXT")
    private String id;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(nullable = false, columnDefinition = "INTEGER")
    private long timestamp;
    private String description;
    private int likes;
    private int views;
    @Column(name = "channel_id", nullable = false, columnDefinition = "TEXT")
    private String channelID;
    @Column(name = "channel_name", nullable = false)
    private String channelName;
    private int commentNum;

    public Video(){}

    public Video(String id, String name, long timestamp, String description, int likes, int views, String channelID, String channelName, int commentNum) {
        this.id = id;
        this.likes = likes;
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.views = views;
        this.channelID = channelID;
        this.channelName = channelName;
        this.commentNum = commentNum;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
