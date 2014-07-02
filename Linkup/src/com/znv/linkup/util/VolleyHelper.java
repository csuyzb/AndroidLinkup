package com.znv.linkup.util;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.znv.linkup.R;
import com.znv.linkup.rest.NetMsgListener;

/**
 * Volley网络事件处理的帮助类
 * 
 * @author yzb
 * 
 */
public class VolleyHelper {
    private RequestQueue requestQueue = null;

    public VolleyHelper(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
    }

    /**
     * 加载JsonArray数据
     * 
     * @param url
     *            url地址
     * @param listener
     *            网络消息处理监听器
     */
    public void getJsonArray(String url, final NetMsgListener<JSONArray> listener) {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (listener != null) {
                    listener.netMsgHandle(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d("VolleyUtil-getJsonArray", e.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 加载网络图片到ImageView
     * 
     * @param iv
     *            ImageView实例
     * @param url
     *            url地址
     */
    public void loadImage(ImageView iv, String url) {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.icon_default, R.drawable.icon_default);
        imageLoader.get(url, listener);
    }

    /**
     * 加载网络图片到NetworkImageView
     * 
     * @param niv
     *            NetworkImageView实例
     * @param url
     *            url地址
     */
    public void loadNetImage(NetworkImageView niv, String url) {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());

        niv.setTag("url");
        niv.setImageUrl(url, imageLoader);
    }

    // public void cancelAll(Context ctx) {
    // requestQueue.cancelAll(ctx);
    // }
}
