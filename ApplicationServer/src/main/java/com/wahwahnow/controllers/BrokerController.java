package com.wahwahnow.controllers;

import com.wahwahnow.services.BrokerService;
import com.wahwahnow.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/brokers")
public class BrokerController {

    @Autowired
    private BrokerService brokerService;
    @Autowired
    private VideoService videoService;

    @RequestMapping(value = "/notify" ,method = RequestMethod.POST)
    public void getNotified(@RequestBody Map<String, Object> payload){

        String brokerAddress = (String) payload.get("brokerAddress");
        int port = (int) payload.get("port");
        int nodeCopies = (int) payload.get("nodeCopies");

        boolean status = brokerService.postBroker(brokerAddress, port, nodeCopies, 1);
        if(status){
            System.out.println("- Ping (alive) from broker: "+brokerAddress+": "+port);
        }

    }


    @RequestMapping(value = "/video/image", method = RequestMethod.POST)
    public void getVideoImage(@RequestBody Map<String, Object> payload){

        String videoID = payload.get("videoID").toString();
        String base64 = payload.get("base64").toString();

        videoService.putVideoImage(videoID, base64);

    }

    @RequestMapping(value = "/video/notify", method = RequestMethod.POST)
    public void getVideoNotified(@RequestBody Map<String, Object> payload){

        System.out.println("Broker notify");

        String brokerAddress = (String) payload.get("brokerAddress");
        int port = (int) payload.get("port");
        boolean streamable = (boolean) payload.get("streamable");
        String video = (String) payload.get("video");

        System.out.println("Updating...");

        videoService.putStreamable(brokerAddress+":"+port, video, streamable);

    }

}
