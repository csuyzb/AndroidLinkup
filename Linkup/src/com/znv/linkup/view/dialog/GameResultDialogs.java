package com.znv.linkup.view.dialog;

import android.view.View;
import android.view.View.OnClickListener;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;

/**
 * 游戏结果对话框，包括胜利和失败
 * 
 * @author yzb
 * 
 */
public class GameResultDialogs {

    private GameActivity linkup;
    private ConfirmDialog lostDialog;
    private ResultDialog successDialog;

    public GameResultDialogs(final GameActivity linkup) {
        this.linkup = linkup;

        lostDialog = createLostDialog(linkup.getString(R.string.game_level_lost), "Lost", R.drawable.fail);
        lostDialog.setNegativeButton(linkup.getString(R.string.game_level_lost_cancel), new OnClickListener() {

            @Override
            public void onClick(View v) {
                linkup.onBackPressed();
            }
        });
        lostDialog.setPositiveButton(linkup.getString(R.string.game_level_lost_ok), new OnClickListener() {
            @Override
            public void onClick(View v) {
                linkup.start();
            }
        });

        successDialog = createSuccessDialog(linkup.getString(R.string.game_level_success), "Success", R.drawable.success);
        successDialog.setNegativeButton(linkup.getString(R.string.game_level_success_cancel), new OnClickListener() {
            @Override
            public void onClick(View v) {
                linkup.onBackPressed();
            }
        });

        successDialog.setPositiveButton(linkup.getString(R.string.game_level_success_ok), new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                linkup.next();
            }
        });
    }

    /**
     * 游戏失败
     */
    public void lost() {
        String lostMsg = linkup.getGameResult(false) + getResultInfo(true);
        lostDialog.setMessage(lostMsg);
        lostDialog.show();
    }

    /**
     * 游戏胜利
     * 
     * @param stars
     *            游戏星级
     * @param isNewRecord
     *            是否为新记录
     */
    public void success(int stars, boolean isNewRecord) {
        String successMsg = linkup.getGameResult(true) + getResultInfo(false);
        successDialog.setMessage(successMsg);
        successDialog.setGameStar(stars);
        successDialog.isNewRecord(isNewRecord);
        successDialog.show();
    }

    /**
     * 创建胜利对话框
     * 
     * @param title
     * @param msg
     * @param resId
     * @return
     */
    private ResultDialog createSuccessDialog(String title, String msg, int resId) {
        ResultDialog dialog = new ResultDialog(linkup);
        dialog.setTitle(title).setMessage(msg).setIcon(resId);
        return dialog;
    }

    /**
     * 创建失败对话框
     * 
     * @param title
     * @param msg
     * @param resId
     * @return
     */
    private ConfirmDialog createLostDialog(String title, String msg, int resId) {
        ConfirmDialog dialog = new ConfirmDialog(linkup);
        dialog.setTitle(title).setMessage(msg).setIcon(resId);
        return dialog;
    }

    /**
     * 根据胜利与否获取提示信息
     * 
     * @param isLost
     *            是否失败
     * @return 提示信息
     */
    private String getResultInfo(boolean isLost) {
        return isLost ? linkup.getString(R.string.game_level_lost_ask) : linkup.getString(R.string.game_level_success_ask);
    }
}
