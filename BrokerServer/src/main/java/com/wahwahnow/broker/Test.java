package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.wahwahnow.broker.io.FileReader;


public class Test {

    private static Gson gson = new Gson();

    public static void main(String[] args) {

        String filepath = "./root/7ae32df5e881e91bba528875c2985cb50b19e581/baa7cc4b59b64fbe619ff3696e068603d603329e";

        System.out.println("Last fragmentID is "+FileReader.getLastFragmentID(filepath));

    }
}
