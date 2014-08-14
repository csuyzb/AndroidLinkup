package com.znv.linkup.util;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.WelcomeActivity;
import com.znv.linkup.rest.UserScore;

/**
 * 分享的帮助类
 * 
 * @author yzb
 * 
 */
public class ShareUtil {

    private Context context = null;

    public ShareUtil(Context context) {
        this.context = context;

        ShareSDK.initSDK(context);

        // 设置分享监听
        Platform[] plats = ShareSDK.getPlatformList(context);
        for (Platform plat : plats) {
            plat.setPlatformActionListener(platAction);
        }
    }

    /**
     * 分享文字
     * 
     * @param msg
     *            文字信息
     */
    public void shareMessage(String msg) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        // text是分享文本，所有平台都需要这个字段
        oks.setText(msg);

        showShare(oks);
    }

    /**
     * 分享截图
     * 
     * @param view
     *            截图的view
     */
    public void shareView(View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setViewToShare(view);

        showShare(oks);
    }

    /**
     * 分享文字和图片
     * 
     * @param msg
     *            文字信息
     * @param view
     *            截图的view
     */
    public void shareMsgView(String msg, View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setText(msg);
        oks.setViewToShare(view);

        showShare(oks);
    }

    private PlatformActionListener platAction = new PlatformActionListener() {

        public void onError(Platform platform, int action, Throwable t) {
        }

        public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
            // 操作成功的处理代码
            if (WelcomeActivity.userInfo != null) {
                // 消耗钻石
                WelcomeActivity.userInfo.addDiamond(context, 5);
                // 更新网络钻石数
                UserScore.updateAward(WelcomeActivity.userInfo.getUserId(), 5, 0, netMsgHandler);
            }
        }

        public void onCancel(Platform platform, int action) {
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler netMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ViewSettings.MSG_UPDATE_GOLD: {
                Toast.makeText(context, R.string.share_success, Toast.LENGTH_SHORT).show();
            }
                break;
            case ViewSettings.MSG_NETWORK_EXCEPTION: {
                Toast.makeText(context, R.string.network_exception, Toast.LENGTH_SHORT).show();
            }
                break;
            }
        }
    };

    private void showShare(OnekeyShare oks) {
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(ViewSettings.WebRoot);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(ViewSettings.WebRoot);
        // 评论
        // oks.setComment(context.getString(R.string.share_comment));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(ViewSettings.WebRoot);

        oks.setSilent(true);

        // 启动分享GUI
        oks.show(context);
    }
}
