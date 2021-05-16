package com.wahwahnow.broker.database;

public class Queries {

    public static String insertUploader(String id, String videoPath){
        return "INSERT INTO uploaders (id, folder_path) \n"
                + "VALUES (" + id + ", "+ videoPath + ");";
    }

    public static String insertVideo(String id, String uploader_id, String videoPath){
        return "INSERT INTO videos (id, uploader_id, videoPath) \n"
                + "VALUES (" + id + ", "+ uploader_id + ", "+ videoPath+" );";
    }

    public static String insertVideo(String id, String uploader_id, String videoPath, int chunks, float length, int height, int width, float framerate){
        return "INSERT INTO videos (id, uploader_id, videoPath, chunks, length, height, width, framerate) \n"
                + "VALUES (" + id + ", "+ uploader_id + ", "+ videoPath +  ", "+ chunks + ", " + length + ", " + height + ", " + width + ", " + framerate +");";
    }

    public static String deleteUploader(String id){
        return "DELETE FROM uploaders" +
                "WHERE uploaders.id = " + id + ";";
    }

    public static String deleteVideo(String id){
        return "DELETE FROM videos" +
                "WHERE videos.id = " + id + ";";
    }

    public static String getVideo(String video) {
        return "SELECT * FROM videos WHERE id = "+video+";";
    }
}
