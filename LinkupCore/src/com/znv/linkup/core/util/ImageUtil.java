package com.znv.linkup.core.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 图片缩放处理类
 * 
 * @author yzb
 * 
 */
public class ImageUtil {

    /**
     * 根据大小缩放图片
     * 
     * @param bitmap
     *            当前图片
     * @param newSizeX
     *            缩放后的宽度
     * @param newSizeY
     *            缩放后的高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newSizeX, int newSizeY) {
        float scaleX = (float) (newSizeX * 1.0 / bitmap.getWidth());
        float scaleY = (float) (newSizeY * 1.0 / bitmap.getHeight());
        return scaleBitmap(bitmap, scaleX, scaleY);
    }

    /**
     * 根据比例缩放图片
     * 
     * @param bitmap
     *            当前图片
     * @param scaleX
     *            宽度缩放比例
     * @param scaleY
     *            高度缩放比例
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 将图片转化为圆形图片
     * 
     * @param bitmap
     *            原图
     * @return 圆形图
     */
    public static Bitmap roundBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
