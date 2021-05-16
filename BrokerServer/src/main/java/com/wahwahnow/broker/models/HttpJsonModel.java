package com.wahwahnow.broker.models;

public class HttpJsonModel {


    public static String contentType(){
        return "application/json";
    }

    public static String postNotify(String brokerAddress, int port, int nodeCopies){
        return " { \"brokerAddress\": \""+brokerAddress+"\", \"port\": "+port+", \"nodeCopies\": "+nodeCopies+" }";
    }
}
