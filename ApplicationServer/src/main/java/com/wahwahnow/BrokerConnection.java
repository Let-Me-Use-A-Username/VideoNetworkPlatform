package com.wahwahnow;

import com.wahwahnow.models.Broker;

public class BrokerConnection {

    // Notify broker about topic
    // if there is a source the broker will get the files from the source broker(the broker who will be send to the client in order to upload the video)
    public static void notifyBrokerTopic(String artistHash, String videoHash, Broker broker, Broker source){
        System.out.println("I will notify about "+videoHash+" broker "+broker.getBrokerAddress());
    }

}
