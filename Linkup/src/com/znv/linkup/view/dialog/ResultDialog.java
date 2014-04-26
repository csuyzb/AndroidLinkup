package com.znv.linkup.view.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.znv.linkup.R;

public class ResultDialog extends ConfirmDialog {

    public ResultDialog(Context context) {
        super(context);
        setContentView(R.layout.result_dialog);
    }

    public void isNewRecord(boolean isNewRecord) {
        ImageView ivRecord = (ImageView) findViewById(R.id.level_champion);
        ivRecord.setAlpha(isNewRecord ? 1f : 0f);
    }

    public void setGameStar(int star) {
        RatingBar rbStar = (RatingBar) findViewById(R.id.level_rating);
        rbStar.setRating(star);
    }

}
