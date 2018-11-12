package com.robin8.rb.activity.web;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.robin8.rb.R;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.update.UpdateNewApk;
import com.robin8.rb.util.LogUtil;

import java.io.File;

public class TextActivity extends AppCompatActivity {

    private Button btn;
    private UpdateNewApk mUpdateNewApk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        FileDownloader.setup(this);
        btn = ((Button) findViewById(R.id.btn));
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LogUtil.LogShitou("点击了", "------");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    LogUtil.LogShitou("卡啊嗷嗷", "------");

                    FileDownloader.getImpl().create(CommonConfig.DOWN).setWifiRequired(true).setPath((Environment.getExternalStorageDirectory() + File.separator + "robin8")).setListener(new FileDownloadListener() {

                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            LogUtil.LogShitou("下载开始", "------");
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            int percent = (int) ((double) soFarBytes / (double) totalBytes * 100);
                            btn.setText("(" + percent + "%" + ")");
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            Toast.makeText(TextActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
                            btn.setText("(" + "100%" + ")");
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            e.printStackTrace();
                            LogUtil.LogShitou("下载出错", "------" + e.getMessage());

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                        }
                    }).start();
                }else {
                    //SD卡没有装入
                    LogUtil.LogShitou("没卡啊嗷嗷", "------");
                }
            }
        });
    }

}
