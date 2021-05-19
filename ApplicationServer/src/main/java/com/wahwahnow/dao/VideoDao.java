package com.wahwahnow.dao;

import com.wahwahnow.models.*;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VideoDao{

    @PersistenceContext
    private EntityManager entityManager;

    // Ignore unexpected. Works as it should
    public List<VideoTag> getTags(){
        Session session = getSession();
        TypedQuery<VideoTag> q = session.createQuery("FROM video_tag", VideoTag.class);
        return q.getResultList();
    }

    public List<Video> getVideos(int offset) {
        Session session = getSession();
        try{
            TypedQuery<Video> q = session.createNativeQuery("SELECT * FROM video INNER JOIN broker_video " +
                    "WHERE video.id = broker_video.videoid AND broker_video.streamable = 1 " +
                    "LIMIT 10 OFFSET "+offset,Video.class);
            return q.getResultList();
        }
        catch (NoResultException e){
            e.printStackTrace();
            return new ArrayList<>();}
    }


    //TODO: fix channelid to not be null
    @Transactional
    public Video postNewVideo(String id, String channelID, String name, String description) {
        Session session = getSession();
        Video video = new Video(id, name, description, 0, 0, channelID, 0);
        session.persist(video);
        Video videoRes = session.getReference(Video.class, id);
        if (videoRes.getChannelID().equals(channelID)) {
            return videoRes;
        }
        return null;
    }

    @Transactional
    public void putStreamable(String brokerAddress, String video, int status){
        Session session = getSession();
        BrokerVideo brokerVideo = new BrokerVideo(brokerAddress, video, status);
        session.persist(brokerVideo);
    }

    public Session getSession(){
        Session session = null;
        if(entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException();
        }
        return session;
    }

}
