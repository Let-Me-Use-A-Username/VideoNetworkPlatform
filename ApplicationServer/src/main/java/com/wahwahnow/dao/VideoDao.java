package com.wahwahnow.dao;

import com.wahwahnow.models.*;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    public Session getSession(){
        Session session = null;
        if(entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException();
        }
        return session;
    }

}
