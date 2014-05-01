package com.znv.linkup.view.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.znv.linkup.R;

/**
 * 游戏结果确认框
 * 
 * @author yzb
 * 
 */
public class ResultDialog extends ConfirmDialog {

    public ResultDialog(Context context) {
        super(context);
        setContentView(R.layout.result_dialog);
    }

    /**
     * 设置是否是新的记录
     * 
     * @param isNewRecord
     *            是否为新记录
     */
    public void isNewRecord(boolean isNewRecord) {
        ImageView ivRecord = (ImageView) findViewById(R.id.level_champion);
        ivRecord.setAlpha(isNewRecord ? 1f : 0f);
    }

    /**
     * 设置游戏星级
     * 
     * @param star
     *            星级数
     */
    public void setGameStar(int star) {
        RatingBar rbStar = (RatingBar) findViewById(R.id.level_rating);
        rbStar.setRating(star);
    }

}
