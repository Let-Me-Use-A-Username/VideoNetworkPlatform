package com.wahwahnow.dao;

import com.wahwahnow.models.Users;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDao {

    private static Map<String, Users> users;

    static {
        users = new HashMap<>() {
            {
                put("1", new Users(
                        "1",
                        "PewDiePie",
                        "pewdiepie@gmail.com"
                ));
                put("2", new Users(
                        "2",
                        "HowToBasic",
                        "hwtb@gmail.com"
                ));
                put("3", new Users(
                        "3",
                        "SallyUp",
                        "sallyUp@yahoo.com"
                ));
            }
        };
    }

    public boolean userExists(String channelName){
        return users.values()
                .stream()
                .anyMatch((u -> u.getChannelName().equals(channelName)));
    }

    public boolean userEmailExists(String email){
        return users.values()
                .stream()
                .anyMatch((u -> u.getEmail().equalsIgnoreCase(email)));
    }

    public Users createUser(String uuid, String channelName, String email){
        users.put(uuid, new Users(uuid, channelName, email));
        return users.get(uuid);
    }

    public Users getUserByID(String id){
        return users.get(id);
    }

    public Users getUserByEmail(String email){
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Users setChannelEmail(String id, String email){
        users.get(id).setEmail(email);
        return users.get(id);
    }

    public void printUsers() {
        users.values().forEach(u -> {
            System.out.println(u.getChannelName()+" "+u.getEmail()+" "+u.getId());
        });
    }
}
