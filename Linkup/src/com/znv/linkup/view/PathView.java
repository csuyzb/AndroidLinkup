package com.znv.linkup.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.card.Piece;

/**
 * 消除路径
 * 
 * @author yzb
 * 
 */
public class PathView extends View {

    public PathView(Context context) {
        super(context);

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(ViewSettings.PathWidth);
        paint.setColor(getResources().getColor(R.color.path_color));

        alphaAnim.setDuration(500);
        alphaAnim.setAnimationListener(hideAnim);

        setVisibility(View.GONE);
    }

    /**
     * 显示消除路径
     * 
     * @param pieces
     *            路径卡片集合
     */
    public void showLines(List<Piece> pieces) {
        if (pieces == null || pieces.size() < 2) {
            return;
        }
        points = new ArrayList<Point>();
        for (Piece p : pieces) {
            points.add(p.getCenter());
        }

        setVisibility(View.VISIBLE);
        invalidate();
        startAnimation(alphaAnim);
    }

    /**
     * 绘制路径
     */
    protected void onDraw(Canvas canvas) {
        if (points == null || points.size() < 2) {
            return;
        }

        path.reset();
        Point p = points.get(0);
        path.moveTo(p.x, p.y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }

        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    private List<Point> points = null;
    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);

    private AnimationListener hideAnim = new AnimationListener() {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            setVisibility(View.GONE);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    };
}
