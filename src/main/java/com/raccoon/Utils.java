package com.raccoon;

import org.apache.commons.codec.digest.DigestUtils;

public class Utils {

    public static String sha1(String stringToDigest){
        return DigestUtils.sha1Hex(stringToDigest);
    }

}
