package com.wahwahnow.controllers;

import com.wahwahnow.Utils;
import com.wahwahnow.models.Users;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody Map<String, Object> payload){
        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        int status = userService.authenticate(email, password);
        Map<String, Object> res = new HashMap<>();
        switch (status){
            case 404:
                res.put("msg", "User not found");
                res.put("statusMsg", "USER_NOT_FOUND");
                return ResponseEntity.status(status).body(res);
            case 401:
                res.put("msg", "Invalid password");
                res.put("statusMsg", "INVALID_PASSWORD");
                return ResponseEntity.status(status).body(res);
            case 200:
                Users user = userService.getUserByEmail(email);
                String token = Utils.createToken(user.getChannelName());
                if(!token.isBlank()){
                    res.put("msg", "Success");
                    res.put("statusMsg", "LOGIN_SUCCESS");
                    return ResponseEntity.status(status)
                            .header("jwt", token)
                            .body(res);
                }
        }

        // else server failure
        res.put("msg", "Something went wrong");
        res.put("statusMsg", "SERVER_ERROR");
        return ResponseEntity.status(502).body(res);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        userService.printUsers();
        return "";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody Map<String, Object> payload){
        String channelName = (String) payload.get("channelName");
        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        Map<String, Object> res = new HashMap<>();
        // user exists
        if(userService.userExists(channelName)) {
            res.put("msg", "User already exists");
            res.put("statusMsg", "USER_EXISTS");
            return ResponseEntity.status(403).body(res);
        }

        // email already in use
        if(userService.userEmailExists(email)){
            res.put("msg", "Email already in use");
            res.put("statusMsg", "EMAIL_EXISTS");
            return ResponseEntity.status(403).body(res);
        }

        // password error
        if(password.isBlank() || password.length() < 8){
            res.put("msg", "Bad password format");
            res.put("statusMsg", "BAD_PASSWORD_FORMAT");
            return ResponseEntity.status(401).body(res);
        }

        // create user
        boolean status = userService.createUser(channelName, email, password);
        if(status){
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
