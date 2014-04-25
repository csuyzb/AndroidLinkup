package com.znv.linkup;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.znv.linkup.base.config.GameCfg;
import com.znv.linkup.base.config.LevelCfg;
import com.znv.linkup.base.config.RankCfg;
import com.znv.linkup.base.db.DbScore;
import com.znv.linkup.base.db.LevelScore;
import com.znv.linkup.base.sound.GameMusic;
import com.znv.linkup.util.CacheUtil;
import com.znv.linkup.util.ShortcutUtil;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.GameTitle;
import com.znv.linkup.view.dialog.AlertDialog;
import com.znv.linkup.view.dialog.ConfirmDialog;

public class WelcomeActivity extends FullScreenActivity {

    private long exitTime = 0;

//    private ImageFallView fallImages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initShortcut();

        setContentView(R.layout.activity_welcome);

        initMusic();

        loadCfgs();

        initAnimation();
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

//        if (fallImages == null) {
//            LinearLayout container = (LinearLayout) findViewById(R.id.container);
//            fallImages = new ImageFallView(this);
//            fallImages.setAlpha(0.5f);
//            container.addView(fallImages);
//        } else {
//            fallImages.resume();
//        }
    }

    private void unInitAnimation() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.stopAnimation();

//        if (fallImages != null) {
//            fallImages.pause();
//        }
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

    private void initMusic() {
        GameMusic.getInstance().init(this);
    }

    private void loadCfgs() {
        if (rankCfgs == null) {
            XmlResourceParser xrp = getResources().getXml(R.xml.gamecfg);
            GameCfg gameCfg = new GameCfg(xrp);
            rankCfgs = gameCfg.getRankInfos();
            levelCfgs = new HashMap<String, LevelCfg>();
            for (RankCfg rankCfg : rankCfgs) {
                for (LevelCfg levelCfg : rankCfg.getLevelInfos()) {
                    levelCfgs.put(levelCfg.getLevelId(), levelCfg);
                }
            }

            int menuWidth = BitmapFactory.decodeResource(getResources(), R.drawable.menu).getWidth();
            LevelCfg.globalCfg.setMenuWidth(menuWidth);
        }

        loadLevelScores();
    }

    public void loadLevelScores() {
        // 初始化数据库
        DbScore.init(this, rankCfgs);

        List<LevelScore> levelScores = DbScore.selectAll();
        for (LevelScore score : levelScores) {
            String levelId = score.getLevel();
            if (levelCfgs.containsKey(levelId)) {
                LevelCfg cfg = levelCfgs.get(levelId);
                if (cfg != null) {
                    cfg.setMaxScore(score.getMaxScore());
                    cfg.setActive(score.getIsActive() == 1);
                    cfg.setLevelStar(score.getStar());
                }
            }
        }
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
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.getToast(this, R.string.back_again).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
