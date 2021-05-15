package com.wahwahnow;

import java.net.URI;
import java.util.Scanner;

public class CLI implements Runnable{

    private Scanner input;
    private boolean continueProgram;
    private static String serverFile = "./build/resources/main/server_address";

    public CLI(){
        ClientData.getInstance().setJWT(Utils.getServerURL(serverFile));
        continueProgram = true;
        input = new Scanner(System.in);
    }

    public void run(){
        int choice = -1;
        while (continueProgram){
            printMenu();
            choice = readInput("> ");
            switch (choice){
                case 1:
                    getLatest();
                case 2:
                    searchUser();
                case 3:
                    uploadVideo();
                case 4:
                    downloadVideo();
                case 5:
                    checkYourVideos();
                case 6:
                    deleteVideo();
                case 7:
                    login();
                case 8:
                    register();
                case 0:
                    continueProgram = false;
            }
        }
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

    }

    /**
     * Menu option: 8
     * Register
     */
    private void register(){

    }


    private void printMenu(){
        System.out.println("""
                1. Check latest videos
                2. Search user
                3. Upload video
                4. Stream video
                5. Check your videos
                6. Delete video
                7. Login
                8. Create channel
                0. Exit
                """);
    }

    public int readInput(String prompt){
        System.out.print(prompt);
        String in = input.nextLine();
        try{
            return Integer.parseInt(in);
        }catch (Exception i){
            return -1;
        }
    }

    public static void main(String[] args){
        new Thread(new CLI()).start();
    }

}
