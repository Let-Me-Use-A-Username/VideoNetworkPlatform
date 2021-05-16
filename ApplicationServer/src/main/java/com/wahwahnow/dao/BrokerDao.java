package com.wahwahnow.dao;

import com.wahwahnow.models.Broker;
import com.wahwahnow.models.VideoTag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrokerDao {

    @PersistenceContext
    private EntityManager entityManager;


    public boolean brokerExists(String address, int port){
        Session session = getSession();
        try{
            Broker br = (Broker) session.createQuery("FROM broker WHERE broker_address=:addressPort")
                    .setParameter("addressPort", address+":"+port).getSingleResult();
            return true;
        }catch (NoResultException r){
            return false;
        }
    }

    @Transactional
    public boolean postBroker(String address, int port, int nodeCopies){
        Session session = getSession();
        Broker broker = new Broker(address+":"+port, nodeCopies, 1);
        session.persist(broker);
        return true;
    }

    @Transactional
    public boolean putBroker(String address, int port, int nodeCopies, int status){
        Session session = getSession();
        Broker old = (Broker) session.createQuery("FROM broker WHERE brokerAddress=:addressPort")
                .setParameter("addressPort", address+":"+port).getSingleResult();
        session.evict(old);
        Broker broker = new Broker(address+":"+port, nodeCopies, status);
        session.update(broker);
        return true;
    }

    public List<Broker> getBrokers() {
        Session session = getSession();
        TypedQuery<Broker> q = session.createQuery("FROM broker", Broker.class);
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
