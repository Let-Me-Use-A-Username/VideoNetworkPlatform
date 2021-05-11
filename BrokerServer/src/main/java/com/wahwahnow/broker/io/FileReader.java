package com.wahwahnow.broker.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileReader {

    private static Comparator<String> ascending(){
        return (o1, o2) -> {
            String[] partsO1 = o1.split("_");
            String[] partsO2 = o2.split("_");

            int fragmentIDo1 = Integer.parseInt(partsO1[0]);
            int fragmentIDo2 = Integer.parseInt(partsO2[0]);
            if(fragmentIDo1 > fragmentIDo2){
                return 1;
            }else if (fragmentIDo1 < fragmentIDo2){
                return -1;
            }

            return 0;
        };
    }

    private static Comparator<String> descending(){
        return (o1, o2) -> {
            String[] partsO1 = o1.split("_");
            String[] partsO2 = o2.split("_");

            int fragmentIDo1 = Integer.parseInt(partsO1[0]);
            int fragmentIDo2 = Integer.parseInt(partsO2[0]);
            if(fragmentIDo1 < fragmentIDo2){
                return 1;
            }else if (fragmentIDo1 > fragmentIDo2){
                return -1;
            }

            return 0;
        };
    }

    public static int getLastFragmentID(String videoDirectory){

        File directory = new File(videoDirectory);

        List<String> files = Arrays.asList(directory.list());
        files.sort(descending());
        return Integer.parseInt(files.get(0).split("_")[0]);

    }

    public static ArrayList<String> getRemainingFragments(String videoDirectory, int clientLastFragmentID){

        File directory = new File(videoDirectory);

        List<String> files = Arrays.asList(directory.list());
        files.sort(descending());

        ArrayList<String> remainingFragments = new ArrayList<>();

        for(String file: files){
            int fragmentID = Integer.parseInt(file.split("_")[0]);
            if(clientLastFragmentID < fragmentID) remainingFragments.add(file);
        }

        if(!remainingFragments.isEmpty()) remainingFragments.sort(ascending());

        for(String frag: remainingFragments){
            System.out.println(frag);
        }

        return remainingFragments;
    }

    public static boolean exists(String filepath) {
        File f = new File(filepath);
        return f.exists();
    }
}
