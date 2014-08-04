package com.znv.linkup;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.ModeCfg;
import com.znv.linkup.rest.NetMsgListener;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.rest.VolleyHelper;
import com.znv.linkup.util.LevelUtil;
import com.znv.linkup.util.StringUtil;

/**
 * 排行榜
 * 
 * @author yzb
 * 
 */
public class TopActivity extends Activity {

    private int curMode = 0;
    private int curRank = 0;
    private int curLevel = 0;
    private int levelIndex = 0;
    private static int[] modeRanks = new int[] { R.array.totalranks, R.array.mode0ranks, R.array.mode1ranks, R.array.mode2ranks, R.array.mode3ranks };
    private VolleyHelper volley = null;
    private LinearLayout topList = null;
    private List<TopItemHolder> holders = null;
    private List<ModeCfg> modeCfgs = BaseActivity.modeCfgs;

    private Spinner spModes = null;
    private Spinner spRanks = null;
    private Spinner spLevels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFullScreen();
        setContentView(R.layout.activity_top);

        volley = new VolleyHelper(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        topList = (LinearLayout) findViewById(R.id.topUsers);
        holders = new ArrayList<TopItemHolder>(ViewSettings.TopRankN);
        for (int i = 0; i < ViewSettings.TopRankN; i++) {
            View view = inflater.inflate(R.layout.topitem, null);
            TopItemHolder item = new TopItemHolder();
            item.tvOrder = (TextView) view.findViewById(R.id.order);
            item.ivIcon = (ImageView) view.findViewById(R.id.icon);
            item.tvScore = (TextView) view.findViewById(R.id.score);
            item.tvName = (TextView) view.findViewById(R.id.name);
            item.tvDate = (TextView) view.findViewById(R.id.date);
            holders.add(item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 2, 2, 0);
            if (i % 2 == 0) {
                view.setBackgroundColor(0xcccccccc);
            } else {
                view.setBackgroundColor(0xdddddddd);
            }

            view.setLayoutParams(params);
            view.setVisibility(View.INVISIBLE);
            topList.addView(view);
        }
        topList.setVisibility(View.INVISIBLE);

        spModes = (Spinner) findViewById(R.id.spModes);
        spRanks = (Spinner) findViewById(R.id.spRanks);
        spLevels = (Spinner) findViewById(R.id.spLevels);

        // 查询当前关卡排名
        // getLevelRanks();

        // 设置查询条件
        setSelectItem();
    }

    /**
     * 全屏初始化
     */
    private void initFullScreen() {
        // set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置选项卡动作
     */
    private void setSelectItem() {
        spModes.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    curMode = position - 1;
                    String[] types = TopActivity.this.getResources().getStringArray(modeRanks[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TopActivity.this, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRanks.setAdapter(adapter);
                    spLevels.setVisibility(View.INVISIBLE);
                    findViewById(R.id.ivPreLevel).setVisibility(View.INVISIBLE);
                    findViewById(R.id.ivNextLevel).setVisibility(View.INVISIBLE);
                } else if (curMode != position - 1) {
                    curMode = position - 1;
                    spLevels.setVisibility(View.VISIBLE);
                    findViewById(R.id.ivPreLevel).setVisibility(View.VISIBLE);
                    findViewById(R.id.ivNextLevel).setVisibility(View.VISIBLE);
                    // // 设置下拉列表风格
                    String[] ranks = TopActivity.this.getResources().getStringArray(modeRanks[position]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TopActivity.this, android.R.layout.simple_spinner_item, ranks);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 防止触发Rank的ItemSelected事件
                    spRanks.setAdapter(adapter);
                    if (curRank == 0) {
                        getLevelRanks();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spRanks.setOnItemSelectedListener(rankSelectedListener);

        spLevels.setOnItemSelectedListener(levelSelectedListener);
    }

    OnItemSelectedListener rankSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (curMode == -1) {
                curRank = position;
                getTotalRanks();
            } else if (curRank != position) {
                curRank = position;
                getLevelRanks();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    OnItemSelectedListener levelSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (curLevel != position) {
                curLevel = position;
                getLevelRanks();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * 获取当前关卡排名信息
     */
    private void getLevelRanks() {
        volley.cancelAll();
        LevelCfg levelCfg = modeCfgs.get(curMode).getRankInfos().get(curRank).getLevelInfos().get(curLevel);
        String levelName = modeCfgs.get(curMode).getModeName() + "-" + levelCfg.getRankName() + "-" + levelCfg.getLevelName();
        ((TextView) findViewById(R.id.tvCurLevel)).setText(getString(R.string.level_title, levelName));
        levelIndex = levelCfg.getLevelId();
        String uri = UserScore.LEVEL_GET_URI + "?level=" + String.valueOf(levelIndex) + "&top=" + String.valueOf(ViewSettings.TopRankN);
        volley.getJsonArray(uri, new NetMsgListener<JSONArray>() {

            @Override
            public void onNetMsg(JSONArray t) {
                try {
                    for (int i = 0; i < t.length(); i++) {
                        topList.getChildAt(i).setVisibility(View.VISIBLE);
                        JSONObject obj = t.getJSONObject(i);
                        TopItemHolder item = holders.get(i);
                        setOrder(item.tvOrder, i);
                        item.tvName.setTextSize(12);
                        item.tvName.setText(StringUtil.toUtf8(obj.getString("userName")));
                        item.tvScore.setVisibility(View.VISIBLE);
                        if (LevelUtil.isTimeMode(levelIndex)) {
                            item.tvScore.setText(StringUtil.secondToString(obj.getInt("time")));
                        } else {
                            item.tvScore.setText(String.valueOf(obj.getInt("score")));
                        }
                        item.tvDate.setText(obj.getString("pubTime").substring(0, 10));

                        volley.loadImage(item.ivIcon, obj.getString("userIcon"));
                    }
                    for (int i = t.length(); i < ViewSettings.TopRankN; i++) {
                        topList.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.searchMini).setVisibility(View.VISIBLE);
                    topList.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(TopActivity.this, getString(R.string.top_data_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError e) {
                Toast.makeText(TopActivity.this, getString(R.string.top_net_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取当前的总排名，所有/本月/本周
     */
    private void getTotalRanks() {
        volley.cancelAll();
        String rankName = spRanks.getSelectedItem().toString() + "-" + spModes.getSelectedItem().toString();
        ((TextView) findViewById(R.id.tvCurLevel)).setText(rankName);
        String dayString = "";
        if (curMode == -1) {
            if (curRank == 0) {
                dayString = "&day=7";
            } else if (curRank == 1) {
                dayString = "&day=30";
            } else if (curRank == 2) {
            }
        }
        String uri = UserScore.USER_TOTALRANK_URI + "?top=10" + dayString;
        volley.getJsonArray(uri, new NetMsgListener<JSONArray>() {

            @Override
            public void onNetMsg(JSONArray t) {
                try {
                    for (int i = 0; i < t.length(); i++) {
                        topList.getChildAt(i).setVisibility(View.VISIBLE);
                        JSONObject obj = t.getJSONObject(i);
                        TopItemHolder item = holders.get(i);
                        setOrder(item.tvOrder, i);
                        item.tvName.setTextSize(16);
                        item.tvName.setText(StringUtil.toUtf8(obj.getString("userName")));
                        item.tvScore.setVisibility(View.GONE);
                        item.tvDate.setText(obj.getString("totalScore"));

                        volley.loadImage(item.ivIcon, obj.getString("userIcon"));
                    }
                    for (int i = t.length(); i < ViewSettings.TopRankN; i++) {
                        topList.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.searchMini).setVisibility(View.VISIBLE);
                    topList.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(TopActivity.this, getString(R.string.top_data_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError e) {
                Toast.makeText(TopActivity.this, getString(R.string.top_net_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取下一关的排名
     */
    public void nextLevel(View v) {
        if (curMode == modeCfgs.size() - 1 && curRank == modeCfgs.get(curMode).getRankInfos().size() - 1 && curLevel == 23) {
            return;
        }

        if (curLevel + 1 < 24) {
            curLevel++;
            spLevels.setSelection(curLevel);
        } else {
            curLevel = 0;
            spLevels.setSelection(curLevel);
            if (curRank + 1 < modeCfgs.get(curMode).getRankInfos().size()) {
                curRank++;
                spRanks.setSelection(curRank);
            } else {
                curRank = 0;
                spRanks.setSelection(curRank);
                if (curMode + 1 < modeCfgs.size()) {
                    curMode++;
                } else {
                    curMode = 0;
                }
                spModes.setSelection(curMode);
            }
        }

        getLevelRanks();
    }

    /**
     * 查询前一关的排名
     */
    public void preLevel(View v) {
        if (curMode == 0 && curRank == 0 && curLevel == 0) {
            return;
        }

        if (curLevel - 1 >= 0) {
            curLevel--;
            spLevels.setSelection(curLevel);
        } else {
            curLevel = 23;
            spLevels.setSelection(curLevel);
            if (curRank - 1 >= 0) {
                curRank--;
                spRanks.setSelection(curRank);
            } else {
                if (curMode - 1 >= 0) {
                    curMode--;
                } else {
                    curMode = modeCfgs.size() - 1;
                }
                spModes.setSelection(curMode);
                curRank = modeCfgs.get(curMode).getRankInfos().size() - 1;
                spRanks.setSelection(curRank);
            }
        }

        getLevelRanks();
    }

    /**
     * 处理网络消息回调的handler
     */
    @SuppressLint("HandlerLeak")
    public Handler netMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ViewSettings.MSG_LEVEL_GET: {
                try {
                    String result = (String) msg.obj;
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        topList.getChildAt(i).setVisibility(View.VISIBLE);
                        JSONObject obj = array.getJSONObject(i);
                        TopItemHolder item = holders.get(i);
                        setOrder(item.tvOrder, i);
                        item.tvName.setText(StringUtil.toUtf8(obj.getString("userName")));
                        if (LevelUtil.isTimeMode(levelIndex)) {
                            item.tvScore.setText(StringUtil.secondToString(obj.getInt("time")));
                        } else {
                            item.tvScore.setText(String.valueOf(obj.getInt("score")));
                        }
                        item.tvDate.setText(obj.getString("pubTime").substring(0, 10));

                        volley.loadImage(item.ivIcon, obj.getString("userIcon"));
                    }
                    for (int i = array.length(); i < ViewSettings.TopRankN; i++) {
                        topList.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.searchMini).setVisibility(View.VISIBLE);
                    topList.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(TopActivity.this, getString(R.string.top_data_error), Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case ViewSettings.MSG_NETWORK_EXCEPTION: {
                Toast.makeText(TopActivity.this, getString(R.string.top_net_error), Toast.LENGTH_SHORT).show();
            }
                break;
            }
        }
    };

    /**
     * 设置排名列
     * 
     * @param tvOrder
     *            排名列的TextView
     * @param index
     *            排名
     */
    private void setOrder(TextView tvOrder, int index) {
        if (index == 0) {
            tvOrder.setBackgroundResource(R.drawable.gold);
        } else if (index == 1) {
            tvOrder.setBackgroundResource(R.drawable.silver);
        } else if (index == 2) {
            tvOrder.setBackgroundResource(R.drawable.bronze);
        } else {
            tvOrder.setText(String.valueOf(index + 1));
        }
    }

    /**
     * 控件缓存
     * 
     * @author yzb
     * 
     */
    class TopItemHolder {
        TextView tvOrder;
        ImageView ivIcon;
        TextView tvScore;
        TextView tvName;
        TextView tvDate;
        TextView tvTime;
    }
}
