package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "broker")
public class Broker implements Serializable {

    @Id
    @Column(name = "broker_address", columnDefinition = "TEXT")
    private String brokerAddress;
    @Column(name = "node_copies")
    private Integer nodeCopies;
    // 0 = false , 1 = true
    private Integer active;

    public Broker(){ }

    public Broker(String brokerAddress, Integer nodeCopies, Integer active) {
        this.brokerAddress = brokerAddress;
        this.nodeCopies = nodeCopies;
        this.active = active;
    }

    public Broker(Broker broker) {
        this.brokerAddress = broker.brokerAddress;
        this.nodeCopies = broker.nodeCopies;
        this.active = broker.active;
    }

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getNodeCopies() {
        return nodeCopies;
    }

    public void setNodeCopies(Integer nodeCopies) {
        this.nodeCopies = nodeCopies;
    }
}
