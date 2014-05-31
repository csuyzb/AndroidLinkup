package com.znv.linkup.view;

import java.util.Random;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.util.AnimatorUtil;
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
     * 获取与界面卡片关联的piece
     * 
     * @return 与界面卡片关联的piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * 设置与界面卡片关联的piece
     * 
     * @param piece
     *            piece信息
     * @param isAnim
     *            是否应用动画
     */
    public void setPiece(Piece piece, boolean isAnim) {
        this.piece = piece;
        imageView.setImageBitmap(piece.getImage());

        if (isAnim) {
            setXY(piece.getBeginX(), -piece.getHeight());
            // 从上面落下
            Animator anim = ObjectAnimator.ofFloat(this, "translationY", 0, piece.getBeginY() + piece.getHeight());
            anim.setDuration(500);
            anim.setStartDelay((Piece.YSize - piece.getIndexY()) * 50 - ran.nextInt(50));
            anim.start();
        } else {
            // 设置卡片的left和top
            setXY(piece.getBeginX(), piece.getBeginY());
        }

        lineWidth = piece.getWidth() / 16 + 1;
        rect = new RectF(piece.getWidth() / 32, piece.getWidth() / 32, piece.getWidth() - lineWidth + 1, piece.getHeight() - lineWidth + 1);
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
            unPrompt();
            // 解决放大遮挡问题
            this.bringToFront();
            AnimatorUtil.animScale(this, 1f, 1.2f, 1f, 1.2f);
        } else {
            checkedRect.setVisibility(View.INVISIBLE);
            AnimatorUtil.animScale(this, this.getScaleX(), 1f, this.getScaleY(), 1f);
        }
    }

    /**
     * 提示，执行卡片提示动画
     */
    public void prompt() {
        promptRect.setVisibility(View.VISIBLE);
        promptRect.startAnimation(cardNoteAnim);
    }

    /**
     * 取消提示
     */
    public void unPrompt() {
        if (promptRect.getVisibility() != View.VISIBLE) {
            promptRect.setAnimation(null);
            promptRect.setVisibility(View.INVISIBLE);
        }
    }

    private View genBorder(int color) {
        final Paint borderPaint = new Paint();
        borderPaint.setColor(color);
        borderPaint.setStyle(Style.STROKE);

        return new View(getContext()) {

            protected void onDraw(Canvas canvas) {
                borderPaint.setStrokeWidth(lineWidth);
                canvas.drawRoundRect(rect, lineWidth, lineWidth, borderPaint);
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
    private int lineWidth = 1;
    private ImageView imageView = null;
    private View checkedRect = null;
    private View promptRect = null;
    private RectF rect = null;
    private Random ran = new Random(System.currentTimeMillis());
    private CardPromptAnim cardNoteAnim = new CardPromptAnim();
}
