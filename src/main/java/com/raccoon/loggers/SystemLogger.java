package com.raccoon.loggers;

public class SystemLogger {

    public static synchronized void Log(String message){
        System.out.println(message);
    }

}
