package com.wahwahnow.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "broker_video")
public class BrokerVideo implements Serializable {

    @Id
    private String brokerAddress;
    @Id
    private String videoID;
    private int streamable;

    public BrokerVideo(){ }

    public BrokerVideo(String brokerAddress, String videoID) {
        this.brokerAddress = brokerAddress;
        this.videoID = videoID;
        streamable = 0;
    }

    public BrokerVideo(String brokerAddress, String videoID, int streamable) {
        this.brokerAddress = brokerAddress;
        this.videoID = videoID;
        this.streamable = streamable;
    }

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public int getStreamable() {
        return streamable;
    }

    public void setStreamable(int streamable) {
        this.streamable = streamable;
    }
}
