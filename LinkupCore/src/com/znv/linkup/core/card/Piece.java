package com.znv.linkup.core.card;

import com.znv.linkup.core.GameSettings;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Piece {
    private Bitmap image = null;
    private int imageId = 0;
    private int beginX = 0;
    private int beginY = 0;
    private int indexX = 0;
    private int indexY = 0;
    private int width = 40;
    private int height = 40;
    private boolean isEmpty = false;

    public Piece(int indexY, int indexX) {
        this.indexY = indexY;
        this.indexX = indexX;
    }

    public boolean isSameImage(Piece other) {
        if (image == null || other.image == null) {
            return false;
        }
        return getImageId() == other.getImageId();
    }

    public Point getCenter() {
        return new Point(beginX + width / 2, beginY + height / 2);
    }

    public void exchange(Piece other) {
        Bitmap bm = getImage();
        setImage(other.getImage());
        other.setImage(bm);

        int imageId = getImageId();
        setImageId(other.getImageId());
        other.setImageId(imageId);

        boolean isEmpty = isEmpty();
        setEmpty(other.isEmpty());
        other.setEmpty(isEmpty);
    }

    public static boolean hasImage(Piece piece) {
        return piece != null && !piece.isEmpty() && piece.getImageId() != GameSettings.GroundCardValue && piece.getImageId() != GameSettings.EmptyCardValue;
    }

//    public static boolean canThrough(Piece piece) {
//        return !hasImage(piece);
//    }

    public static boolean canSelect(Piece piece) {
        return hasImage(piece) && piece.getImageId() != GameSettings.ObstacleCardValue;
    }

    public static int getDistance(Piece p1, Piece p2) {
        return Math.abs(p1.getIndexX() - p2.getIndexX()) + Math.abs(p1.getIndexY() - p2.getIndexY());
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    private boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
