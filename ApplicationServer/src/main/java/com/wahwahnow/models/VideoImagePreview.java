package com.wahwahnow.models;

import javax.persistence.*;
import java.io.Serializable;

@Table
@Entity(name = "video_image_preview")
public class VideoImagePreview implements Serializable {

    @Id
    @Column(name = "video_id", columnDefinition = "TEXT")
    private String videoID;
    @Column(name = "data", columnDefinition = "BLOB")
    @Lob
    private byte[] data;

    public VideoImagePreview(){}

    public VideoImagePreview(String videoID, byte[] data) {
        this.data = data;
        this.videoID = videoID;
    }


    public byte[] getBase64() {
        return data;
    }

    public void setBase64(byte[] data) {
        this.data = data;
    }


    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
}
