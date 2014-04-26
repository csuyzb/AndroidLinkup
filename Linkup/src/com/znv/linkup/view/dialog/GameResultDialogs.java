package com.znv.linkup.view.dialog;

import android.view.View;
import android.view.View.OnClickListener;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;

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

    public void lost() {
        String lostMsg = linkup.getGameResult(false) + getResultInfo(true);
        lostDialog.setMessage(lostMsg);
        lostDialog.show();
    }

    public void success(int stars, boolean isNewRecord) {
        String successMsg = linkup.getGameResult(true) + getResultInfo(false);
        successDialog.setMessage(successMsg);
        successDialog.setGameStar(stars);
        successDialog.isNewRecord(isNewRecord);
        successDialog.show();
    }

    private ResultDialog createSuccessDialog(String title, String msg, int resId) {
        ResultDialog dialog = new ResultDialog(linkup);
        dialog.setTitle(title).setMessage(msg).setIcon(resId);
        return dialog;
    }

    private ConfirmDialog createLostDialog(String title, String msg, int resId) {
        ConfirmDialog dialog = new ConfirmDialog(linkup);
        dialog.setTitle(title).setMessage(msg).setIcon(resId);
        return dialog;
    }

    private String getResultInfo(boolean isLost) {
        return isLost ? linkup.getString(R.string.game_level_lost_ask) : linkup.getString(R.string.game_level_success_ask);
    }
}
