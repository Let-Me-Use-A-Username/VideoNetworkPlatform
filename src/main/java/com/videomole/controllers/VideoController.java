package com.videomole.controllers;

import com.videomole.Models.VideoDirectory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VideoController {

    // hash function: SHA512
    public String hash(String filename){
        String hashed = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(filename.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            hashed = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    public boolean fragmentation(byte[] file, VideoDirectory videoDirectory, long maxChunkSize){

        return true;
    }

    public void deleteFailedFragmentation(){

    }

    public void stream(){

    }

}
