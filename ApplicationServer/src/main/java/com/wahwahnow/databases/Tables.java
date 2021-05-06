package com.wahwahnow.databases;

public class Tables {

    public static final String CREATE_CHANNELS = "CREATE TABLE IF NOT EXISTS channels (\n"
            + "	id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "	email VARCHAR(50), \n"
            + " about VARCHAR(300), \n"
            + " hashtags VARCHAR(150), \n"
            + "	videosUploaded INTEGER, \n"
            + "	dateCreated VARCHAR(20) \n"
            + ");";

    public static final String CREATE_USERS = "CREATE TABLE IF NOT EXISTS users (\n"
            + "	email VARCHAR(50), \n"
            + "	id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "	password VARCHAR(20), \n"
            + "	name VARCHAR(50)\n" //maybe not needed
            + ");";

    public static final String CREATE_USERPROFILEIMAGES = "CREATE TABLE IF NOT EXISTS userProfileImages (\n"
            + "	id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "	imageBase64 VARCHAR(1000) \n"
            + ");";

    public static final String CREATE_VIDEOIMAGEPREVIEWS = "CREATE TABLE IF NOT EXISTS videoImagePreviews (\n"
            + "	videoPreviewID INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "	imageBase64 VARCHAR(1000),\n"
            + "	password VARCHAR(20),\n"
            + "	name VARCHAR(50), \n"
            + " FOREIGN KEY(name) REFERENCES users(name)\n"
            + ");";

    public static final String CREATE_VIDEOS = "CREATE TABLE IF NOT EXISTS videos (\n"
            + "	videoID INTEGER PRIMARY KEY, \n"
            + "	videoPreviewID INTEGER AUTOINCREMENT, \n"
            + "	views INTEGER, \n"
            + "	likes INTEGER, \n"
            + "	id INTEGER AUTOINCREMENT,"
            + "	comments INTEGER,\n"
            + " FOREIGN KEY(videoPreviewID) REFERENCES videoimagepreviews(videoPreviewID),\n"
            + " FOREIGN KEY(id) REFERENCES channels(id)\n"
            + ");";

    public static final String CREATE_LIKES = "CREATE TABLE IF NOT EXISTS likes (\n"
            + "	id INTEGER PRIMARY KEY, \n" //video id
            + "	videoID INTEGER AUTOINCREMENT, \n"
            + " FOREIGN KEY(id) REFERENCES channels(id), \n" //channel id
            + " FOREIGN KEY(videoID) REFERENCES videos(videoID) \n"
            + ");";

    public static final String CREATE_COMMENTS = "CREATE TABLE IF NOT EXISTS comments (\n"
            + "	id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
            + "	videoID INTEGER AUTOINCREMENT, \n"
            + "	comments TEXT, \n"
            + "	timestamp INTEGER,\n"
            + " FOREIGN KEY(id) REFERENCES channels(id)\n"
            + ");";

    public static final String CREATE_HASHTAGS = "CREATE TABLE IF NOT EXISTS hashtags (\n"
            + "	hashtagID VARCHAR(50), \n"
            + "	name VARCHAR(50)\n"
            //Λευτερη μρπο δεν εχω ιδεα πως κανεις MAP μεσα σε βαση, το αφηνω για εσενα <3
            + ");";

}
