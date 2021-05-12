package com.wahwahnow.services;

import com.wahwahnow.dao.ChannelDao;
import com.wahwahnow.models.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelService {

   @Autowired
    private ChannelDao channelDao;

    public Collection<Channel> getAllChannels(){
        return channelDao.getAllChannels();
    }

}
