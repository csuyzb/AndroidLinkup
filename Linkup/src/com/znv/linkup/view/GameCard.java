package com.znv.linkup.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.view.animation.CardPromptAnim;

/**
 * 游戏卡片类，单独控制便于动画和刷新
 * 
 * @author yzb
 * 
 */
public class GameCard extends FrameLayout {

    public GameCard(Context context) {
        super(context);

        // 卡片界面图片
        imageView = new ImageView(getContext());
        addView(imageView, -1, -1);

        // 设置选择边框
        checkedRect = genBorder(getResources().getColor(R.color.check_color));
        addView(checkedRect, -1, -1);
        setChecked(false);

        // 设置提示边框
        promptRect = genBorder(getResources().getColor(R.color.prompt_color));
        addView(promptRect, -1, -1);
        promptRect.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取卡片内部piece信息
     * 
     * @return piece信息
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * 设置与界面卡片关联的piece
     * 
     * @param piece
     *            piece信息
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        imageView.setImageBitmap(piece.getImage());

        // 设置卡片的left和top
        setXY(piece.getBeginX(), piece.getBeginY());

        int lineWidth = ViewSettings.CheckLineWidth;
        rect = new RectF(lineWidth / 2, lineWidth / 2, piece.getWidth() - lineWidth, piece.getHeight() - lineWidth);
    }

    /**
     * 设置卡片是否选中
     * 
     * @param checked
     *            选中为true
     */
    public void setChecked(boolean checked) {
        if (checked) {
            checkedRect.setVisibility(View.VISIBLE);
            stopNoteAnim();
        } else {
            checkedRect.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 提示，执行卡片提示动画
     */
    public void prompt() {
        startNoteAnim();
    }

    /**
     * 取消提示
     */
    public void unPrompt() {
        stopNoteAnim();
    }

    /**
     * 开始提示动画
     */
    private void startNoteAnim() {
        promptRect.setVisibility(View.VISIBLE);
        promptRect.startAnimation(cardNoteAnim);
    }

    /**
     * 停止提示动画
     */
    private void stopNoteAnim() {
        if (promptRect.getVisibility() != View.VISIBLE) {
            promptRect.setAnimation(null);
            promptRect.setVisibility(View.INVISIBLE);
        }
    }

    private View genBorder(int color) {
        final Paint borderPaint = new Paint();
        borderPaint.setColor(color);
        // borderPaint.setShader(linearGradient);
        borderPaint.setStyle(Style.STROKE);
        borderPaint.setStrokeWidth(ViewSettings.CheckLineWidth);

        return new View(getContext()) {

            protected void onDraw(Canvas canvas) {
                canvas.drawRoundRect(rect, ViewSettings.CheckLineWidth, ViewSettings.CheckLineWidth, borderPaint);
                super.onDraw(canvas);
            }
        };
    }

    private void setXY(float x, float y) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) super.getLayoutParams();
        lp.topMargin = (int) y;
        lp.leftMargin = (int) x;
        setLayoutParams(lp);
    }

    private Piece piece = null;
    private ImageView imageView = null;
    private View checkedRect = null;
    private View promptRect = null;
    private RectF rect = null;
    private CardPromptAnim cardNoteAnim = new CardPromptAnim();

    // private static int[] colors = new int[] { Color.RED, Color.YELLOW };
    // private static LinearGradient linearGradient = new LinearGradient(0, 0, 50, 50, colors, null, TileMode.REPEAT);
}
