package com.wahwahnow.models;

public class ChannelAuth {

    private String userID;
    private String password;
    private String hashFunction;

    public ChannelAuth(){ }

    public ChannelAuth(String userID, String password, String hashFunction){
        this.userID = userID;
        this.password = password;
        this.hashFunction = hashFunction;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashFunction() {
        return hashFunction;
    }

    public void setHashFunction(String hashFunction) {
        this.hashFunction = hashFunction;
    }
}
