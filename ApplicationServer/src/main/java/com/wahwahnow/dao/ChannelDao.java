package com.wahwahnow.dao;

import com.wahwahnow.models.Channel;
import com.wahwahnow.models.Video;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ChannelDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean createNewChannel(String id, String about, String channelName, int timestamp, String userid){
        Session session = getSession();
        Channel channel = new Channel(id, channelName, "", 0, timestamp, userid);
        session.persist(channel);
        return true;
    }

    public String getChannelID(String channelName) {
        Session session = getSession();
        try {
            Channel ch = (Channel) session.createQuery("FROM channel WHERE channel_name=:channelName")
                    .setParameter("channelName", channelName)
                    .getSingleResult();
            return ch.getUserID();
        }catch (NoResultException r){
            return "";
        }
    }

    public Session getSession(){
        Session session = null;
        if(entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException();
        }
        return session;
    }
}
