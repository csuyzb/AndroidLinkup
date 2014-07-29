package com.znv.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 选择模式界面
 * 
 * @author yzb
 * 
 */
public class ModeActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        findViewById(R.id.mode0).setOnClickListener(this);
        findViewById(R.id.mode1).setOnClickListener(this);
        findViewById(R.id.mode2).setOnClickListener(this);
        findViewById(R.id.mode3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.mode0:
        case R.id.mode1:
        case R.id.mode2:
        case R.id.mode3:
            soundMgr.select();
            int modeIndex = Integer.parseInt((String) v.getTag());
            if (modeIndex >= 0 && modeIndex < 4) {
                Intent intent = new Intent(this, RankActivity.class);
                intent.putExtra("modeIndex", modeIndex);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    protected void playMusic() {

    }

    @Override
    protected void stopMusic() {

    }
}
