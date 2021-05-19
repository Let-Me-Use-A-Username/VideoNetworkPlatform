package com.wahwahnow;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class CLI implements Runnable{

    private final Scanner input;
    private final Gson gson;
    private boolean continueProgram;
    private final HttpRouter router;
    private Thread fetchMetadataTask;
    private static String serverFile = "./build/resources/main/server_address";

    public CLI(){
        gson = new Gson();
        serverFile = Thread.currentThread().getContextClassLoader().getResource("server_address").getPath();
        ClientData.getInstance().setWebApi(Utils.getServerURL(serverFile));
        router = new HttpRouter();
        continueProgram = true;
        input = new Scanner(System.in);
    }

    public void run(){
        fetchMetadataTask = new Thread(this::updateVideoTags);
        fetchMetadataTask.start();
        int choice = -1;
        while (continueProgram){
            printMenu();
            choice = readInput("> ");
            switch (choice) {
                case 1 -> getLatest();
                case 2 -> searchUser();
                case 3 -> uploadVideo();
                case 4 -> downloadVideo();
                case 5 -> checkYourVideos();
                case 6 -> deleteVideo();
                case 7 -> login();
                case 8 -> register();
                case 9 -> printCategories();
                case 0 -> continueProgram = false;
            }
        }
        fetchMetadataTask.stop();
        System.out.println("Exiting program...");
    }


    /**
     * Menu option: 1
     * Check latest videos
     */
    private void getLatest(){

    }

    /**
     * Menu option: 2
     * Search user's list
     */
    private void searchUser(){

    }

    /**
     * Menu option: 3
     * Upload video
     * (user has to login or create account)
     */
    private void uploadVideo(){

        String absolutePath = readString("Give absolute path of video file: ");
        // send request to application server
        String videoName = readString("Give video name: ");
        printCategories();
        int tag = readInput("Please choose a category: ");
        String description = readString("Give description (can be blank): ");
        // send request
        try{
            HttpResponseData res = router.sendRequest(HttpRouter.POST,
                    ClientData.getInstance().getWebApi().toString()+"/channels/upload",
                    ClientData.getInstance().getJwtHeader(),
                    JsonModels.contentType,
                    JsonModels.uploadVideoJSON(videoName, tag, description)
            );
            System.out.println(res.statusCode+"\n"+res.content);
            switch (res.statusCode){
                case 200 -> {
                    System.out.println("Uploading file...");
                    // success now send data to broker
                    JsonObject jsonObject = JsonParser.parseString(res.content).getAsJsonObject();
                    String brokerAdress = jsonObject.get("uploadServer").getAsString();
                    String artist = jsonObject.get("artist").getAsString();
                    String video = jsonObject.get("video").getAsString();
                    MRMTPClient.uploadVideo(artist, video, brokerAdress, absolutePath);
                }
                case 401 -> {
                    // jwt is expired or not provided
                }
                case 403 -> {
                    // bad request
                }
                default -> {
                    // server fucked up
                    System.out.println("Server failure... Please try again later.");
                }
            }
        }catch (IOException e){ }

    }

    /**
     * Menu option: 4
     * Stream video (Download)
     */
    private void downloadVideo(){

    }

    /**
     * Menu option: 5
     * Check your videos
     * (list with names)
     */
    private void checkYourVideos(){

    }

    /**
     * Menu option: 6
     * Delete video (if logged in)
     */
    private void deleteVideo(){

    }

    /**
     * Menu option: 7
     * Login
     */
    private void login(){
        String email = readString("Enter email: ");
        String password = readString("Enter password: ");
        try {
            HttpResponseData res = router.sendRequest(
                    HttpRouter.POST,
                    ClientData.getInstance().getWebApi().toString()+"/login",
                    null,
                    JsonModels.contentType,
                    JsonModels.loginJSON(email, password)
            );
            switch (res.statusCode){
                case 200 -> {
                    Log("Login successful");
                    // parse jwt
                    List<Object> val = (List<Object>) res.headers.get("jwt");
                    ClientData.getInstance().setJWT((String) val.get(0));
                }
                case 401 -> {
                    Log("Invalid credentials.");
                }
                case 404 -> {
                    Log("User not found");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Menu option: 8
     * Register
     */
    private void register(){
        String channelName = readString("Enter Channel Name: ");
        String email = readString("Enter email address: ");
        String password = readString("Enter password: ");
        try{
            HttpResponseData res = router.sendRequest(
                    HttpRouter.POST,
                    ClientData.getInstance().getWebApi().toString()+"/register",
                    null,
                    JsonModels.contentType,
                    JsonModels.registerJSON(channelName, email, password)
            );
            switch (res.statusCode){
                case 200 ->{
                    Log("Channel has been created. Please login if you want to upload videos.");
                }
                case 404 -> {
                    Log("Duplicate data (email or channelName already exist)");
                }
                case 400 -> {
                    Log("Bad credentials.");
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Menu option: 9
     * Prints video tags
     */
    private void printCategories() {
        StringBuilder sb = new StringBuilder();
        List<VideoTag> list = ClientData.getInstance().getVideoTags();
        list.sort((v1, v2)->{
            if(v1.getId() < v2.getId()){
                return -1;
            } else if(v1.getId() > v2.getId()){
                return 1;
            }
            return 0;
        });
        sb.append("\n\t\tCategories");
        sb.append("\n---------------------------------").append("\n");
        for(VideoTag videoTag: list) sb.append(videoTag.getId()).append(" ").append(videoTag.getName()).append("\n");
        sb.append("---------------------------------\n");
        Log(sb.toString());
    }

    private void Log(String message){
        System.out.println(message);
    }

    private void printMenu(){
        System.out.println("""
                    
                    MAIN MENU
                1. Check latest videos
                2. Search user
                3. Upload video
                4. Stream video
                5. Check your videos
                6. Delete video
                7. Login
                8. Create channel
                9. Check categories
                0. Exit
                """);
    }

    public String readString(String prompt){
        System.out.print(prompt);
        return input.nextLine();
    }

    public int readInput(String prompt){
        String in = readString(prompt);
        try{
            return Integer.parseInt(in);
        }catch (Exception i){
            return -1;
        }
    }

    // Update video tags every 5 minutes
    private void updateVideoTags() {
        while (continueProgram){
            try{
                HttpResponseData res = router.sendRequest(
                        HttpRouter.GET,
                        ClientData.getInstance().getWebApi().toString()+"/video/videoTags",
                        null,
                        "",
                        ""
                );

                JsonObject jsonObject =  JsonParser.parseString(res.content).getAsJsonObject();
                Type listType = new TypeToken<List<VideoTag>>(){}.getType();
                List<VideoTag> videoTagList = gson.fromJson(jsonObject.get("videoTags"), listType);
                ClientData.getInstance().setVideoTags(videoTagList);
            }catch (IOException e){ }
            try{
                Thread.sleep(5 * 60 * 1000);
            }catch (InterruptedException ignored){ }
        }
    }

    public static void main(String[] args){
        new Thread(new CLI()).start();
    }

}
