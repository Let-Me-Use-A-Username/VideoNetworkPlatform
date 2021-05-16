package com.wahwahnow.services;

import com.wahwahnow.dao.ChannelDao;
import com.wahwahnow.models.Channel;
import com.wahwahnow.models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ChannelService {

   @Autowired
   private ChannelDao channelDao;

   public boolean createChannel(String id, String about, String channelName, int timestamp, String userid){
       return channelDao.createNewVideo(id, about, channelName, timestamp, userid);
   }

   public Video postVideo(String id, String channelID, String name, String description){
        return channelDao.postNewVideo(id, channelID, name, description);
   }

   public String getChannelID(String channelName){
        return channelDao.getChannelID(channelName);
   }

}
