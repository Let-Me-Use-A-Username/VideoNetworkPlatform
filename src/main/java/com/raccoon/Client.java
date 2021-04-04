package com.raccoon;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket("192.168.1.8", 3001);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            System.out.print("> ");
            String clientInput = input.nextLine();
            System.out.println("\n");

            if(clientInput == null) break;
            if(clientInput.isBlank()) continue;

            try {
                if(out == null || in == null) break;
                out.println(clientInput);
                System.out.println("Response: "+in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(clientInput.equals("stop.")) break;
        }
    }

}
