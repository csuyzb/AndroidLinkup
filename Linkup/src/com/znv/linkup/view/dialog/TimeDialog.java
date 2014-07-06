package com.znv.linkup.view.dialog;

import android.app.Dialog;
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
import com.znv.linkup.util.StringUtil;
import com.znv.linkup.view.LevelTop;
import com.znv.linkup.view.LevelTop.LevelTopStatus;

/**
 * 计时模式结果
 * 
 * @author yzb
 * 
 */
public class TimeDialog extends Dialog implements IUpload {

    private GameActivity linkup = null;
    private ResultInfo resultInfo = null;
    private LevelTop levelTop = null;

    public TimeDialog(final GameActivity linkup) {
        super(linkup, R.style.CustomDialogStyle);
        this.linkup = linkup;
        setContentView(R.layout.time_dialog);
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
                String msg = String.format(getContext().getString(R.string.share_time), getContext().getString(R.string.app_name), linkup.getLevelCfg()
                        .getRankName() + "-" + linkup.getLevelCfg().getLevelName(), StringUtil.secondToString(resultInfo.getTime()));
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

        TextView btnAgain = (TextView) findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.start();
            }
        });

        TextView btnNext = (TextView) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                linkup.next();
            }
        });

        levelTop = (LevelTop) findViewById(R.id.time_top);
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
        TextView tvTime = (TextView) findViewById(R.id.success_time);
        tvTime.setText(StringUtil.secondToString(resultInfo.getTime()) + getContext().getString(R.string.time_unit));
        TextView tvRecord = (TextView) findViewById(R.id.time_record);
        tvRecord.setText(StringUtil.secondToString(linkup.getLevelCfg().getMaxScore()) + getContext().getString(R.string.time_unit));
        ImageView ivRecord = (ImageView) findViewById(R.id.level_champion);
        TextView tvDiamond = (TextView) findViewById(R.id.level_diamond_reward);
        tvDiamond.setText("+" + String.valueOf(resultInfo.getStars()));
        TextView tvCoin = (TextView) findViewById(R.id.level_coin_reward);
        tvCoin.setText("+" + String.valueOf(resultInfo.getScore() / 10));
        ivRecord.setVisibility(View.INVISIBLE);
        if (resultInfo.isNewRecord()) {
            ivRecord.setVisibility(View.VISIBLE);
        }

        if (levelTop != null) {
            levelTop.reset();
        }
        uploadTime();

        show();
    }

    /**
     * 上传时间
     */
    private void uploadTime() {
        // 判断是否已登录
        if (!resultInfo.getUserId().equals("")) {
            if (resultInfo.isNewRecord()) {
                UserScore.addTime(resultInfo.getUserId(), resultInfo.getLevel(), resultInfo.getTime(), levelTop.netMsgHandler);
            } else {
                if (!resultInfo.isUpload()) {
                    UserScore.addTime(resultInfo.getUserId(), resultInfo.getLevel(), resultInfo.getMinTime(), levelTop.netMsgHandler);
                } else {
                    // 获取排行榜
                    UserScore.getTopTimes(resultInfo.getLevel(), levelTop.netMsgHandler);
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
            uploadTime();
        }
    }

    @Override
    public void onScoreAdd(Message msg) {
    }

    @Override
    public void onTimeAdd(Message msg) {
        // 更新是否已上传
        linkup.getLevelCfg().setUpload(true);
        LevelScore ls = new LevelScore(resultInfo.getLevel());
        ls.setIsUpload(1);
        DbScore.updateUpload(ls);

        // 获取排行榜
        UserScore.getTopTimes(resultInfo.getLevel(), levelTop.netMsgHandler);
    }

    @Override
    public void onAuthorizeClick() {

    }
}
