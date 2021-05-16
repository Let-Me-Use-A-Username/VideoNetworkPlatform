package com.wahwahnow.controllers;

import com.wahwahnow.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

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
