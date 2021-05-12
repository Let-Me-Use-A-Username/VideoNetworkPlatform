package com.wahwahnow;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wahwahnow.models.BcryptModel;
import com.wahwahnow.models.ChannelAuth;
import org.springframework.security.crypto.bcrypt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class Utils {

    //TODO: pass these values in a spring component
    private static final String secret = "5gIsTheVirus";
    private static final int SALT_ROUNDS = 12;

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static BcryptModel genBcrypt(String password){
        BcryptModel bcryptModel = new BcryptModel();
        bcryptModel.password = password;
        bcryptModel.salt = BCrypt.gensalt(SALT_ROUNDS);
        bcryptModel.hash = BCrypt.hashpw(bcryptModel.password, bcryptModel.salt);
        return bcryptModel;
    }

    public static boolean authBcrypt(String password, ChannelAuth cAuth){
        return BCrypt.checkpw(password, cAuth.getPassword());
    }

    public static String readSecret(String filepath){
        File f = new File(filepath);
        StringBuilder ob = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            while ((line = br.readLine()) != null) ob.append(line);
        }catch (IOException e){ }
        return ob.toString();
    }

    public static String createToken(String channelName){
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.
                    create()
                    .withIssuer("auth0")
                    .withClaim("channelName", channelName)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            return "";
        }
    }


    public static String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .acceptExpiresAt(172800)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            // if expired return return 1
            return jwt.getClaim("channelName").toString();
        } catch (JWTVerificationException exception){
            return "";
        }
    }


}
