package com.wahwahnow.databases;

import java.util.ArrayList;

public class InsertQueries {

    public static String insertChannel(int id, String email, String about, ArrayList<String> hashtags, int videosUploaded, String dateCreated ){

        return "INSERT INTO channels (id, email, about, hashtags, videosUploaded, dateCreated) \n"
                + "VALUES (" + id + ", "+ email + ", "+ about + ", "+ hashtags + ", "+ videosUploaded+ ", "+dateCreated+");";
    }

    public static String insertUser(String email, int id, String password, String name){

        return "INSERT INTO channels (email, id, password, name) \n"
                + "VALUES (" + email + ", "+ id + ", "+ password + ", "+ name +");";
    }

    public static String insertUserProfileImage(int videoPreviewID, String imageBase64){

        return "INSERT INTO channels (videoPreviewID, imageBase64) \n"
                + "VALUES (" + videoPreviewID + ", "+ imageBase64 +");";
    }

    public static String insertVideoImagePreview(int videoPreviewID, String imageBase64, String password, String name){

        return "INSERT INTO channels (imageBase64, password, name) \n"
                + "VALUES (" + ", "+ imageBase64 + ", "+ password + ", "+ name + ");";
    }

    public static String insertVideos(int videoID, int views, int likes, int commentsNum){

        return "INSERT INTO channels (id, email, about, hashtags, videosUploaded, dateCreated) \n"
                + "VALUES (" + videoID + ", "+ views + ", "+ likes + ", "+ commentsNum+");";
    }

    public static String insertLike(int id){

        return "INSERT INTO channels (id) \n"
                + "VALUES (" + id+");";
    }

    public static String insertComment(String comment, String timestamp){

        return "INSERT INTO channels (comment, timestamp) \n"
                + "VALUES (" + comment + ", "+ timestamp +");";
    }

    public static String insertHashtag(int hashtagID, String name){

        return "INSERT INTO channels (id, email, about, hashtags, videosUploaded, dateCreated) \n"
                + "VALUES (" + hashtagID + ", "+ name+");";
    }


}
