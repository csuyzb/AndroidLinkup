package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;

/**
 * 计时模式结果
 * 
 * @author yzb
 * 
 */
public class TaskDialog extends Dialog {

    private GameActivity linkup = null;

    public TaskDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        this.linkup = linkup;
        setContentView(R.layout.task_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.onBackPressed();
            }

        });
    }

    /**
     * 处理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Button btn = (Button) findViewById(R.id.btnBack);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示游戏胜利对话框
     * 
     * @param isNewRecord
     *            是否是新记录
     */
    public void showDialog(boolean isNewRecord) {
        int score = linkup.getGameScore(true);
        TextView tvScore = (TextView) findViewById(R.id.success_score);
        tvScore.setText(String.valueOf(score));
        TextView tvTask = (TextView) findViewById(R.id.task_score);
        tvTask.setText(String.valueOf(linkup.getLevelCfg().getTask()));

        ImageView ivRecord = (ImageView) findViewById(R.id.task_pass);
        ivRecord.setVisibility(View.INVISIBLE);
        Button btnAgainOrNext = (Button) findViewById(R.id.btnAgainOrNext);
        btnAgainOrNext.setBackgroundResource(R.drawable.again);
        btnAgainOrNext.setOnClickListener(againHandler);
        if (isNewRecord) {
            ivRecord.setVisibility(View.VISIBLE);
            btnAgainOrNext.setBackgroundResource(R.drawable.next);
            btnAgainOrNext.setOnClickListener(nextHandler);
        }

        show();
    }

    private View.OnClickListener againHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            cancel();
            linkup.start();
        }
    };

    private View.OnClickListener nextHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            cancel();
            linkup.next();
        }
    };
}
