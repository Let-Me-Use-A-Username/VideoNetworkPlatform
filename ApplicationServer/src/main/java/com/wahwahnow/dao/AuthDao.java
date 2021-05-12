package com.wahwahnow.dao;

import com.wahwahnow.models.ChannelAuth;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthDao {

    private static Map<String, ChannelAuth> auth;

    static {
        auth = new HashMap<>() {
            {
                put("1", new ChannelAuth(
                        "1",
                        "pewdiewpie",
                        "nameInLowerCase"
                ));
                put("2", new ChannelAuth(
                        "2",
                        "howtobasic",
                        "nameInLowerCase"
                ));
                put("3", new ChannelAuth(
                        "3",
                        "sallyup",
                        "nameInLowerCase"
                ));
            }
        };
    }

    public ChannelAuth get(String uuid){
        return auth.get(uuid);
    }

    public boolean put(ChannelAuth cAuth){
        auth.put(cAuth.getUserID(), cAuth);
        return true;
    }

}
