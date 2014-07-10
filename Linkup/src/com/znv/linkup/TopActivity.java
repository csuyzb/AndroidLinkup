package com.znv.linkup;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.znv.linkup.rest.NetMsgListener;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.StringUtil;
import com.znv.linkup.util.VolleyHelper;

public class TopActivity extends Activity {

    private int curLevel = 0;
    private VolleyHelper volley = null;
    private LinearLayout topList = null;
    private List<TopItemHolder> holders = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            // item.tvTime = (TextView) view.findViewById(R.id.time);
            holders.add(item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 2, 0, 0);
            if (i % 2 == 0) {
                view.setBackgroundColor(0xcccccccc);
            } else {
                view.setBackgroundColor(0xdddddddd);
            }

            view.setLayoutParams(params);
            view.setVisibility(View.INVISIBLE);
            topList.addView(view);
        }
        topList.setVisibility(View.GONE);
    }

    public void search(View v) {
        searchCurLevel();
    }

    public void searchPre(View v) {
        if (curLevel > 0) {
            curLevel--;
            searchCurLevel();
        }
    }

    public void searchNext(View v) {
        if (curLevel + 1 < 264) {
            curLevel++;
            searchCurLevel();
        }
    }

    private void searchCurLevel() {
        volley.cancelAll();
        getTopInfos(curLevel);
    }

    public void getTopInfos(final int level) {
        String uri = UserScore.SCORE_GET_URI + "?level=" + String.valueOf(level) + "&top=" + String.valueOf(ViewSettings.TopRankN);
        if (isTimeMode(level)) {
            uri = UserScore.TIME_GET_URI + "?level=" + String.valueOf(level) + "&top=" + String.valueOf(ViewSettings.TopRankN);
        }
        volley.getJsonArray(uri, new NetMsgListener<JSONArray>() {

            @Override
            public void onNetMsg(JSONArray t) {
                try {
                    for (int i = 0; i < t.length(); i++) {
                        topList.getChildAt(i).setVisibility(View.VISIBLE);
                        JSONObject obj = t.getJSONObject(i);
                        TopItemHolder item = holders.get(i);
                        item.tvOrder.setText(String.valueOf(i + 1));
                        item.tvName.setText(StringUtil.toUtf8(obj.getString("userName")));
                        if (isTimeMode(level)) {
                            item.tvScore.setText(StringUtil.secondToString(obj.getInt("time")));
                        } else {
                            item.tvScore.setText(String.valueOf(obj.getInt("score")));
                        }
                        item.tvDate.setText(obj.getString("pubTime").substring(0, 10));
                        // item.tvTime.setText(obj.getString("pubTime").substring(11));

                        volley.loadImage(item.ivIcon, obj.getString("userIcon"));
                    }
                    for (int i = t.length(); i < ViewSettings.TopRankN; i++) {
                        topList.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.searchMax).setVisibility(View.GONE);
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

    public boolean isTimeMode(int level) {
        return level >= 120 && level < 192;
    }

    public void showSearchMax(View v) {
        findViewById(R.id.searchMax).setVisibility(View.VISIBLE);
        findViewById(R.id.searchMini).setVisibility(View.GONE);
        topList.setVisibility(View.GONE);
    }

    class TopItemHolder {
        TextView tvOrder;
        ImageView ivIcon;
        TextView tvScore;
        TextView tvName;
        TextView tvDate;
        TextView tvTime;
    }
}
