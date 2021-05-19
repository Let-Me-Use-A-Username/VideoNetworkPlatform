package com.wahwahnow.controllers;

import com.wahwahnow.models.Video;
import com.wahwahnow.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

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

    @RequestMapping(value = "/video/{video_id}")
    public ResponseEntity getVideo(@PathVariable String video_id){
        Map<String, Object> res = new HashMap<>();

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
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
