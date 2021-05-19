package com.wahwahnow.broker.models;

public class HttpJsonModel {


    public static String contentType(){
        return "application/json";
    }

    public static String postNotify(String brokerAddress, int port, int nodeCopies){
        return " { \"brokerAddress\": \""+brokerAddress+"\", \"port\": "+port+", \"nodeCopies\": "+nodeCopies+" }";
    }

    public static String postVideoNotify(String brokerAddress, int port,String video, boolean status){
        return " { \"brokerAddress\": \""+brokerAddress+"\", \"port\": "+port+", \"video\": \""+video+"\", \"streamable\": "+status+" }";
    }

}
