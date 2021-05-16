package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "comments")
public class Comments implements Serializable {

    @Id
    @Column(name = "video_id", columnDefinition = "TEXT")
    private String videoID;
    @Id
    @Column(name = "channel_id", columnDefinition = "TEXT")
    private String channelID;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;
    @Id
    private Integer timestamp;

    public Comments(){}

    public Comments(String videoID, String channelID, String comment, Integer timestamp) {
        this.videoID = videoID;
        this.channelID = channelID;
        this.comment = comment;
        this.timestamp = timestamp;
    }


    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
