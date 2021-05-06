package com.wahwahnow.databases;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseController {

    private final String URL;
    private final String osURL;

    public DatabaseController(String filename, String osURL){
        this.osURL = osURL;
        URL = "jdbc:sqlite:" + osURL + "/" + filename;
    }

    public void createNewDatabase() {

        try {
            Files.createDirectories(Paths.get(osURL));
        }catch (IOException ignore){ }

        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeApplicationServerTables() {

        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            // create tables
            stmt.execute(Tables.CREATE_CHANNELS);
            System.out.println("Finished creating channels");
            stmt.execute(Tables.CREATE_USERS);
            System.out.println("Finished creating users");
            stmt.execute(Tables.CREATE_USERPROFILEIMAGES);
            System.out.println("Finished creating userProfileImages");
            stmt.execute(Tables.CREATE_VIDEOIMAGEPREVIEWS);
            System.out.println("Finished creating videoImagePreviews");
            stmt.execute(Tables.CREATE_COMMENTS);
            System.out.println("Finished creating comments");
            stmt.execute(Tables.CREATE_HASHTAGS);
            System.out.println("Finished creating hashtags");
            stmt.execute(Tables.CREATE_VIDEOS);
            System.out.println("Finished creating videos");
            stmt.execute(Tables.CREATE_LIKES);
            System.out.println("Finished creating likes");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
