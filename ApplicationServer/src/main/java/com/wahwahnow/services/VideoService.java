package com.wahwahnow.services;

import com.wahwahnow.dao.VideoDao;
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

}
