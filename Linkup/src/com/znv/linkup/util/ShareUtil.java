package com.znv.linkup.util;

import com.znv.linkup.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.content.Context;
import android.view.View;

/**
 * 分享的帮助类
 * 
 * @author yzb
 * 
 */
public class ShareUtil {

    private static void showShare(Context context, OnekeyShare oks) {
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://xxllk.aliapp.com");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://xxllk.aliapp.com");
        // 评论
        oks.setComment(context.getString(R.string.share_comment));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.wandoujia.com/apps/com.znv.linkup");

        oks.setSilent(true);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享文字
     * 
     * @param context
     *            上下文信息
     * @param msg
     *            文字信息
     */
    public static void shareMessage(Context context, String msg) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        // text是分享文本，所有平台都需要这个字段
        oks.setText(msg);

        showShare(context, oks);
    }

    /**
     * 分享截图
     * 
     * @param context
     *            上下文
     * @param view
     *            截图的view
     */
    public static void shareView(Context context, View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setViewToShare(view);

        showShare(context, oks);
    }

    /**
     * 分享文字和图片
     * 
     * @param context
     *            上下文
     * @param msg
     *            文字信息
     * @param view
     *            截图的view
     */
    public static void shareMsgView(Context context, String msg, View view) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setText(msg);
        oks.setViewToShare(view);

        showShare(context, oks);
    }
}
