package com.wahwahnow.services;

import com.wahwahnow.dao.BrokerDao;
import com.wahwahnow.models.Broker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerService {

    @Autowired
    private BrokerDao brokerDao;

    public boolean brokerExists(String address, int port){
        return brokerDao.brokerExists(address, port);
    }

    // Can be used both for post and update
    public boolean postBroker(String adress, int port, int nodeCopies, int status){
        return brokerExists(adress, port)? brokerDao.putBroker(adress, port, nodeCopies, status) : brokerDao.postBroker(adress, port, nodeCopies);
    }


    public List<Broker> getBrokers() {
        return brokerDao.getBrokers();
    }
}
