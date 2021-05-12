package com.wahwahnow;

public class ConfigData {

    private static final ConfigData instance = new ConfigData();

    private ConfigData(){ }

    public static synchronized ConfigData getInstance(){
        return instance;
    }

    private String secret;

    public void setSecret(String secret){
        this.secret = secret;
    }

    public String getSecret(){
        return secret;
    }

}
