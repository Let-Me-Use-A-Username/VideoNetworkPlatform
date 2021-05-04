package com.wahwahnow.dao;

import com.wahwahnow.models.Channel;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChannelDao {

    private static Map<String, Channel> channels;

    static {
        channels = new HashMap<String, Channel>(){
            {
                put("1",new Channel(
                        "1",
                        "PewDiePie",
                        "Let's Play Channel",
                        new ArrayList<String>(Collections.singletonList("Gaming")),
                        100,
                        "19-05-1984"
                ));
                put("2",new Channel(
                        "2",
                        "HowToBasic",
                        "Fucked up channel",
                        new ArrayList<String>(Collections.singletonList("Entertainment")),
                        69,
                        "11-05-2019"
                ));
            }
        };
    }

    /*public Channel getChannelByName(){
        for(Channel channel: channels.values()){

        }
    }

    public Collection<Channel> getChannelsByTag(){

    }*/

    //To be deleted..

    public Collection<Channel> getAllChannels(){
        return channels.values();
    }
}
