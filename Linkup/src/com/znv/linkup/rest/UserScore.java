package com.znv.linkup.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;
import android.os.Message;

import com.znv.linkup.MyApplication;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.util.RestUtil;
import com.znv.linkup.util.StringUtil;

/**
 * 第三方平台用户关卡分数或时间管理
 * 
 * @author yzb
 * 
 */
public class UserScore {
    private static String USER_ADD_URI = ViewSettings.WebRoot + "/webapi/user/add";
    private static String USER_UPDATE_URI = ViewSettings.WebRoot + "/webapi/user/update";
    private static String USER_LIKE_URI = ViewSettings.LikeWebRoot + "/webapi/user/like";
    public static String USER_TOTALRANK_URI = ViewSettings.WebRoot + "/webapi/user/totalrank";
    public static String LEVEL_ADD_URI = ViewSettings.WebRoot + "/webapi/level/add";
    public static String LEVEL_GET_URI = ViewSettings.WebRoot + "/webapi/level/get";
    public static String LEVEL_ADDGET_URI = ViewSettings.WebRoot + "/webapi/level/addget";

    /**
     * 记录用户登录
     * 
     * @param userInfo
     *            用户登录信息
     * @param handler
     *            消息处理
     */
    public static void login(final UserInfo userInfo, final Handler handler) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", userInfo.getUserId()));
        params.add(new BasicNameValuePair("username", userInfo.getUserName()));
        params.add(new BasicNameValuePair("usergender", userInfo.getUserGender()));
        params.add(new BasicNameValuePair("usericon", userInfo.getUserIcon()));
        if (!StringUtil.isNullOrEmpty(MyApplication.Push_Reg_ID)) {
            params.add(new BasicNameValuePair("regid", MyApplication.Push_Reg_ID));
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RestUtil.post(USER_ADD_URI, params);
                if (result != null) {
                    Message msg = new Message();
                    msg.what = ViewSettings.MSG_LOGIN;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    // 网络或其它问题
                    handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
                }
            }
        }).start();
    }

    /**
     * 更新用户的钻石和金币
     * 
     * @param userId
     *            用户ID
     * @param diamond
     *            改变的钻石数
     * @param gold
     *            改变的金币数
     * @param handler
     *            消息处理
     */
    public static void updateAward(final String userId, int diamond, int gold, final Handler handler) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", userId));
        params.add(new BasicNameValuePair("diamond", String.valueOf(diamond)));
        params.add(new BasicNameValuePair("gold", String.valueOf(gold)));
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RestUtil.post(USER_UPDATE_URI, params);
                if (result != null) {
                    Message msg = new Message();
                    msg.what = ViewSettings.MSG_UPDATE_GOLD;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    // 网络或其它问题
                    handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
                }
            }
        }).start();
    }

    /**
     * 点赞用户
     * 
     * @param tag
     *            用户ID;用户名
     * @param likeNum
     *            1:赞;-1:取消赞
     * @param handler
     */
    public static void updateLike(final String tag, final int likeNum, final Handler handler) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        String userId = tag.substring(0, tag.indexOf(";"));
        params.add(new BasicNameValuePair("userid", userId));
        params.add(new BasicNameValuePair("like", String.valueOf(likeNum)));
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RestUtil.post(USER_LIKE_URI, params);
                if (result != null) {
                    Message msg = new Message();
                    msg.what = ViewSettings.MSG_UPDATE_LIKE;
                    msg.obj = String.valueOf(likeNum) + ";" + tag;
                    handler.sendMessage(msg);
                } else {
                    // 网络或其它问题
                    handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
                }
            }
        }).start();
    }

    /**
     * 新增用户关卡分数，用于排名
     * 
     * @param levelInfo
     *            上传的分数信息
     * @param handler
     *            消息处理
     */
    public static void addGetResult(LevelInfo levelInfo, final Handler handler) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", levelInfo.getUserId()));
        params.add(new BasicNameValuePair("level", String.valueOf(levelInfo.getLevel())));
        params.add(new BasicNameValuePair("score", String.valueOf(levelInfo.getScore())));
        params.add(new BasicNameValuePair("time", String.valueOf(levelInfo.getTime())));
        params.add(new BasicNameValuePair("diamond", String.valueOf(levelInfo.getDiamond())));
        params.add(new BasicNameValuePair("gold", String.valueOf(levelInfo.getGold())));
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RestUtil.post(LEVEL_ADDGET_URI, params);
                if (result != null) {
                    // 成功获取排名信息
                    Message msg = new Message();
                    msg.what = ViewSettings.MSG_LEVEL_ADDGET;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    // 网络或其它问题
                    handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
                }
            }
        }).start();
    }

    /**
     * 按关卡获取排名信息
     * 
     * @param level
     *            关卡id
     * @param handler
     *            消息处理
     */
    public static void getLevelTops(int level, final Handler handler) {
        getLevelTops(level, ViewSettings.TopN, handler);
    }

    /**
     * 按关卡获取排名信息
     * 
     * @param level
     *            关卡id
     * @param handler
     *            消息处理
     */
    public static void getLevelTops(int level, int topN, final Handler handler) {
        final String uri = LEVEL_GET_URI + "?level=" + String.valueOf(level) + "&top=" + String.valueOf(topN);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RestUtil.get(uri);
                if (result != null) {
                    // 成功获取排名信息
                    Message msg = new Message();
                    msg.what = ViewSettings.MSG_LEVEL_GET;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    // 网络或其它问题
                    handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
                }
            }
        }).start();
    }

    // /**
    // * 获取用户icon
    // *
    // * @param url
    // * url地址
    // * @param handler
    // * 消息处理
    // */
    // public static void getUserImage(final String url, final Handler handler) {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // String newUrl = url;
    // // 获取清晰的QQ图像
    // // if(newUrl.endsWith("/30")){
    // // newUrl = newUrl.substring(0, newUrl.length() - 3) + "/100";
    // // }
    // Bitmap bm = IconCacheUtil.getIcon(newUrl);
    // if (bm == null) {
    // try {
    // bm = BitmapFactory.decodeStream((new URL(newUrl)).openStream());
    // } catch (Exception e) {
    // e.printStackTrace();
    // bm = null;
    // }
    // }
    // if (bm != null) {
    // IconCacheUtil.putIcon(newUrl, bm);
    // // 成功获取排名信息
    // Message msg = new Message();
    // msg.what = ViewSettings.MSG_IMAGE_GET;
    // msg.obj = bm;
    // handler.sendMessage(msg);
    // } else {
    // // 网络或其它问题
    // handler.sendEmptyMessage(ViewSettings.MSG_NETWORK_EXCEPTION);
    // }
    // }
    // }).start();
    // }
}
