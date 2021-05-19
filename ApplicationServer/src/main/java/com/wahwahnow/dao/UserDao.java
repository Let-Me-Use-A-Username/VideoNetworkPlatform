package com.wahwahnow.dao;

import com.wahwahnow.models.Users;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean userEmailExists(String email){
        Session session = getSession();
        try {
            Users user = (Users) session.createQuery("FROM users WHERE email=:email")
                    .setParameter("email", email)
                    .getSingleResult();
            return true;
        }catch (NoResultException r){
            return false;
        }
    }

    @Transactional
    public Users createUser(String uuid, String email){
        Session session = getSession();
        Users user = new Users(uuid, email);
        session.persist(user);
        return user;
    }

    public Users getUserByID(String id){
        Session session = getSession();
        try {
            return (Users) session.createQuery("FROM users WHERE id=:id")
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException r){
            return null;
        }
    }

    public Users getUserByEmail(String email){
        Session session = getSession();
        try {
            return (Users) session.createQuery("FROM users WHERE email=:email")
                    .setParameter("email", email)
                    .getSingleResult();
        }catch (NoResultException r){
            return null;
        }
    }
//
//    public Users setChannelEmail(String id, String email){
//        users.get(id).setEmail(email);
//        return users.get(id);
//    }
//

    public Session getSession(){
        Session session = null;
        if(entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
            throw new NullPointerException();
        }
        return session;
    }

}
