package com.raccoon.models;

public class ServerNode {

    private int port;
    private String address;

    public ServerNode(){ }

    public int getPort(){return port;}

    public void setPort(int port){this.port = port;}

    public String getAddress(){return address;}

    public void setAddress(String address){this.address = address;}

    public String out(){ return address + ":" + port ;}
}
