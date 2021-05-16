package com.wahwahnow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static String getServerURL(String filepath){
        File f = new File(filepath);
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            br.close();
            return line.replace("\\s+", "").replace("\n", "").replace("\r", "");
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
