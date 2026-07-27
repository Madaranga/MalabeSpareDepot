package com.example.malabesparedepot.util;

import javafx.scene.image.Image;

import java.io.*;

public class ImageUtil {
    private static final String IMAGE_RECOURCE_FOLDER = "/images/";
    private ImageUtil() {
    }

    public static Image loadImage(String imagePath) throws FileNotFoundException {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        File imageFile = new File(imagePath.trim());
        if (imageFile.isFile()) {
            return new Image(imageFile.toURI().toString(), true);
        }

        String fileName = new File(imagePath.trim()).getName();
        try (InputStream stream = new FileInputStream(imageFile)) {
            return stream == null ? null : new Image(stream);
        } catch (Exception exception) {
            System.err.println("Error loading image file: " + exception.getMessage());
            return null;
        }

    }
}
