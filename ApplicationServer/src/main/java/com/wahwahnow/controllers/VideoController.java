package com.wahwahnow.controllers;

import com.wahwahnow.Utils;
import com.wahwahnow.models.BrokerVideo;
import com.wahwahnow.models.Channel;
import com.wahwahnow.models.Video;
import com.wahwahnow.services.ChannelService;
import com.wahwahnow.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private ChannelService channelService;

    /*
        Get published videos from: index * 10 + 1 to index * 10 + 11
     */
    @RequestMapping(value = "/{offset}")
    public ResponseEntity getLatest(@PathVariable String offset){
        Map<String, Object> res = new HashMap<>();

        int off = 0;
        try{
            off = Integer.parseInt(offset);
        }catch (Exception e) {
        }

        //tha prepei na valoume na stelnei o broker oti to video einai streamable gia na valoume kai to where (streamable) molis kanei ta chunk
        // tsouk
        // mporei na argisei na to kommatiasei
        // kai tha prepei na valoume kapoia stigmi tropo na afaireite apo tin db. p.x. ena timer
        // ston messagequeue kalitera gia na min gemisoume ton broker me threads
        // pou tsekarei an mesa se 5 lepta dn exei steilei o client na to vgazei o broker kai apo tin db kai apo ton app

        List<Video> videoList = videoService.getVideos(off * 10);

        res.put("videos", videoList);
        return ResponseEntity.status(200).body(res);
    }

    @RequestMapping(value = "/image/{video_id}")
    public ResponseEntity getVideoImage(@PathVariable String video_id){
        Map<String, Object> res = new HashMap<>();
        String base64 = videoService.getVideoImage(video_id);
        if(base64 == null){
            return ResponseEntity.status(404).body("{ \"msg\": \"Not Found\" }");
        }

        res.put("videoID", video_id);
        res.put("base64", base64);
        return ResponseEntity.status(200).body(res);
    }

    @RequestMapping(value = "/videos/{video_id}")
    public ResponseEntity getVideo(@PathVariable String video_id){
        Map<String, Object> res = new HashMap<>();

        // get channel name to get channelName
        Channel ch = channelService.getChannelByVideoId(video_id);

        String artistHash = Utils.sha1(ch.getChannelName());

        // Get broker who has it
        List<BrokerVideo> brokerList = videoService.getBrokers(video_id);
        List<String> brokers = new ArrayList<>();
        brokerList.forEach((b)->{
            brokers.add(b.getBrokerAddress());
        });

        res.put("artist", artistHash);
        res.put("brokers", brokers);

        return ResponseEntity.status(200).body(res);
    }

    @RequestMapping(value = "/videoTags", method = RequestMethod.GET)
    public ResponseEntity getVideoTags(){
        Map<String, Object> res = new HashMap<>();

        try{
            res.put("videoTags", videoService.getVideoTags());
            return ResponseEntity.status(200).body(res);
        }catch (Exception e){ }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }


}
