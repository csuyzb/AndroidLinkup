package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.util.AnimatorUtil;

/**
 * 游戏结果确认框
 * 
 * @author yzb
 * 
 */
public class SuccessDialog extends Dialog {

    private GameActivity linkup = null;

    public SuccessDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        this.linkup = linkup;
        setContentView(R.layout.success_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Button btnCancel = (Button) findViewById(R.id.success_button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.onBackPressed();
            }

        });

        Button btnOk = (Button) findViewById(R.id.success_button_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

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
            Button btn = (Button) findViewById(R.id.success_button_cancel);
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
     * @param stars
     *            游戏星级
     */
    public void showDialog(boolean isNewRecord, int stars) {
        setGameScore(linkup.getGameScore(true));
        setGameStar(stars);
        isNewRecord(isNewRecord);
        show();
    }

    /**
     * 设置游戏总得分
     * 
     * @param score
     *            游戏得分
     */
    private void setGameScore(int score) {
        TextView tvScore = (TextView) findViewById(R.id.success_score);
        tvScore.setText(String.valueOf(score));
    }

    /**
     * 设置是否是新的记录
     * 
     * @param isNewRecord
     *            是否为新记录
     */
    private void isNewRecord(boolean isNewRecord) {
        ImageView ivRecord = (ImageView) findViewById(R.id.level_champion);
        ivRecord.setVisibility(isNewRecord ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置游戏星级
     * 
     * @param star
     *            星级数
     */
    private void setGameStar(int star) {
        ImageView level_star1 = (ImageView) findViewById(R.id.level_star1_pass);
        level_star1.setImageResource(R.drawable.pass_fail);
        ImageView level_star2 = (ImageView) findViewById(R.id.level_star2_pass);
        level_star2.setImageResource(R.drawable.pass_fail);
        ImageView level_star3 = (ImageView) findViewById(R.id.level_star3_pass);
        level_star3.setImageResource(R.drawable.pass_fail);

        AnimatorUtil.animAlpha(level_star1, 0, 1, 500, 0);
        AnimatorUtil.animAlpha(level_star2, 0, 1, 1000, 500);
        AnimatorUtil.animAlpha(level_star3, 0, 1, 1500, 1000);

        if (star <= 0 || star > 3) {
            return;
        }
        if (star > 0) {
            if (LevelCfg.globalCfg.getPromptNum() < ViewSettings.PromptMaxNum) {
                // promt 增加一次
                LevelCfg.globalCfg.setPromptNum(LevelCfg.globalCfg.getPromptNum() + 1);
            }
            level_star1.setImageResource(R.drawable.pass_ok);
        }
        if (star > 1) {
            if (LevelCfg.globalCfg.getAddTimeNum() < ViewSettings.AddTimeMaxNum) {
                // AddTime 增加一次
                LevelCfg.globalCfg.setAddTimeNum(LevelCfg.globalCfg.getAddTimeNum() + 1);
            }
            level_star2.setImageResource(R.drawable.pass_ok);
        }
        if (star > 2) {
            if (LevelCfg.globalCfg.getRefreshNum() < ViewSettings.RefreshMaxNum) {
                // refresh 增加一次
                LevelCfg.globalCfg.setRefreshNum(LevelCfg.globalCfg.getRefreshNum() + 1);
            }
            level_star3.setImageResource(R.drawable.pass_ok);
        }
        linkup.setGlobalCfg();
    }
}
