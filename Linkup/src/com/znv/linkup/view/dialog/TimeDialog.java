package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.util.StringUtil;

/**
 * 计时模式结果
 * 
 * @author yzb
 * 
 */
public class TimeDialog extends Dialog {

    private GameActivity linkup = null;

    public TimeDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        this.linkup = linkup;
        setContentView(R.layout.time_dialog);
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

        Button btnAgain = (Button) findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.start();
            }
        });

        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.next();
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
        int seconds = linkup.getGameTime();
        TextView tvTime = (TextView) findViewById(R.id.success_time);
        tvTime.setText(StringUtil.secondToString(seconds));
        TextView tvRecord = (TextView) findViewById(R.id.time_record);
        tvRecord.setText(StringUtil.secondToString(linkup.getLevelCfg().getMaxScore()));
        ImageView ivRecord = (ImageView) findViewById(R.id.level_champion);
        ivRecord.setVisibility(View.INVISIBLE);
        if (isNewRecord) {
            ivRecord.setVisibility(View.VISIBLE);
        }

        show();
    }
}
