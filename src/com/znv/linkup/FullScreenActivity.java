package com.znv.linkup;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.znv.linkup.base.config.LevelCfg;
import com.znv.linkup.base.config.RankCfg;
import com.znv.linkup.util.CacheUtil;

public class FullScreenActivity extends Activity {

    protected static List<RankCfg> rankCfgs = null;
    protected static Map<String, LevelCfg> levelCfgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFullScreen();

        initBaiduPush();
    }

    private void initFullScreen() {
        // set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // 以apikey的方式绑定
    private void initBaiduPush() {
        if (!CacheUtil.hasBind(getApplicationContext())) {
            // Push: 无账号初始化，用api key绑定
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, CacheUtil.getMetaValue(this, "api_key"));
        }
    }
}
