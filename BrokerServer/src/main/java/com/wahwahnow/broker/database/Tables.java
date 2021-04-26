package com.wahwahnow.broker.database;

public class Tables {

    public static final String CREATE_UPLOADERS = "CREATE TABLE IF NOT EXISTS uploaders (\n"
            + "	id text PRIMARY KEY, \n"
            + "	folder_path text NOT NULL \n"
            + ");";

    public static final String CREATE_VIDEOS = "CREATE TABLE IF NOT EXISTS videos (\n"
            + " id text PRIMARY KEY, \n"
            + " uploader_id text NOT NULL, \n"
            + " videoPath text NOT NULL, \n"
            + " chunks integer NOT NULL, \n"
            + " length real NOT NULL, \n"
            + " height integer NOT NULL, \n"
            + " width integer NOT NULL, \n"
            + " framerate real NOT NULL, \n"
            + " FOREIGN KEY(uploader_id) REFERENCES uploaders(id) ON DELETE CASCADE\n"
            + ");";

}
