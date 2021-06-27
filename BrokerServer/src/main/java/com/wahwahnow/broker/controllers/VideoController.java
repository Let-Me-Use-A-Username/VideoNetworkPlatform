package com.wahwahnow.broker.controllers;

import com.wahwahnow.broker.models.VideoDirectory;
import org.apache.commons.codec.binary.Base64;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.RgbToBgr;
import org.jcodec.scale.Transform;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoController {

    public static boolean createVideoDirectory(File tempSource, VideoDirectory videoDirectory) {

        // create users directory and video directory
        try {
            Files.createDirectories(Paths.get(videoDirectory.getVideoPath()));
        } catch (IOException e) {
            return false;
        }
        // fragment video
        boolean failure = false;
        for(int i = 0; i < videoDirectory.getChunks(); ++i) {
            int secOff = i*10;
            String targetName = videoDirectory.getVideoPath()+"/"+secOff+"_"+(secOff+10)+".mp4";
            System.out.println("Video target name is "+targetName);
            File target = new File(targetName);
            if(!fragmentation(tempSource, target, videoDirectory, 10.0f, i*10.0f)) {
                failure = true;
                break;
            }
        }
        // if failure delete everything and tempSource send status to application server
            if(failure){
            // delete stuff
            return false;
        }
        // if success delete tempSource send status to application server
        // delete tempSource
        return true;
    }

    public static boolean fragmentation(File source, File target, VideoDirectory videoDirectory, float duration, float offset){

        try{
            AudioAttributes audio = new AudioAttributes();
            VideoAttributes video = new VideoAttributes();
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);
            attrs.setOffset(offset);
            attrs.setDuration(duration);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs, null);
        }catch (EncoderException e) {
           e.printStackTrace();
            // SystemLogger.Log("Failed to fragment "+videoDirectory.getVideoDetails().getName()+" at offset {"+offset+"}");
            return false;
        }

        return true;
    }

    public static String getBase64Frame(File source, VideoDirectory videoDirectory, long timestamp) throws IOException, JCodecException {

        File trueSource = new File(videoDirectory.getVideoPath()+"/frame.png");
        Picture frame = FrameGrab.getFrameAtSec(source, 2);

        BufferedImage bufferedImage = toBufferedImage(frame);
        ImageIO.write(bufferedImage, "png", trueSource);

        byte[] buffer = new byte[(int) trueSource.length()];

        try{
            FileInputStream fr = new FileInputStream(trueSource);
            fr.read(buffer, 0, (int) trueSource.length());

            String data = "data:image/png;base64, " + Base64.encodeBase64String(buffer);
            writeImage(trueSource, data);
            return data;

        } catch (IOException ignore) { }

        return "";
    }

    private static BufferedImage toBufferedImage(Picture src) {
        if (src.getColor() != ColorSpace.BGR) {
            Picture bgr = Picture.createCropped(src.getWidth(), src.getHeight(), ColorSpace.BGR, src.getCrop());
            if (src.getColor() == ColorSpace.RGB) {
                new RgbToBgr().transform(src, bgr);
            } else {
                Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
                transform.transform(src, bgr);
                new RgbToBgr().transform(bgr, bgr);
            }
            src = bgr;
        }
        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
                BufferedImage.TYPE_3BYTE_BGR);

        if (src.getCrop() == null)
            toBufferedImage2(src, dst);
        else
            toBufferedImageCropped(src, dst);

        return dst;
    }

    private static void toBufferedImage2(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (srcData[i] + 128);
        }
    }

    private static void toBufferedImageCropped(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        int dstStride = dst.getWidth() * 3;
        int srcStride = src.getWidth() * 3;
        for (int line = 0, srcOff = 0, dstOff = 0; line < dst.getHeight(); line++) {
            for (int id = dstOff, is = srcOff; id < dstOff + dstStride; id += 3, is += 3) {
                data[id] = (byte) (srcData[is] + 128);
                data[id + 1] = (byte) (srcData[is + 1] + 128);
                data[id + 2] = (byte) (srcData[is + 2] + 128);
            }
            srcOff += srcStride;
            dstOff += dstStride;
        }
    }


    public static void writeImage(File source, String data){
        try{
            FileWriter wr = new FileWriter(source);
            wr.write(data);
            wr.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
