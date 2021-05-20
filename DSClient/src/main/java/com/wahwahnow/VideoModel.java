package com.wahwahnow;

public class VideoModel {

    public String videoName;
    public String channelName;
    public String videoId;

    public VideoModel(String videoName, String channelName, String videoId) {
        this.videoName = videoName;
        this.channelName = channelName;
        this.videoId = videoId;
    }

    public void out(){
        System.out.println(" "+videoName+" - "+channelName +  "\tvid: "+videoId);
    }
}
