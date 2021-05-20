package com.wahwahnow.services;

import com.wahwahnow.dao.ChannelDao;
import com.wahwahnow.dao.VideoDao;
import com.wahwahnow.models.Channel;
import com.wahwahnow.models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

   @Autowired
   private ChannelDao channelDao;
   @Autowired
   private VideoDao videoDao;

   public boolean createChannel(String id, String about, String channelName, int timestamp, String userid){
       return channelDao.createNewChannel(id, about, channelName, timestamp, userid);
   }


   public String getChannelID(String channelName){
        return channelDao.getChannelID(channelName);
   }

    public String getChannelName(String userid) {
       return channelDao.getChannelName(userid);
    }

    public Channel getChannelByVideoId(String video_id) {
       Video video = videoDao.getVideoById(video_id);
       System.out.println(video);
       if(video == null) return null;
       return channelDao.getChannel(video.getChannelName());
    }
}
