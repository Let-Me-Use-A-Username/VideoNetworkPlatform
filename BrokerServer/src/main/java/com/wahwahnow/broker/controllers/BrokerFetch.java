package com.wahwahnow.broker.controllers;

public class BrokerFetch {

    // try to fetch video for 1 hour (request every 5 minutes) if failure notify server that this broker doesn't have the video
    // if successful notify application server that video can be streamed from here (make it available for users)
    public static void fetchVideo(String brokerAddress, int port, String videoHash){
        System.out.println("We will fetch video from "+brokerAddress+":"+port+ " for video: "+videoHash);
    }

}
