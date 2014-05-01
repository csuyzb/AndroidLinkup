package com.znv.linkup.view.dialog;

import com.znv.linkup.R;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义的警告框
 * 
 * @author yzb
 * 
 */
public class AlertDialog extends Dialog {

    public AlertDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        setContentView(R.layout.alert_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setPositiveButton(context.getResources().getString(R.string.submit), null);
    }

    /**
     * 设置标题
     * 
     * @param title
     *            标题
     * @return 警告框实例
     */
    public AlertDialog setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.dialog_title);
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置信息
     * 
     * @param msg
     *            信息
     * @return 警告框示例
     */
    public AlertDialog setMessage(String msg) {
        TextView tvMessage = (TextView) findViewById(R.id.dialog_message);
        tvMessage.setText(msg);
        return this;
    }

    /**
     * 设置图标
     * 
     * @param resId
     *            图标resid
     * @return 警告框实例
     */
    public AlertDialog setIcon(int resId) {
        ImageView ivImage = (ImageView) findViewById(R.id.dialog_title_image);
        ivImage.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置确认按钮
     * 
     * @param text
     *            确认按钮文字
     * @param listener
     *            确认操作
     * @return 警告框实例
     */
    public AlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        Button btn = (Button) findViewById(R.id.dialog_button_ok);
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (listener != null) {
                    listener.onClick(null);
                }
            }
        });
        return this;
    }

    /**
     * 处理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Button btn = (Button) findViewById(R.id.dialog_button_ok);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
