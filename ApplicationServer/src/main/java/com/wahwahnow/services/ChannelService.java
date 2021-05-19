package com.wahwahnow.services;

import com.wahwahnow.dao.ChannelDao;
import com.wahwahnow.models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

   @Autowired
   private ChannelDao channelDao;

   public boolean createChannel(String id, String about, String channelName, int timestamp, String userid){
       return channelDao.createNewChannel(id, about, channelName, timestamp, userid);
   }


   public String getChannelID(String channelName){
        return channelDao.getChannelID(channelName);
   }

}
