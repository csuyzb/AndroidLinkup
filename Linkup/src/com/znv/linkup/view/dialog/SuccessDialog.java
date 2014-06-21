package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.AnimatorUtil;
import com.znv.linkup.util.ShareUtil;
import com.znv.linkup.view.LevelTop;

/**
 * 游戏结果确认框
 * 
 * @author yzb
 * 
 */
public class SuccessDialog extends Dialog implements IUpload {

    private GameActivity linkup = null;
    private ResultInfo resultInfo = null;
    private LevelTop levelTop = null;

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

        Button btnShare = (Button) findViewById(R.id.btnshare);
        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = String.format(getContext().getString(R.string.share_score), getContext().getString(R.string.app_name), linkup.getLevelCfg()
                        .getRankName() + "-" + linkup.getLevelCfg().getLevelName(), String.valueOf(resultInfo.getScore()));
                ShareUtil.shareMessage(linkup, msg);
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

        levelTop = (LevelTop) findViewById(R.id.level_top);
        levelTop.setUploadListener(this);
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
     * @param ResultInfo
     *            游戏结果
     */
    public void showDialog(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
        setGameScore(resultInfo.getScore());
        setGameStar(resultInfo.getStars());
        isNewRecord(resultInfo.isNewRecord());
        if (levelTop != null) {
            levelTop.reset();
        }
        uploadScore();
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

    /**
     * 上传分数
     */
    private void uploadScore() {
        // 判断是否已登录
        if (!resultInfo.getUserId().equals("")) {
            if (resultInfo.isNewRecord()) {
                UserScore.addScore(resultInfo.getUserId(), resultInfo.getLevel(), resultInfo.getScore(), levelTop);
            } else {
                if (!resultInfo.isUpload()) {
                    UserScore.addScore(resultInfo.getUserId(), resultInfo.getLevel(), resultInfo.getMaxScore(), levelTop);
                } else {
                    // 获取排行榜
                    UserScore.getTopScores(resultInfo.getLevel(), levelTop);
                }
            }
        } else {
            // 没有登录则提示登录
        }
    }

    @Override
    public void onLoginSuccess(Message msg) {
        UserInfo userInfo = (UserInfo) msg.obj;
        if (userInfo != null) {
            resultInfo.setUserId(userInfo.getUserId());
            uploadScore();
        }
    }

    @Override
    public void onScoreAdd(Message msg) {
        // 更新是否已上传
        linkup.getLevelCfg().setUpload(true);
        LevelScore ls = new LevelScore(resultInfo.getLevel());
        ls.setIsUpload(1);
        DbScore.updateUpload(ls);

        // 获取排行榜
        UserScore.getTopScores(resultInfo.getLevel(), levelTop);
    }

    @Override
    public void onTimeAdd(Message msg) {
    }

    @Override
    public void onAuthorizeClick() {
    }
}
