package com.example.registration.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtility {

    public static byte[] compressImage(byte[] input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        deflater.end();
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] input) throws IOException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        inflater.setInput(input);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        inflater.end();
        return outputStream.toByteArray();
    }

}
