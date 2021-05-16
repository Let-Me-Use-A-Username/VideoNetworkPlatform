package com.wahwahnow.controllers;

import com.wahwahnow.Utils;
import com.wahwahnow.models.Video;
import com.wahwahnow.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

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


        if(success){
            // hash artist (sha1(channelName)
            String artistHash = Utils.sha1(channelName);
            // hash video (sha1(id))
            String videoHash = Utils.sha1(video.getId());
            res.put("msg", "Success");
            res.put("artist", artistHash);
            res.put("video", videoHash);
            return ResponseEntity.status(200).body(res);
        }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }

}
