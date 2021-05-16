package com.wahwahnow.models;

import javax.persistence.*;
import java.io.Serializable;

@Table
@Entity(name = "channel_profile_image")
public class ChannelProfileImage implements Serializable {

    @Id
    @Column(name = "channel_id", nullable = false)
    private String channelID;
    @Column(name = "data", nullable = false)
    @Lob
    private byte[] data;

    public ChannelProfileImage(){}

    public ChannelProfileImage(String channelID, String base64) {
        this.channelID = channelID;
        this.data = data;
    }


    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public byte[] getBase64() {
        return data;
    }

    public void setBase64(byte[] data) {
        this.data = data;
    }
}
