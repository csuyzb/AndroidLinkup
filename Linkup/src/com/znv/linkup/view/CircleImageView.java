package com.znv.linkup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.util.ImageUtil;

/**
 * 圆形的Imageview
 * 
 * @author yzb
 * 
 */
public class CircleImageView extends ImageView {

    public interface ILoadImage {
        void onCircleImageLoaded(Bitmap bm);
    }

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap cbm = ImageUtil.roundBitmap(ImageUtil.scaleBitmap(bitmap, ViewSettings.UserImageWidth, ViewSettings.UserImageWidth));
            if (loadImageListener != null) {
                loadImageListener.onCircleImageLoaded(bitmap);
            }
            canvas.drawBitmap(cbm, rect, rect, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setLoadImageListener(ILoadImage listener) {
        loadImageListener = listener;
    }

    private ILoadImage loadImageListener = null;
    private static Paint paint = new Paint();
    private static Rect rect = new Rect(0, 0, ViewSettings.UserImageWidth, ViewSettings.UserImageWidth);
}
