package com.wahwahnow.dao;

import com.wahwahnow.models.Channel;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
            TypedQuery<Channel> q = session.createQuery("FROM channel WHERE channel_name=:channelName", Channel.class)
                    .setParameter("channelName", channelName);
            return q.getSingleResult().getUserID();
        }catch (NoResultException r){
            return "";
        }
    }

    public String getChannelName(String userid) {
        Session session = getSession();
        try{
            TypedQuery<Channel> q = session.createQuery("FROM channel WHERE userid=:in_userid", Channel.class).setParameter("in_userid", userid);
            return q.getSingleResult().getChannelName();
        }catch (NoResultException r){
            return "";
        }
    }

    public Channel getChannel(String channelName) {
        Session session = getSession();
        try{
            TypedQuery<Channel> q = session.createQuery("FROM channel WHERE channel_name=:name", Channel.class).setParameter("name", channelName);
            return q.getSingleResult();
        }catch (NoResultException r){
            return null;
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
