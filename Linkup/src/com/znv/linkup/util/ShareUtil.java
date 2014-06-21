package com.znv.linkup.util;

import com.znv.linkup.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.content.Context;
import android.view.View;

public class ShareUtil {

    private static void showShare(Context context, OnekeyShare oks) {
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setSilent(true);

        // 分享时Notification的图标和文字
        // oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        // oks.setTitleUrl("http://www.wandoujia.com/apps/com.znv.linkup");
        // // text是分享文本，所有平台都需要这个字段
        // oks.setText(msg);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.wandoujia.com/apps/com.znv.linkup");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://xxllk.aliapp.com");

        // 启动分享GUI
        oks.show(context);
    }

    public static void shareMessage(Context context, String msg) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        // text是分享文本，所有平台都需要这个字段
        oks.setText(msg);

        showShare(context, oks);
    }

    public static void shareView(Context context, View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setViewToShare(view);

        showShare(context, oks);
    }

    public static void shareMsgView(Context context, String msg, View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setText(msg);
        oks.setViewToShare(view);

        showShare(context, oks);
    }
}
