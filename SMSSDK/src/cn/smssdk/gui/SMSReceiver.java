/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */
package cn.smssdk.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

/**短信监听接收器，用于自动获取短信验证码，然后自动填写到验证码区域*/
public class SMSReceiver extends BroadcastReceiver {
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private SMSVerifyCodeListener verifyCodeListener = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			SmsMessage[] messages = getMessagesFromIntent(intent);
			for (SmsMessage message : messages) {
				String body = message.getDisplayMessageBody();
				if (!TextUtils.isEmpty(body)) {
					// "验证码："的char ， 判断在string中的位置
					String value = String.valueOf(new char[] { 0x9a8c, 0x8bc1, 0x7801, 0xff1a });
					int index = body.indexOf(value);
					if (index > -1) {
						String verifyCode = null;
						try {
							verifyCode = body.substring(index + 4, index + 8);
							Integer.parseInt(verifyCode);
						} catch (Throwable t) {
							verifyCode = null;
						}

						if (!TextUtils.isEmpty(verifyCode) && verifyCodeListener != null) {
							Log.e("verify code ==>>", verifyCode);
							verifyCodeListener.receiveVerifyCode(verifyCode);
						}
					}
				}
			}
		}
	}

	public void setSMSVerifyCodeListener(SMSVerifyCodeListener verifyCodeListener){
		this.verifyCodeListener = verifyCodeListener;
	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}

		return msgs;
	}

	public interface SMSVerifyCodeListener{
		public void receiveVerifyCode(String verifyCode);
	}

}
