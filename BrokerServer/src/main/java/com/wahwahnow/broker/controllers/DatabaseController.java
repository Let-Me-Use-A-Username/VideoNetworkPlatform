package com.wahwahnow.broker.controllers;

import com.wahwahnow.broker.database.Tables;
import com.wahwahnow.broker.models.VideoDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<VideoDirectory> getVideos(String sql){
        ArrayList<VideoDirectory> videos = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()){
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()){
                //id, uploader_id, videoPath, chunks, length, height, width, framerate
                String id = set.getString("id");
                String uploaderID = set.getString("uploader_id");
                int chunks = set.getInt("chunks");
                String videoPath = set.getString("videoPath");
                int height = set.getInt("height");
                int width = set.getInt("width");
                float length = set.getFloat("length");
                float framerate = set.getFloat("framerate");
                videos.add(new VideoDirectory(id,uploaderID,chunks, videoPath,height, width, length,framerate));
            }
        }catch (SQLException e){ return new ArrayList<>();}
        return videos;
    }

}
