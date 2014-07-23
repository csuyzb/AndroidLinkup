package com.znv.linkup.view;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.znv.linkup.BaseActivity;
import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.WelcomeActivity;
import com.znv.linkup.core.util.ImageUtil;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.rest.VolleyHelper;
import com.znv.linkup.util.LevelUtil;
import com.znv.linkup.util.StringUtil;

/**
 * 登录或排行榜视图
 * 
 * @author yzb
 * 
 */
public class LevelTop extends LinearLayout implements PlatformActionListener {

    /**
     * 登录状态
     * 
     * @author yzb
     * 
     */
    public enum LevelTopStatus {
        None, Login, UserInfo, TopInfo
    }

    public LevelTop(final Context context, AttributeSet attrs) {
        super(context, attrs);

        View v = LayoutInflater.from(context).inflate(R.layout.level_top, null);
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // v.setLayoutParams(params);
        this.addView(v);

        findViewById(R.id.level_tvWeibo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beforeLogin();
                authorize(new SinaWeibo(context));
            }
        });
        findViewById(R.id.level_tvQq).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                beforeLogin();
                authorize(new QZone(context));
            }
        });

        getControls();

        volley = new VolleyHelper(context);
    }

    /**
     * 根据状态显示界面
     */
    private void showStatus(LevelTopStatus status) {
        topStatus = status;
        holder.levelLogin.setVisibility(View.GONE);
        holder.userInfo.setVisibility(View.GONE);
        holder.levelTopUsers.setVisibility(View.GONE);
        switch (topStatus) {
        case Login:
            holder.levelLogin.setVisibility(View.VISIBLE);
            break;
        case UserInfo:
            holder.userInfo.setVisibility(View.VISIBLE);
            break;
        case TopInfo:
            holder.levelTopUsers.setVisibility(View.VISIBLE);
            break;
        case None:
            break;
        }
    }

    /**
     * 重新初始化控件
     */
    public void reset() {
        try {
            ShareSDK.initSDK(getContext());
        } catch (Exception ex) {
        }
        if (BaseActivity.userInfo == null) {
            // 显示登录
            showStatus(LevelTopStatus.Login);
        } else {
            showStatus(LevelTopStatus.None);
        }
        holder.tvgolduser.setText("");
        holder.tvgoldscore.setText("");
        holder.ivgoldIcon.setImageResource(R.drawable.icon_default);
        holder.tvsilveruser.setText("");
        holder.tvsilverscore.setText("");
        holder.ivsilverIcon.setImageResource(R.drawable.icon_default);
        holder.tvthirduser.setText("");
        holder.tvthirdscore.setText("");
        holder.ivthirdIcon.setImageResource(R.drawable.icon_default);
        holder.ivIcon.setImageResource(R.drawable.icon_default);
        holder.tvUser.setText("");
    }

    public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            login(plat);
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            netMsgHandler.sendEmptyMessage(ViewSettings.MSG_AUTH_ERROR);
        }
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            netMsgHandler.sendEmptyMessage(ViewSettings.MSG_AUTH_CANCEL);
        }
    }

    /**
     * 处理网络消息回调的handler
     */
    @SuppressLint("HandlerLeak")
    public Handler netMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ViewSettings.MSG_LEVEL_ADD: {
                if (uploadListener != null) {
                    uploadListener.onLevelResultAdd(msg);
                }
            }
                break;
            case ViewSettings.MSG_LEVEL_GET: {
                showStatus(LevelTopStatus.TopInfo);

                String result = (String) msg.obj;
                try {
                    JSONArray array = new JSONArray(result);
                    if (array.length() > 0) {
                        JSONObject obj = (JSONObject) array.get(0);
                        volley.loadImage(holder.ivgoldIcon, obj.getString("userIcon"));
                        holder.tvgolduser.setText(StringUtil.substring(obj.getString("userName"), ViewSettings.ShowNameLength));
                        int level = obj.getInt("level");
                        if (LevelUtil.isTimeMode(level)) {
                            holder.tvgoldscore.setText(StringUtil.secondToString(obj.getInt("time")));
                        } else {
                            holder.tvgoldscore.setText(obj.getString("score"));
                        }
                        if (array.length() > 1) {
                            JSONObject obj2 = (JSONObject) array.get(1);
                            volley.loadImage(holder.ivsilverIcon, obj2.getString("userIcon"));
                            holder.tvsilveruser.setText(StringUtil.substring(obj2.getString("userName"), ViewSettings.ShowNameLength));
                            if (LevelUtil.isTimeMode(level)) {
                                holder.tvsilverscore.setText(StringUtil.secondToString(obj2.getInt("time")));
                            } else {
                                holder.tvsilverscore.setText(obj2.getString("score"));
                            }
                            if (array.length() > 2) {
                                JSONObject obj3 = (JSONObject) array.get(2);
                                volley.loadImage(holder.ivthirdIcon, obj3.getString("userIcon"));
                                holder.tvthirduser.setText(StringUtil.substring(obj3.getString("userName"), ViewSettings.ShowNameLength));
                                if (LevelUtil.isTimeMode(level)) {
                                    holder.tvthirdscore.setText(StringUtil.secondToString(obj3.getInt("time")));
                                } else {
                                    holder.tvthirdscore.setText(obj3.getString("score"));
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.d("MSG_LEVEL_GET", e.getMessage());
                }
            }
                break;
            case ViewSettings.MSG_LOGIN: {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(json.getString("userId"));
                    userInfo.setUserName(json.getString("userName"));
                    userInfo.setUserGender(json.getString("userGender"));
                    userInfo.setUserIcon(json.getString("userIcon"));
                    userInfo.setDiamond(getContext(), json.getInt("diamond"));
                    userInfo.setGold(getContext(), json.getInt("gold"));
                    userInfo.setAward(json.getInt("isAward") == 1);
                    userInfo.setTotalRank(json.getInt("totalRank"));

                    BaseActivity.userInfo = userInfo;
                    msg.obj = userInfo;
                    if (uploadListener != null) {
                        uploadListener.onLoginSuccess(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                break;
            case ViewSettings.MSG_IMAGE_GET: {
                Bitmap bm = (Bitmap) msg.obj;
                if (bm != null) {
                    showStatus(LevelTopStatus.UserInfo);

                    Bitmap bmRound = ImageUtil.roundBitmap(ImageUtil.scaleBitmap(bm, ViewSettings.UserImageWidth, ViewSettings.UserImageWidth));
                    WelcomeActivity.userInfo.setUserImage(bmRound);
                    holder.ivIcon.setImageBitmap(bmRound);
                    if (WelcomeActivity.userInfo != null) {
                        holder.tvUser.setText(WelcomeActivity.userInfo.getUserName() + getContext().getString(R.string.user_hello));
                        holder.tvDiamond.setText(String.valueOf(WelcomeActivity.userInfo.getDiamond(getContext())));
                        holder.tvGold.setText(String.valueOf(WelcomeActivity.userInfo.getGold(getContext())));
                        if (WelcomeActivity.userInfo.getTotalRank() == 0) {
                            holder.tvTotalRank.setText(getResources().getString(R.string.total_rank_0));
                        } else {
                            holder.tvTotalRank.setText(String.valueOf(WelcomeActivity.userInfo.getTotalRank()));
                        }
                        String text = getContext().getString(R.string.logining);
                        if (WelcomeActivity.userInfo.isAward()) {
                            text = getContext().getString(R.string.login_award);
                        }
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
                break;
            case ViewSettings.MSG_AUTH_CANCEL: {
                showStatus(LevelTopStatus.Login);
                Toast.makeText(getContext(), R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
                break;
            case ViewSettings.MSG_AUTH_ERROR: {
                showStatus(LevelTopStatus.Login);
                Toast.makeText(getContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
                break;
            case ViewSettings.MSG_NETWORK_EXCEPTION: {
                showStatus(LevelTopStatus.Login);
                Toast.makeText(getContext(), R.string.network_exception, Toast.LENGTH_SHORT).show();
            }
                break;
            }
        }

    };

    public IUpload getUploadListener() {
        return uploadListener;
    }

    public void setUploadListener(IUpload uploadListener) {
        this.uploadListener = uploadListener;
    }

    public LevelTopStatus getTopStatus() {
        return topStatus;
    }

    public void weiboClick() {
        findViewById(R.id.level_tvWeibo).performClick();
    }

    public void qqClick() {
        findViewById(R.id.level_tvQq).performClick();
    }

    /**
     * 更新用户钻石和金币信息
     */
    public void updateUserInfo() {
        if (WelcomeActivity.userInfo != null && WelcomeActivity.userInfo.getUserImage() != null) {
            showStatus(LevelTopStatus.UserInfo);
            holder.ivIcon.setImageBitmap(WelcomeActivity.userInfo.getUserImage());
            holder.tvUser.setText(WelcomeActivity.userInfo.getUserName() + getContext().getString(R.string.user_hello));
            holder.tvDiamond.setText(String.valueOf(WelcomeActivity.userInfo.getDiamond(getContext())));
            holder.tvGold.setText(String.valueOf(WelcomeActivity.userInfo.getGold(getContext())));
            if (WelcomeActivity.userInfo.getTotalRank() == 0) {
                holder.tvTotalRank.setText(getResources().getString(R.string.total_rank_0));
            } else {
                holder.tvTotalRank.setText(String.valueOf(WelcomeActivity.userInfo.getTotalRank()));
            }
        }
    }

    /**
     * 取消所有获取image的网络请求
     */
    public void cancelUrlImages() {
        volley.cancelAll();
    }

    /**
     * 缓存控件，提高效率
     */
    private void getControls() {
        holder.levelLogin = (View) findViewById(R.id.level_login);
        holder.levelTopUsers = (View) findViewById(R.id.level_top_users);
        holder.userInfo = (View) findViewById(R.id.user_info);
        holder.tvgolduser = (TextView) findViewById(R.id.level_tvgolduser);
        holder.tvgoldscore = (TextView) findViewById(R.id.level_tvgoldscore);
        holder.ivgoldIcon = (ImageView) findViewById(R.id.level_ivgoldIcon);
        holder.tvsilveruser = (TextView) findViewById(R.id.level_tvsilveruser);
        holder.tvsilverscore = (TextView) findViewById(R.id.level_tvsilverscore);
        holder.ivsilverIcon = (ImageView) findViewById(R.id.level_ivsilverIcon);
        holder.tvthirduser = (TextView) findViewById(R.id.level_tvthirduser);
        holder.tvthirdscore = (TextView) findViewById(R.id.level_tvthirdscore);
        holder.ivthirdIcon = (ImageView) findViewById(R.id.level_ivthirdIcon);
        holder.ivIcon = (ImageView) findViewById(R.id.ivIcon);
        holder.tvUser = (TextView) findViewById(R.id.tvUser);
        holder.tvDiamond = (TextView) findViewById(R.id.tvDiamond);
        holder.tvGold = (TextView) findViewById(R.id.tvCoin);
        holder.tvTotalRank = (TextView) findViewById(R.id.tvTotalRank);
    }

    private void beforeLogin() {
        try {
            ShareSDK.initSDK(getContext());
        } catch (Exception ex) {
        }
        BaseActivity.soundMgr.select();
    }

    private void authorize(Platform plat) {
        try {
            // 先清除缓存账户
            // plat.getDb().removeAccount();

            if (plat.isValid()) {
                String userId = plat.getDb().getUserId();
                if (!TextUtils.isEmpty(userId)) {
                    login(plat);
                    return;
                }
            }
            plat.setPlatformActionListener(this);
            plat.SSOSetting(true);
            plat.showUser(null);
        } catch (Exception ex) {
            Log.d("LevelTop-authorize", ex.getMessage());
        }
    }

    /**
     * 登录某个平台
     * 
     * @param plat
     *            平台信息
     */
    private void login(Platform plat) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(plat.getDb().getUserId());
        userInfo.setUserName(plat.getDb().getUserName());
        userInfo.setUserGender(plat.getDb().getUserGender());
        userInfo.setUserIcon(plat.getDb().getUserIcon());
        UserScore.login(userInfo, netMsgHandler);
    }

    private IUpload uploadListener = null;
    private levelTopHolder holder = new levelTopHolder();
    private LevelTopStatus topStatus = LevelTopStatus.Login;
    private VolleyHelper volley = null;

    /**
     * 缓存控件，提高效率
     * 
     * @author yzb
     * 
     */
    private class levelTopHolder {
        public View levelLogin;
        public View levelTopUsers;
        public View userInfo;
        public TextView tvgolduser;
        public TextView tvgoldscore;
        public ImageView ivgoldIcon;
        public TextView tvsilveruser;
        public TextView tvsilverscore;
        public ImageView ivsilverIcon;
        public TextView tvthirduser;
        public TextView tvthirdscore;
        public ImageView ivthirdIcon;
        public ImageView ivIcon;
        public TextView tvUser;
        public TextView tvDiamond;
        public TextView tvGold;
        public TextView tvTotalRank;
    }

}
