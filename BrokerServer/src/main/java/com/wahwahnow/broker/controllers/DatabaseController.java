package com.wahwahnow.broker.controllers;

import com.wahwahnow.broker.database.Tables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseController {

    private String URL;
    private String osURL;
    private String filename;

    public DatabaseController(String filepath){
        File f = new File(filepath);
        osURL = f.getParent();
        filename = f.getName();
        System.out.println("Os url is "+osURL);
        System.out.println("File name is "+filename);
        URL = "jdbc:sqlite:" + osURL + "/" + filename;
    }

    public void createNewDatabase() {

        String url = URL;

        try {
            Files.createDirectories(Paths.get(osURL));
        }catch (IOException ignore){ }

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeBrokerTables() {

        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            // create tables
            stmt.execute(Tables.CREATE_UPLOADERS);
            stmt.execute(Tables.CREATE_VIDEOS);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean executeQuery(String sql) {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()){
            stmt.executeQuery(sql);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

}
