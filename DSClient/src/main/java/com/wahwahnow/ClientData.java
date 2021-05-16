package com.wahwahnow;

import java.net.URI;
import java.util.*;

public class ClientData {


    private static final ClientData instance = new ClientData();

    public static synchronized ClientData getInstance(){
        return instance;
    }

    private ClientData()
    {
        jwt = "";
        videoTagList = new ArrayList<>();
    }

    private String jwt;
    private URI webApi;
    private TreeMap<Integer, String> tags;
    private List<VideoTag> videoTagList;

    public synchronized void setJWT(String jwt){
        this.jwt = jwt;
    }

    public synchronized String getJwt(){
        return jwt;
    }

    public synchronized HashMap<String, String> getJwtHeader(){
        HashMap<String, String> map = new HashMap<>();
        map.put("jwt", getJwt());
        return map;
    }

    public synchronized void setTags(Map<Integer, String> tags){
        this.tags = new TreeMap<>(tags);
    }

    public synchronized TreeMap<Integer, String> getTags(){
        return tags;
    }

    public synchronized void setWebApi(String address){
        System.out.println("Address is "+address+" test");
        webApi = URI.create("http://"+address);
    }

    public synchronized URI getWebApi(){
        return webApi;
    }

    public synchronized List<VideoTag> getVideoTags(){
        return videoTagList;
    }

    public synchronized void setVideoTags(List<VideoTag> videoTagList){
        this.videoTagList = videoTagList;
    }

}
