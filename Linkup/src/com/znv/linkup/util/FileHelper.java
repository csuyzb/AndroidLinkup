package com.znv.linkup.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

public class FileHelper {

    private static String ENCODE = "utf-8";
    private Context ctx = null;

    public FileHelper(Context ctx) {
        this.ctx = ctx;
    }

    public void writeJson(String fileName, String jsonStr) {
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jsonStr.getBytes(ENCODE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readJson(String fileName) {
        FileInputStream fis = null;
        try {
            fis = ctx.openFileInput(fileName);
            // 输出到内存
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);//
            }
            byte[] content_byte = outStream.toByteArray();
            return new String(content_byte, ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeJsonToSd(String fileName, String jsonStr) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
            fos.write(jsonStr.getBytes(ENCODE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readJsonFromSd(String fileName) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
            // 输出到内存
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);//
            }
            byte[] content_byte = outStream.toByteArray();
            outStream.close();
            return new String(content_byte, ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String readJsonFromAssets(String fileName) {
        InputStream is = null;
        try {
            is = ctx.getAssets().open(fileName);
            // 输出到内存
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[10240];
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);//
            }
            byte[] content_byte = outStream.toByteArray();
            outStream.close();
            return new String(content_byte, ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
