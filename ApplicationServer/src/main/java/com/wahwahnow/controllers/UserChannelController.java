package com.wahwahnow.controllers;

import com.wahwahnow.Utils;
import com.wahwahnow.models.Users;
import com.wahwahnow.services.ChannelService;
import com.wahwahnow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UserChannelController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody Map<String, Object> payload){

        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        int status = userService.authenticate(email.toLowerCase(), password);
        Map<String, Object> res = new HashMap<>();
        switch (status) {
            case 401 -> {
                res.put("msg", "Invalid password");
                res.put("statusMsg", "INVALID_PASSWORD");
                return ResponseEntity.status(status).body(res);
            }
            case 404 -> {
                res.put("msg", "User not found");
                res.put("statusMsg", "USER_NOT_FOUND");
                return ResponseEntity.status(status).body(res);
            }
            case 200 -> {
                Users user = userService.getUserByEmail(email);
                // Get user's channel name
                String token = Utils.createToken(channelService.getChannelName(user.getId()));
                if (!token.isBlank()) {
                    res.put("msg", "Success");
                    res.put("statusMsg", "LOGIN_SUCCESS");
                    return ResponseEntity.status(status)
                            .header("jwt", token)
                            .body(res);
                }
            }
        }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody Map<String, Object> payload){
        String channelName = (String) payload.get("channelName");
        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        Map<String, Object> res = new HashMap<>();
        // channel exists
        if(userService.userExists(channelName)) {
            res.put("msg", "User already exists");
            res.put("statusMsg", "USER_EXISTS");
            return ResponseEntity.status(403).body(res);
        }

        // email already in use
        if(userService.userEmailExists(email.toLowerCase())){
            res.put("msg", "Email already in use");
            res.put("statusMsg", "EMAIL_EXISTS");
            return ResponseEntity.status(403).body(res);
        }

        // password error
        if(password.isBlank() || password.length() < 8){
            res.put("msg", "Bad password format");
            res.put("statusMsg", "BAD_PASSWORD_FORMAT");
            return ResponseEntity.status(400).body(res);
        }

        // create user and channel
        boolean status = userService.createUser(email.toLowerCase(), password);
        Users user = userService.getUserByEmail(email.toLowerCase());
        boolean status2 = channelService.createChannel(Utils.generateUUID(), "", channelName, (int) (System.currentTimeMillis() / 1000), user.getId());
        if(status && status2){
            // respond success
            res.put("msg", "Channel created");
            res.put("statusMsg", "CHANNEL_CREATED");
            return ResponseEntity.status(200).body(res);
        }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }

}
