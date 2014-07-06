package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;

/**
 * 游戏结果确认框
 * 
 * @author yzb
 * 
 */
public class FailDialog extends Dialog {

    public FailDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        setContentView(R.layout.fail_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        TextView btnCancel = (TextView) findViewById(R.id.fail_button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.onBackPressed();
            }

        });

        TextView btnOk = (TextView) findViewById(R.id.fail_button_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.start();
            }
        });
    }

    /**
     * 处理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TextView btn = (TextView) findViewById(R.id.fail_button_cancel);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示游戏失败对话框
     * 
     * @param score
     *            游戏分数
     */
    public void showDialog(int score) {
        TextView tvScore = (TextView) findViewById(R.id.fail_score);
        tvScore.setText(String.valueOf(score) + getContext().getString(R.string.score_unit));
        show();
    }
}
