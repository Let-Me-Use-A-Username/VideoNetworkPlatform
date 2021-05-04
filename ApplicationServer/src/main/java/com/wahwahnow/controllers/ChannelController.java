package com.wahwahnow.controllers;

import com.wahwahnow.models.Channel;
import com.wahwahnow.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Channel> getAllChannels(){
        return channelService.getAllChannels();
    }
}
