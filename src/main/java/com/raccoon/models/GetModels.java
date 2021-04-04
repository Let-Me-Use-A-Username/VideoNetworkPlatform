package com.raccoon.models;

public class GetModels {

    class GetVideos {

        String type; // "category", "latest", "user", "trends", "random"
        VideoModel[] videos;

    }

    class GetVideo {

        String videoID;
        int chunkOrder;
        byte[] chunk;

    }



}
