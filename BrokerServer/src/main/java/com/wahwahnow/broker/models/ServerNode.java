package com.wahwahnow.broker.models;

public class ServerNode {

    private int port;
    private String address;
    private String brokerID;
    private String applicationServer;
    private int nodeCopies;
    private String sqlite;

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

    public ServerNode(String address, int port, String brokerID, String applicationServer){
        this.address = address;
        this.port = port;
        this.brokerID = brokerID;
        this.applicationServer = applicationServer;
    }

    public ServerNode(String address, int port, String brokerID, String applicationServer, int nodeCopies){
        this.address = address;
        this.port = port;
        this.brokerID = brokerID;
        this.applicationServer = applicationServer;
        this.nodeCopies = nodeCopies;
    }

    public ServerNode(String address, int port, String brokerID, String applicationServer, int nodeCopies, String sqlite){
        this.address = address;
        this.port = port;
        this.brokerID = brokerID;
        this.applicationServer = applicationServer;
        this.nodeCopies = nodeCopies;
        this.sqlite = sqlite;
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

    public String getApplicationServer() {
        return applicationServer;
    }

    public void setApplicationServer(String applicationServer) {
        this.applicationServer = applicationServer;
    }

    public void setNodeCopies(int nodeCopies){
        this.nodeCopies = nodeCopies;
    }

    public int getNodeCopies() {
        return nodeCopies;
    }

    public String getSqlite() {
        return sqlite;
    }

    public void setSqlite(String sqlite) {
        this.sqlite = sqlite;
    }
}
