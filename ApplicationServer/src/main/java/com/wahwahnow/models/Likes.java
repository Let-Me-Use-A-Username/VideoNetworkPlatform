package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "likes")
public class Likes implements Serializable {

    @Id
    @Column(name = "video_id", columnDefinition = "TEXT")
    private String video_id;
    @Id
    @Column(name = "channel_id", columnDefinition = "TEXT")
    private String channel_id;

    public Likes(){}

    public Likes(String video_id, String channel_id) {
        this.video_id = video_id;
        this.channel_id = channel_id;
    }


    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String videoID) {
        this.video_id = videoID;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channelID) {
        this.channel_id = channelID;
    }
}
