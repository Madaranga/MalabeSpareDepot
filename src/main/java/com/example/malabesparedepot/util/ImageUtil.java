package com.example.malabesparedepot.util;

import javafx.scene.image.Image;

import java.io.File;
import java.io.InputStream;

public final class ImageUtil {

    private static final String IMAGE_RESOURCE_FOLDER = "/Images/";

    private ImageUtil() {
    }

    public static Image loadImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        File imageFile = new File(imagePath.trim());
        if (imageFile.isFile()) {
            return new Image(imageFile.toURI().toString(), true);
        }

        String fileName = new File(imagePath.trim()).getName();
        try (InputStream stream = ImageUtil.class.getResourceAsStream(IMAGE_RESOURCE_FOLDER + fileName)) {
            return stream == null ? null : new Image(stream);
        } catch (Exception exception) {
            System.err.println("Unable to load image '" + imagePath + "': " + exception.getMessage());
            return null;
        }
    }
}
