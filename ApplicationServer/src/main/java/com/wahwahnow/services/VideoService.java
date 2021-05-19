package com.wahwahnow.services;

import com.wahwahnow.dao.VideoDao;
import com.wahwahnow.models.Video;
import com.wahwahnow.models.VideoTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    public List<VideoTag> getVideoTags(){
        return videoDao.getTags();
    }

    public List<Video> getVideos(int offset) { return videoDao.getVideos(offset);}

    public Video postVideo(String id, String channelID, String name, String description){
        return videoDao.postNewVideo(id, channelID, name, description);
    }

    public void putStreamable(String s, String video, boolean streamable) {
        videoDao.putStreamable(s, video, (streamable? 1 : 0));
    }

}
