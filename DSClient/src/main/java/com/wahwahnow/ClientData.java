package com.wahwahnow;

import java.net.URI;

public class ClientData {


    private static final ClientData instance = new ClientData();

    public static synchronized ClientData getInstance(){
        return instance;
    }

    private String jwt;
    private URI webApi;

    public synchronized void setJWT(String jwt){
        this.jwt = jwt;
    }

    public synchronized String getJwt(){
        return jwt;
    }

    public synchronized void setWebApi(String address){
        webApi = URI.create(address);
    }

    public synchronized URI getWebApi(){
        return webApi;
    }

}
