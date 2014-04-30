package com.znv.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.util.CacheUtil;
import com.znv.linkup.util.ShortcutUtil;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.GameTitle;
import com.znv.linkup.view.dialog.AlertDialog;
import com.znv.linkup.view.dialog.ConfirmDialog;

public class WelcomeActivity extends FullScreenActivity {

    private long exitTime = 0;
    private ImageView ivMusic = null;
    private ImageView ivSound = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initShortcut();

        setContentView(R.layout.activity_welcome);

        initSound();

        initAnimation();
    }

    private void initSound() {
        ivMusic = (ImageView) findViewById(R.id.music);

        setGameMusic();
        ivMusic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (musicServer != null) {
                    musicServer.setBgMisicEnabled(!musicServer.isBgMisicEnabled());
                    setGlobalCfg();
                    setGameMusic();
                }
            }
        });
        ivSound = (ImageView) findViewById(R.id.sound);
        setGameSound();
        ivSound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (musicServer != null) {
                    musicServer.setSoundEnabled(!musicServer.isSoundEnabled());
                    setGlobalCfg();
                    setGameSound();
                }
            }
        });
    }

    private void setGameMusic() {
        if (LevelCfg.globalCfg.isGameBgMusic()) {
            ivMusic.setImageResource(R.drawable.music);
        } else {
            ivMusic.setImageResource(R.drawable.music_d);
        }
    }

    private void setGameSound() {
        if (LevelCfg.globalCfg.isGameSound()) {
            ivSound.setImageResource(R.drawable.sound);
        } else {
            ivSound.setImageResource(R.drawable.sound_d);
        }
    }

    private void initShortcut() {
        ShortcutUtil util = new ShortcutUtil(this);
        if (!CacheUtil.hasBind(this, "short_cut")) {
            util.delShortcut();
            util.addShortcut();
            CacheUtil.setBind(this, "short_cut", true);
        }
    }

    private void initAnimation() {

        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.startAnimation();
    }

    private void unInitAnimation() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.stopAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unInitAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnimation();
    }

    public void startRank() {
        Intent intent = new Intent(WelcomeActivity.this, RankActivity.class);
        startActivity(intent);
    }

    public void startGame(View v) {
        startRank();
    }

    public void startHelp(View v) {
        AlertDialog helper = new AlertDialog(this);
        helper.setTitle(getString(R.string.help));
        helper.setMessage(getString(R.string.help_info));
        helper.setIcon(R.drawable.success);
        helper.show();
    }

    public void exit(View v) {
        ConfirmDialog exit = new ConfirmDialog(this);
        exit.setTitle(getString(R.string.exit));
        exit.setMessage(getString(R.string.exit_info));
        exit.setIcon(R.drawable.fail);
        exit.setPositiveButton(getString(R.string.submit), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exit.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            delayFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void delayFinish() {
        if ((System.currentTimeMillis() - exitTime) > ViewSettings.TwoBackExitInterval) {
            ToastUtil.getToast(this, R.string.back_again).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
