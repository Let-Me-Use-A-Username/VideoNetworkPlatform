package com.wahwahnow.dao;

import com.wahwahnow.models.UserAuth;
import com.wahwahnow.models.Users;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuth get(String uuid){
        Session session = getSession();
        try {
            return (UserAuth) session.createQuery("FROM user_auth WHERE id=:id")
                    .setParameter("id", uuid)
                    .getSingleResult();
        }catch (NoResultException r){
            return null;
        }
    }

    @Transactional
    public boolean put(UserAuth cAuth){
        Session session = getSession();
        session.persist(cAuth);
        return true;
    }

    public Session getSession(){
        Session session = null;
        if(entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException();
        }
        return session;
    }

}
