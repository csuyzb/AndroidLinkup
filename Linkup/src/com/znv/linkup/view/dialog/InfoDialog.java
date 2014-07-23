package com.znv.linkup.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.znv.linkup.R;
import com.znv.linkup.util.CacheUtil;

/**
 * 自定义的警告框
 * 
 * @author yzb
 * 
 */
public class InfoDialog extends Dialog {

    public InfoDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        setContentView(R.layout.info_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setPositiveButton();
    }

    /**
     * 设置标题
     * 
     * @param title
     *            标题
     * @return 警告框实例
     */
    public InfoDialog setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
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
    public InfoDialog setMessage(String msg) {
        TextView tvMessage = (TextView) findViewById(R.id.tvInfo);
        tvMessage.setText(msg);
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
    private InfoDialog setPositiveButton() {
        CheckBox btn = (CheckBox) findViewById(R.id.cbxKnown);
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CacheUtil.setBind(InfoDialog.this.getContext(), "info_login", true);
                    cancel();
                }
            }
        });
        return this;
    }

    public void setWeiboClick(final View.OnClickListener listener) {
        findViewById(R.id.tvWeibo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                cancel();
            }
        });
    }

    public void setQQClick(final View.OnClickListener listener) {
        findViewById(R.id.tvQQ).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                cancel();
            }
        });
    }

    // /**
    // * 处理返回键
    // */
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // cancel();
    // return true;
    // }
    // return super.onKeyDown(keyCode, event);
    // }
}
