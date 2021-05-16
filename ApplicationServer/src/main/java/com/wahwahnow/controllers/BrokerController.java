package com.wahwahnow.controllers;

import com.wahwahnow.services.BrokerService;
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

    @RequestMapping(value = "/notify" ,method = RequestMethod.POST)
    public ResponseEntity getNotified(@RequestBody Map<String, Object> payload){

        String brokerAddress = (String) payload.get("brokerAddress");
        int port = (int) payload.get("port");
        int nodeCopies = (int) payload.get("nodeCopies");

        boolean status = brokerService.postBroker(brokerAddress, port, nodeCopies, 1);
        if(status){
            System.out.println("- Ping (alive) from broker: "+brokerAddress+": "+port);
            return ResponseEntity.status(200).body("");
        }

        return ResponseEntity.status(502).body("");
    }

}
