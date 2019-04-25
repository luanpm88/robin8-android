package com.robin8.rb.util.update;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.robin8.rb.base.constants.CommonConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @params[0]为完整下载链接
 */
public class UpdateDownTask extends AsyncTask<String, String, String> {
    private int lastRate = 0;
    private Boolean isLoading = false;
    private Handler mHandler;

    public UpdateDownTask(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    protected String doInBackground(String... params) {
        this.isLoading = true;
        try {
            int length = 0;
            FileOutputStream fos = null;
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            length = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(CommonConfig.path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String apkFile = CommonConfig.path + new File(url.getFile()).getName();
            File ApkFile = new File(apkFile);
            fos = new FileOutputStream(ApkFile);
            int count = 0;
            byte buf[] = new byte[1024];
            do {
                int numread = is.read(buf);
                count += numread;
                int progress = (int) (((float) count / length) * 100);
                // 更新进度
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.arg1 = progress;
                if (progress >= lastRate + 1) {
                    mHandler.sendMessage(msg);
                    lastRate = progress;
                }
                if (numread <= 0) {
                    // 下载完成通知安装
                    mHandler.sendEmptyMessage(0);
                    // 下载完了，cancelled也要设置
                    isLoading = false;
                    break;
                }
                fos.write(buf, 0, numread);
            } while (isLoading);// 点击取消就停止下载.

            fos.close();

            is.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Boolean isAlive() {
        return isLoading;
    }

    public void setCancelDown() {
        isLoading = false;
    }
}
