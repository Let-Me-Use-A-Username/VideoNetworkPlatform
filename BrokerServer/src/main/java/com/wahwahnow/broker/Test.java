package com.wahwahnow.broker;

import com.google.gson.Gson;
import com.wahwahnow.broker.controllers.VideoController;
import com.wahwahnow.broker.models.VideoDirectory;
import com.wahwahnow.broker.routes.VideoRoutes;
import org.jcodec.api.JCodecException;

import java.io.File;
import java.io.IOException;


public class Test {

    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException, JCodecException {

        String videoID = "be7d1b9070cf98cd0559917eb481c461ab6aa45c";
        String base64 = VideoController.getBase64Frame(
                new File("brokerDS_3/temp/da39a3ee5e6b4b0d3255bfef95601890afd80709-be7d1b9070cf98cd0559917eb481c461ab6aa45c.mp4"),
                new VideoDirectory("", "", 0,"brokerDS_3/da39a3ee5e6b4b0d3255bfef95601890afd80709/be7d1b9070cf98cd0559917eb481c461ab6aa45c",0, 0, 0, 0),
                1L
        );
        VideoRoutes.sendImage(videoID, base64);
//        String filepath = "./root/7ae32df5e881e91bba528875c2985cb50b19e581/baa7cc4b59b64fbe619ff3696e068603d603329e";
//
//        System.out.println("Last fragmentID is "+FileReader.getLastFragmentID(filepath));

    }
}
