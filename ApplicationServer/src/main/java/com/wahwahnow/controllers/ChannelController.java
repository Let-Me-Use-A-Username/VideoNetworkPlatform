package com.wahwahnow.controllers;

import com.wahwahnow.BrokerConnection;
import com.wahwahnow.ConsisteHashRing;
import com.wahwahnow.Utils;
import com.wahwahnow.models.Broker;
import com.wahwahnow.models.Video;
import com.wahwahnow.services.BrokerService;
import com.wahwahnow.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private static final int videoCopies = 2;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private BrokerService brokerService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadVideo(@RequestBody Map<String, Object> payload, @RequestHeader("jwt") String jwt){
        Map<String, Object> res = new HashMap<>();

        // verify user
        String channelName = Utils.verifyToken(jwt);

        String videoName = (String) payload.get("videoName");
        String description = (String) payload.get("description");
        Integer tagID = (Integer) payload.get("tagID");

        // get channel id
        String channelID = channelService.getChannelID(channelName);

        int tries = 15;
        boolean success = false;
        Video video = null;
        while (tries > 0){
            video = channelService.postVideo(Utils.base64(System.currentTimeMillis()+""),channelID, videoName, description);
            if(video != null) {
                success = true;
                break;
            }
            tries--;
        }

        // hash artist (sha1(channelName)
        String artistHash = Utils.sha1(channelName);
        // hash video (sha1(id))
        String videoHash = Utils.sha1(video.getId());


        List<Broker> brokerList = brokerService.getBrokers();
        if(brokerList.size() <= 0) {
            return ResponseEntity.status(500).body("");
        }

        Broker firstBroker = null;
        ConsisteHashRing ring = new ConsisteHashRing(brokerList, Utils.getSecret());
        for(int copies = videoCopies ; copies > 0; copies--){
            Broker broker = ring.getBroker(videoHash);
            if(!BrokerConnection.checkAlive(broker)){
                ring.removeBroker(broker, Utils.getSecret());
                continue;
            }

            final Broker source = firstBroker != null? new Broker(firstBroker) : null;
            new Thread(() -> {
                BrokerConnection.notifyBrokerTopic(artistHash, videoHash, broker, source);
            }).start();
            if(copies == videoCopies) firstBroker = broker;
            ring.removeBroker(broker, Utils.getSecret());
        }

        if(success && firstBroker != null){
            res.put("msg", "Success");
            res.put("artist", artistHash);
            res.put("video", videoHash);
            res.put("uploadServer", firstBroker.getBrokerAddress());
            return ResponseEntity.status(200).body(res);
        }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }

}
