package com.robin8.rb.update;

import android.content.Intent;
import android.net.Uri;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateHelp {
    public static UpdateHelp instance;

    public static UpdateHelp getInstance() {
        if (instance == null) {
            instance = new UpdateHelp();
        }
        return instance;
    }

    /**
     * 安装apk
     */
    public void installApk() {
        URL url = null;
        try {
            url = new URL(UpdateNewApk.DOWNLOADURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File apkfile = new File(CommonConfig.path,
                new File(url.getFile()).getName());
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        BaseApplication.getContext().startActivity(i);
        callback.OnBackResult("finish");
    }

    // 不管是服务还是主线程，下载完成都会回调该结果,将程序结束掉
    private UpdateNewApk.ICallbackResult callback = new UpdateNewApk.ICallbackResult() {

        @Override
        public void OnBackResult(Object result) {
            if ("finish".equals(result)) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return;
            }
        }
    };
}
