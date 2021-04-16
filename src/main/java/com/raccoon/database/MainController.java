package com.raccoon.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainController {

    public static void createNewDatabase(String fileName, String osURL) {

        String url = "jdbc:sqlite:" + osURL + "/" + fileName;

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

}
