package com.wahwahnow.broker.models;

public class ServerNode {

    private int port;
    private String address;
    private String brokerID;

    public ServerNode(){ }

    public ServerNode(String address, int port){
        this.address = address;
        this.port = port;
    }

    public ServerNode(String address, int port, String brokerID){
        this.address = address;
        this.port = port;
        this.brokerID = brokerID;
    }

    public int getPort(){return port;}

    public void setPort(int port){this.port = port;}

    public String getAddress(){return address;}

    public void setAddress(String address){this.address = address;}

    public String out(){ return address + ":" + port ;}

    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }
}
