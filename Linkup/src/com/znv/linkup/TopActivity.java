package com.znv.linkup;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import cn.smssdk.gui.CommonDialog;

import com.android.volley.VolleyError;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.ModeCfg;
import com.znv.linkup.rest.NetMsgListener;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.rest.VolleyHelper;
import com.znv.linkup.util.CacheUtil;
import com.znv.linkup.util.LevelUtil;
import com.znv.linkup.util.LikeHelper;
import com.znv.linkup.util.ShareUtil;
import com.znv.linkup.util.StringUtil;
import com.znv.linkup.view.dialog.InfoDialog;

/**
 * 排行榜
 * 
 * @author yzb
 * 
 */
public class TopActivity extends Activity implements OnGestureListener {

    private int curMode = 0;
    private int curRank = 0;
    private int curLevel = 0;
    private int levelIndex = 0;
    private VolleyHelper volley = null;
    private LinearLayout topList = null;
    private List<TopItemHolder> holders = null;
    private List<ModeCfg> modeCfgs = BaseActivity.modeCfgs;

    private Spinner spModes = null;
    private Spinner spRanks = null;
    private Spinner spLevels = null;
    private ShareUtil shareHelper = null;
    private Dialog pd;

    // 移动最小距离
    private static final int FLING_MIN_DISTANCE = 50;
    // 构建手势探测器
    private GestureDetector mygesture = null;

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
            item.ivLike = (ImageView) view.findViewById(R.id.ivLike);
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

        shareHelper = new ShareUtil(this);

        // 设置查询条件
        setSelectItem();

        LikeHelper.loadLikeUsers(this);

        mygesture = new GestureDetector(this, this);

        initInfoDialog();
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
                    String[] types = TopActivity.this.getResources().getStringArray(ViewSettings.ModeRanks[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TopActivity.this, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRanks.setAdapter(adapter);
                    spLevels.setVisibility(View.INVISIBLE);
                    if (curRank == 0) {
                        getTotalRanks();
                    }
                } else if (curMode != position - 1) {
                    curMode = position - 1;
                    spLevels.setVisibility(View.VISIBLE);
                    // // 设置下拉列表风格
                    String[] ranks = TopActivity.this.getResources().getStringArray(ViewSettings.ModeRanks[position]);
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
            if (isTotalRank()) {
                if (curRank != position) {
                    curRank = position;
                    getTotalRanks();
                }
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
                hideProgress();
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
                        item.tvDate.setTextSize(13);
                        // 是否点赞
                        item.ivLike.setTag(obj.getString("userId") + ";" + item.tvName.getText());
                        if (LikeHelper.isLikeUser(obj.getString("userId"))) {
                            item.ivLike.setImageResource(R.drawable.like);
                        } else {
                            item.ivLike.setImageResource(R.drawable.unlike);
                        }

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
                hideProgress();
                Toast.makeText(TopActivity.this, getString(R.string.top_net_error), Toast.LENGTH_SHORT).show();
            }
        });

        showProgress();

    }

    /**
     * 隐藏进度条
     */
    private void hideProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 显示进度条
     */
    private void showProgress() {
        hideProgress();

        pd = CommonDialog.ProgressDialog(this);
        if (pd != null) {
            pd.show();
        }
    }

    /**
     * 获取当前的总排名，所有/本月/本周
     */
    private void getTotalRanks() {
        volley.cancelAll();
        String rankName = spRanks.getSelectedItem().toString() + "-" + spModes.getSelectedItem().toString();
        ((TextView) findViewById(R.id.tvCurLevel)).setText(rankName);
        String dayString = "";
        if (isTotalRank()) {
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
                hideProgress();
                try {
                    for (int i = 0; i < t.length(); i++) {
                        topList.getChildAt(i).setVisibility(View.VISIBLE);
                        JSONObject obj = t.getJSONObject(i);
                        TopItemHolder item = holders.get(i);
                        setOrder(item.tvOrder, i);
                        item.tvName.setTextSize(15);
                        item.tvName.setText(StringUtil.toUtf8(obj.getString("userName")));
                        item.tvScore.setVisibility(View.GONE);
                        item.tvDate.setText(obj.getString("totalScore"));
                        item.tvDate.setTextSize(15);
                        // 是否点赞
                        item.ivLike.setTag(obj.getString("userId") + ";" + item.tvName.getText());
                        if (LikeHelper.isLikeUser(obj.getString("userId"))) {
                            item.ivLike.setImageResource(R.drawable.like);
                        } else {
                            item.ivLike.setImageResource(R.drawable.unlike);
                        }

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
                hideProgress();
                Toast.makeText(TopActivity.this, getString(R.string.top_net_error), Toast.LENGTH_SHORT).show();
            }
        });
        showProgress();
    }

    /**
     * 处理点赞
     * 
     * @param v
     */
    public void clickLike(View v) {
        String tag = (String) v.getTag();
        String userId = tag.substring(0, tag.indexOf(";"));
        if (!LikeHelper.isLikeUser(userId)) {
            LikeHelper.likeUser(this, userId);
            ((ImageView) v).setImageResource(R.drawable.like);

            UserScore.updateLike(tag, 1, netMsgHandler);
        } else {
            LikeHelper.unlikeUser(this, userId);
            ((ImageView) v).setImageResource(R.drawable.unlike);

            UserScore.updateLike(tag, -1, netMsgHandler);
        }
    }

    /**
     * 获取下一关的排名
     */
    public void nextLevel() {
        if (isTotalRank()) {
            return;
        }
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
    public void preLevel() {
        if (isTotalRank()) {
            return;
        }
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
     * 判断是否为总排行榜
     * 
     * @return 总排行榜：true
     */
    private boolean isTotalRank() {
        return curMode == -1;
    }

    /**
     * 处理网络消息回调的handler
     */
    @SuppressLint("HandlerLeak")
    public Handler netMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ViewSettings.MSG_UPDATE_LIKE: {
                String liketag = (String) msg.obj;
                String likeStr = liketag.substring(0, liketag.indexOf(";"));
                String tag = liketag.substring(liketag.indexOf(";") + 1);
                String userName = tag.substring(tag.indexOf(";") + 1);
                if (likeStr.startsWith("-")) {
                    // 取消点赞
                    Toast.makeText(TopActivity.this, getString(R.string.unlike_info, userName), Toast.LENGTH_SHORT).show();
                } else {
                    // 点赞
                    Toast.makeText(TopActivity.this, getString(R.string.like_info, userName), Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case ViewSettings.MSG_NETWORK_EXCEPTION: {
                Toast.makeText(TopActivity.this, getString(R.string.like_error), Toast.LENGTH_SHORT).show();
            }
                break;
            }
        }
    };

    /**
     * 分享排行榜
     * 
     * @param v
     */
    public void shareTop(View v) {
        if (shareHelper != null) {
            shareHelper.shareMsgView(getString(R.string.share_top_info), topList);
        }
    }

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
        ImageView ivLike;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 向右翻
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            nextLevel();
        }
        // 向左翻
        if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE) {
            preLevel();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    /**
     * 分享提示
     */
    private void initInfoDialog() {
        if (!CacheUtil.hasBind(this, "info_share")) {
            InfoDialog info = new InfoDialog(this);
            info.setTitle(getString(R.string.info_prompt));
            info.setMessage(getString(R.string.share_prompt));
            info.setPositiveButton("info_share");
            info.hasLogin(false);
            info.show();
        }
    }
}
