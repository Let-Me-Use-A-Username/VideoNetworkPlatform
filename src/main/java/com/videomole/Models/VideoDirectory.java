package com.videomole.Models;

import com.raccoon.models.VideoModel;

public class VideoDirectory {

    VideoModel videoDetails;
    int chunks;
    String videoPath;

    public VideoDirectory(){}

    public VideoDirectory(VideoModel videoDetails, int chunks, String videoPath) {
        this.videoDetails = videoDetails;
        this.chunks = chunks;
        this.videoPath = videoPath;
    }

    public VideoModel getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoModel videoDetails) {
        this.videoDetails = videoDetails;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }


}
