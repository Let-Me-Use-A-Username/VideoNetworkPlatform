package com.wahwahnow.broker.io;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonHandler {

    static Gson gson = new Gson();

    public static Object readJson(String filepath, Object objectType){
        try(Reader reader = new FileReader(filepath)){
            return gson.fromJson(reader, objectType.getClass());
        } catch (IOException e){ e.printStackTrace(); }

        return null;
    }

}
