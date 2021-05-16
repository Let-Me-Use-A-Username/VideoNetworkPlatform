package com.wahwahnow;

public class JsonModels {

    public static String contentType = "application/json";

    public static String loginJSON(String email, String password){
        return "{ \"email\": \""+email+"\", \"password\": \""+password +"\" }";
    }

    public static String registerJSON(String channelName, String email, String password){
        return "{ \"channelName\": \"" +channelName+"\" ,\"email\": \""+email+"\", \"password\": \""+password +"\" }";

    }

    public static String uploadVideoJSON(String videoName, int tagID, String description){
        return "{ \"videoName\": \""+videoName+"\", \"tagID\": "+tagID+", \"description\": \""+description+"\" }";
    }
}
