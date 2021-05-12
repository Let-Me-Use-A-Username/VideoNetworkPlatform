package com.wahwahnow.models;

public class Users {

    private String id;
    private String channelName;
    private String email;

    public Users(){ }

    public Users(String id, String channelName, String email) {
        this.id = id;
        this.channelName = channelName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
