package com.videomole;

import com.videomole.Models.VideoData;

import java.util.HashMap;

public class BrokerFileSystem {

    HashMap<String, String> users; // KEY: {String: userID: user} -> Value: {String: userFolder}
    HashMap<String, VideoData[]> userVideos; // KEY: {String: userID: user} -> Value: {VideoModels}
    HashMap<String, String> videos; // KEY: {String: videoID: video} -> Value: {String: videoPath}




}
