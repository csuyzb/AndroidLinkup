package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.ShareUtil;
import com.znv.linkup.view.LevelTop;
import com.znv.linkup.view.LevelTop.LevelTopStatus;

/**
 * 计时模式结果
 * 
 * @author yzb
 * 
 */
public class TaskDialog extends Dialog implements IUpload {

    private GameActivity linkup = null;
    private ResultInfo resultInfo = null;
    private LevelTop levelTop = null;

    public TaskDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        this.linkup = linkup;
        setContentView(R.layout.task_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        TextView btnBack = (TextView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.onBackPressed();
            }

        });

        TextView btnShare = (TextView) findViewById(R.id.btnshare);
        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 分享
                String msg = String.format(getContext().getString(R.string.share_task), getContext().getString(R.string.app_name), linkup.getLevelCfg()
                        .getRankName() + "-" + linkup.getLevelCfg().getLevelName(), String.valueOf(resultInfo.getScore()));
                if (levelTop.getTopStatus() == LevelTopStatus.TopInfo) {
                    View topMain = levelTop.findViewById(R.id.level_top_main);
                    // 带截图分享
                    if (topMain != null) {
                        ShareUtil.shareMsgView(linkup, msg, topMain);
                    } else {
                        ShareUtil.shareMsgView(linkup, msg, levelTop);
                    }
                } else {
                    ShareUtil.shareMessage(linkup, msg);
                }
            }
        });

        levelTop = (LevelTop) findViewById(R.id.task_top);
        levelTop.setUploadListener(this);
    }

    /**
     * 处理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TextView btn = (TextView) findViewById(R.id.btnBack);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示游戏胜利对话框
     * 
     * @param resultInfo
     *            游戏结果
     */
    public void showDialog(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
        TextView tvScore = (TextView) findViewById(R.id.success_score);
        tvScore.setText(String.valueOf(resultInfo.getScore()));
        TextView tvTask = (TextView) findViewById(R.id.task_score);
        tvTask.setText(String.valueOf(linkup.getLevelCfg().getTask()));

        TextView tvTaskTitle = (TextView) findViewById(R.id.dialog_title);
        tvTaskTitle.setText(R.string.game_task_fail);
        ImageView ivRecord = (ImageView) findViewById(R.id.task_pass);
        ivRecord.setVisibility(View.INVISIBLE);
        TextView btnAgainOrNext = (TextView) findViewById(R.id.btnAgainOrNext);
        Drawable drawableAgain = getContext().getResources().getDrawable(R.drawable.again);
        drawableAgain.setBounds(0, 0, drawableAgain.getMinimumWidth(), drawableAgain.getMinimumHeight());
        btnAgainOrNext.setCompoundDrawables(null, drawableAgain, null, null);
        btnAgainOrNext.setText(R.string.again);
        btnAgainOrNext.setOnClickListener(againHandler);
        if (resultInfo.isNewRecord()) {
            tvTaskTitle.setText(R.string.game_task_success);
            ivRecord.setVisibility(View.VISIBLE);
            Drawable drawableNext = getContext().getResources().getDrawable(R.drawable.next);
            drawableNext.setBounds(0, 0, drawableNext.getMinimumWidth(), drawableNext.getMinimumHeight());
            btnAgainOrNext.setCompoundDrawables(null, drawableNext, null, null);
            btnAgainOrNext.setText(R.string.next);
            btnAgainOrNext.setOnClickListener(nextHandler);
        }

        if (levelTop != null) {
            levelTop.reset();
        }
        uploadScore();
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
