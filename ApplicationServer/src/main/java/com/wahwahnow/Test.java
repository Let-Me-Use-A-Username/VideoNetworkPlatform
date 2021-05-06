package com.wahwahnow;

import com.wahwahnow.databases.DatabaseController;

public class Test {

    public static void main(String[] args){

        String filename = "ApplicationServerDatabase.db";
        String osURL = "D:/DS/applicationServer/db";

        DatabaseController dbc = new DatabaseController(filename, osURL);
        dbc.createNewDatabase();
        dbc.initializeApplicationServerTables();
    }
}
