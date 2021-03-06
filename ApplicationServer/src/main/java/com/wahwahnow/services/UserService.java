package com.wahwahnow.services;

import com.wahwahnow.Utils;
import com.wahwahnow.dao.AuthDao;
import com.wahwahnow.dao.UserDao;
import com.wahwahnow.models.BcryptModel;
import com.wahwahnow.models.UserAuth;
import com.wahwahnow.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthDao authDao;

    public boolean userEmailExists(String email) {
        return userDao.userEmailExists(email);
    }

    public boolean createUser(String email, String password){
        String uuid = Utils.generateUUID();
        BcryptModel bcryptModel = Utils.genBcrypt(password);
        userDao.createUser(uuid, email);
        return authDao.put(new UserAuth(uuid, bcryptModel.hash, BcryptModel.method, 12));
    }

    // 401 : bad password 404: user not found
    public int authenticate(String email, String password){
        if(!userDao.userEmailExists(email)) return 404;
        String id = userDao.getUserByEmail(email).getId();
        UserAuth cAuth = authDao.get(id);
        return Utils.authBcrypt(password, cAuth)? 200 : 401;
    }


    public int updateEmail(String id, String newEmail){

        return 200;
    }

    public int updatePassword(String id, String oldPassword, String password){

        return 200;
    }

    public Users getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public boolean userExists(String channelName) {
        return userDao.userEmailExists(channelName);
    }
}
