package com.wahwahnow.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "broker")
public class Broker implements Serializable {

    @Id
    private String brokerAddress;
    // 0 = false , 1 = true
    private Integer active;

    public Broker(){ }

    public Broker(String brokerAddress, Integer active) {
        this.brokerAddress = brokerAddress;
        this.active = active;
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

}
