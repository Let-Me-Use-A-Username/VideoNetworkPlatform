package com.wahwahnow;

import com.wahwahnow.models.Broker;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsisteHashRing {

    private TreeMap<String, Broker> ring;
    private int uniqueSize;

    public ConsisteHashRing(List<Broker> brokers, String secret){
        ring = new TreeMap<>();
        uniqueSize = 0;
        for(Broker broker: brokers){
            if(broker.getNodeCopies() < 1) continue;
            for(int i = 0; i < broker.getNodeCopies() && i < 10; i++){
                String message = broker.getBrokerAddress() + i + secret;
                String sha1 = Utils.sha1(message);
                ring.put(sha1, broker);
            }
            uniqueSize++;
        }
    }

    public Broker getBroker(String videoHash){
        String sha1 = Utils.sha1(videoHash);
        Map.Entry<String, Broker> highCollision = ring.ceilingEntry(sha1);
        if(highCollision != null) return highCollision.getValue();
        return ring.firstEntry().getValue();
    }

    public void removeBroker(Broker broker, String secret){
        for(int i = 0; i < broker.getNodeCopies() && i < 10; i++){
            String message = broker.getBrokerAddress() + i + secret;
            String sha1 = Utils.sha1(message);
            ring.remove(sha1, broker);
        }
        uniqueSize--;
    }

    public int size(){
        return uniqueSize;
    }

}
