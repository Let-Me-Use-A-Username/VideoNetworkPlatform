package com.raccoon.models;

public class PostModels {




    class PostLogin {

        String userID;
        String password;
        String sessionCookie;

    }

    class Register {

        String userID;
        String password;

    }

    class PostVideo {

        String userID;
        String sessionCookie;
        VideoModel video;


    }

    class PostVideoData {

        String userID;
        String sessionCookie;
        String videoID;
        byte[] chunk;
        int chunkOrder;

    }

}
