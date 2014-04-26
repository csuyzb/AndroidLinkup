package com.znv.linkup.core.util;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

public class ImageUtil {
    private static double COLOR_OFFSET = 128;
    private static Random ran = new Random(System.currentTimeMillis());

    public static Bitmap scaleBitmap(Bitmap bitmap, int newSizeX, int newSizeY) {
        float scaleX = (float) (newSizeX * 1.0 / bitmap.getWidth());
        float scaleY = (float) (newSizeY * 1.0 / bitmap.getHeight());
        return scaleBitmap(bitmap, scaleX, scaleY);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int getRandomColor() {
        byte red = (byte) (COLOR_OFFSET + ((255 - COLOR_OFFSET) * ran.nextDouble()));
        byte green = (byte) (COLOR_OFFSET + ((255 - COLOR_OFFSET) * ran.nextDouble()));
        byte blue = (byte) (COLOR_OFFSET + ((255 - COLOR_OFFSET) * ran.nextDouble()));

        return Color.rgb(red, green, blue);
    }

    public static int getRandomBlueColor() {
        byte blue = (byte) (COLOR_OFFSET + ((255 - COLOR_OFFSET) * ran.nextDouble()));
        return Color.rgb(0, 0, blue);
    }
}
