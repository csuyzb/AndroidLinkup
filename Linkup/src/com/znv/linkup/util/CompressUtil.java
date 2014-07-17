package com.znv.linkup.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩和解压缩帮助类
 * 
 * @author yzb
 * 
 */
public class CompressUtil {

    /**
     * 压缩
     * 
     * @param str
     *            待压缩的字符串
     * @return 压缩字符串
     */
    public static String compress(String str) {
        return compress(str, "utf-8");
    }

    /**
     * 压缩
     * 
     * @param str
     *            待压缩的字符串
     * @param charset
     *            字符编码
     * @return 压缩字符串
     */
    public static String compress(String str, String charset) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(charset));
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 解压缩
     * 
     * @param str
     *            压缩字符串
     * @return 解压字符串
     */
    public static String uncompress(String str) {
        return uncompress(str, "utf-8");
    }

    /**
     * 解压缩
     * 
     * @param str
     *            压缩字符串
     * @param charset
     *            字符编码
     * @return 解压字符串
     */
    public static String uncompress(String str, String charset) {
        if (str == null || str.length() == 0) {
            return str;
        }
        byte[] buffer = new byte[256];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(
                    str.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(charset);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
