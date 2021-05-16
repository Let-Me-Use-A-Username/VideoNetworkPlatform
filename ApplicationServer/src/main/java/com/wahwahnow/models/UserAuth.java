package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity(name = "user_auth")
public class UserAuth {

    @Id
    @Column(name = "user_id", columnDefinition = "TEXT")
    private String userID;
    @Column(name = "hash", columnDefinition = "TEXT")
    private String hash;
    @Column(name = "hash_function", columnDefinition = "TEXT")
    private String hashFunction;
    private int rounds;

    public UserAuth(){ }

    public UserAuth(String userID, String hash, String hashFunction, int rounds){
        this.userID = userID;
        this.hash = hash;
        this.hashFunction = hashFunction;
        this.rounds = rounds;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashFunction() {
        return hashFunction;
    }

    public void setHashFunction(String hashFunction) {
        this.hashFunction = hashFunction;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
