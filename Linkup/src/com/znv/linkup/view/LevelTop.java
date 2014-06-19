package com.znv.linkup.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
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
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.WelcomeActivity;
import com.znv.linkup.core.util.ImageUtil;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.StringUtil;

/**
 * 登录或排行榜视图
 * 
 * @author yzb
 * 
 */
public class LevelTop extends LinearLayout implements Callback, PlatformActionListener {

    private int imageWidth = 50;
    private IUpload uploadListener = null;

    public LevelTop(final Context context, AttributeSet attrs) {
        super(context, attrs);

        View v = LayoutInflater.from(context).inflate(R.layout.level_top, null);
        this.addView(v);

        findViewById(R.id.level_tvWeibo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                authorize(new SinaWeibo(context));
            }
        });
        findViewById(R.id.level_tvQq).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                authorize(new QZone(context));
            }
        });
    }

    private void authorize(Platform plat) {
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
    }

    public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            login(plat);
        }
    }

    private void login(Platform plat) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPlat(plat.getName());
        userInfo.setPlatVersion(plat.getDb().getPlatformVersion());
        userInfo.setUserId(plat.getDb().getUserId());
        userInfo.setUserName(plat.getDb().getUserName());
        userInfo.setUserGender(plat.getDb().getUserGender());
        userInfo.setUserIcon(plat.getDb().getUserIcon());
        UserScore.login(userInfo, this);
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(ViewSettings.MSG_AUTH_ERROR, this);
        }
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(ViewSettings.MSG_AUTH_CANCEL, this);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case ViewSettings.MSG_SCORE_ADD: {
            if (uploadListener != null) {
                uploadListener.onScoreAdd(msg);
            }
        }
            break;
        case ViewSettings.MSG_TIME_ADD: {
            if (uploadListener != null) {
                uploadListener.onTimeAdd(msg);
            }
        }
            break;
        case ViewSettings.MSG_SCORE_GET: {
            findViewById(R.id.level_login).setVisibility(View.GONE);
            findViewById(R.id.level_top_users).setVisibility(View.VISIBLE);

            List<String> userIds = new ArrayList<String>();
            List<String> urls = new ArrayList<String>();
            String result = (String) msg.obj;
            try {
                JSONArray array = new JSONArray(result);
                if (array.length() > 0) {
                    JSONObject obj = (JSONObject) array.get(0);
                    userIds.add(obj.getString("userId"));
                    urls.add(obj.getString("userIcon"));
                    ((TextView) findViewById(R.id.level_tvgolduser)).setText(obj.getString("userName"));
                    ((TextView) findViewById(R.id.level_tvgoldscore)).setText(obj.getString("score"));
                    if (array.length() > 1) {
                        JSONObject obj2 = (JSONObject) array.get(1);
                        userIds.add(obj2.getString("userId"));
                        urls.add(obj2.getString("userIcon"));
                        ((TextView) findViewById(R.id.level_tvsilveruser)).setText(obj2.getString("userName"));
                        ((TextView) findViewById(R.id.level_tvsilverscore)).setText(obj2.getString("score"));
                        if (array.length() > 2) {
                            JSONObject obj3 = (JSONObject) array.get(2);
                            userIds.add(obj3.getString("userId"));
                            urls.add(obj3.getString("userIcon"));
                            ((TextView) findViewById(R.id.level_tvthirduser)).setText(obj3.getString("userName"));
                            ((TextView) findViewById(R.id.level_tvthirdscore)).setText(obj3.getString("score"));
                        }
                    }
                }
                UserScore.getTopImages(userIds, urls, this);
            } catch (JSONException e) {
                Log.d("MSG_SCORE_GET", e.getMessage());
            }
        }
            break;
        case ViewSettings.MSG_TIME_GET: {
            findViewById(R.id.level_login).setVisibility(View.GONE);
            findViewById(R.id.level_top_users).setVisibility(View.VISIBLE);

            List<String> userIds = new ArrayList<String>();
            List<String> urls = new ArrayList<String>();
            String result = (String) msg.obj;
            try {
                JSONArray array = new JSONArray(result);
                if (array.length() > 0) {
                    JSONObject obj = (JSONObject) array.get(0);
                    userIds.add(obj.getString("userId"));
                    urls.add(obj.getString("userIcon"));
                    ((TextView) findViewById(R.id.level_tvgolduser)).setText(obj.getString("userName"));
                    ((TextView) findViewById(R.id.level_tvgoldscore)).setText(StringUtil.secondToString(Integer.parseInt(obj.getString("time"))));
                    if (array.length() > 1) {
                        JSONObject obj2 = (JSONObject) array.get(1);
                        userIds.add(obj2.getString("userId"));
                        urls.add(obj2.getString("userIcon"));
                        ((TextView) findViewById(R.id.level_tvsilveruser)).setText(obj2.getString("userName"));
                        ((TextView) findViewById(R.id.level_tvsilverscore)).setText(StringUtil.secondToString(Integer.parseInt(obj2.getString("time"))));
                        if (array.length() > 2) {
                            JSONObject obj3 = (JSONObject) array.get(2);
                            userIds.add(obj3.getString("userId"));
                            urls.add(obj3.getString("userIcon"));
                            ((TextView) findViewById(R.id.level_tvthirduser)).setText(obj3.getString("userName"));
                            ((TextView) findViewById(R.id.level_tvthirdscore)).setText(StringUtil.secondToString(Integer.parseInt(obj3.getString("time"))));
                        }
                    }
                }
                UserScore.getTopImages(userIds, urls, this);
            } catch (JSONException e) {
                Log.d("MSG_TIME_GET", e.getMessage());
            }
        }
            break;
        case ViewSettings.MSG_LOGIN: {
            if (uploadListener != null) {
                uploadListener.onLoginSuccess(msg);
            }
        }
            break;
        case ViewSettings.MSG_TOPIMAGES_GET: {
            @SuppressWarnings("unchecked")
            List<Bitmap> images = (List<Bitmap>) msg.obj;
            if (images.size() > 0) {
                if (images.get(0) != null) {
                    ImageView ivgoldIcon = (ImageView) findViewById(R.id.level_ivgoldIcon);
                    ivgoldIcon.setImageBitmap(ImageUtil.scaleBitmap(images.get(0), imageWidth, imageWidth));
                }
                if (images.size() > 1) {
                    if (images.get(1) != null) {
                        ImageView ivsilverIcon = (ImageView) findViewById(R.id.level_ivsilverIcon);
                        ivsilverIcon.setImageBitmap(ImageUtil.scaleBitmap(images.get(1), imageWidth, imageWidth));
                    }

                    if (images.size() > 2) {
                        if (images.get(2) != null) {
                            ImageView ivthirdIcon = (ImageView) findViewById(R.id.level_ivthirdIcon);
                            ivthirdIcon.setImageBitmap(ImageUtil.scaleBitmap(images.get(2), imageWidth, imageWidth));
                        }
                    }
                }
            }
        }
            break;
        case ViewSettings.MSG_IMAGE_GET: {
            Bitmap bm = (Bitmap) msg.obj;
            if (bm != null) {
                findViewById(R.id.level_login).setVisibility(View.GONE);
                findViewById(R.id.user_info).setVisibility(View.VISIBLE);

                ImageView ivIcon = (ImageView) findViewById(R.id.ivIcon);
                ivIcon.setImageBitmap(ImageUtil.roundBitmap(ImageUtil.scaleBitmap(bm, 64, 64)));
                TextView tvUser = (TextView) findViewById(R.id.tvUser);
                if (WelcomeActivity.userInfo != null) {
                    tvUser.setText(WelcomeActivity.userInfo.getUserName() + getContext().getString(R.string.user_hello));
                    String text = getContext().getString(R.string.logining, WelcomeActivity.userInfo.getPlat());
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        }
            break;
        case ViewSettings.MSG_AUTH_CANCEL: {
            Toast.makeText(getContext(), R.string.auth_cancel, Toast.LENGTH_SHORT).show();
        }
            break;
        case ViewSettings.MSG_AUTH_ERROR: {
            Toast.makeText(getContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
        }
            break;
        case ViewSettings.MSG_NETWORK_EXCEPTION: {
            Toast.makeText(getContext(), R.string.network_exception, Toast.LENGTH_SHORT).show();
        }
            break;
        }
        return false;
    }

    public IUpload getUploadListener() {
        return uploadListener;
    }

    public void setUploadListener(IUpload uploadListener) {
        this.uploadListener = uploadListener;
    }

}
