package com.wahwahnow.models;

import javax.persistence.*;
import java.io.Serializable;

@Table
@Entity(name = "video_image_preview")
public class VideoImagePreview implements Serializable {

    @Id
    @Column(name = "video_id", columnDefinition = "TEXT")
    private String videoID;
    @Column(name = "base64", columnDefinition = "TEXT")
    private String base64;

    public VideoImagePreview(){}

    public VideoImagePreview(String videoID, String base64) {
        this.videoID = videoID;
        this.base64 = base64;
    }


    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
