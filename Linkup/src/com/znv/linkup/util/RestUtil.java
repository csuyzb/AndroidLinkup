package com.znv.linkup.util;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 访问REST接口的帮助类
 * 
 * @author yzb
 * 
 */
public class RestUtil {

    /**
     * 访问REST GET接口，返回字符串数据
     * 
     * @param uri
     *            REST的URI
     * @return 字符串数据
     */
    public static String get(String uri) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(uri);
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            Log.d("REST-GET", e.getMessage());
        }
        return null;
    }

    /**
     * 访问REST POST接口，返回字符串
     * 
     * @param uri
     *            REST的URI
     * @param params
     *            post参数
     * @return 字符串数据
     */
    public static String post(String uri, List<NameValuePair> params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(uri);

        HttpResponse response;
        try {
            HttpEntity entity = new UrlEncodedFormEntity(params, "utf-8");
            request.setEntity(entity);
            response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            Log.d("REST-POST", e.getMessage());
        }
        return null;
    }
}
