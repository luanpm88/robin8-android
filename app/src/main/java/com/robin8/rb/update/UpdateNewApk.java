package com.robin8.rb.update;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.UpdateBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.LinkedHashMap;

/**
 * 该类提供程序更新
 *
 * @author Administrator 党宏
 */

public class UpdateNewApk {
    private Context context;
    private Handler mCheckVersionHandler;
    private String version = "0.0";
    public ProgressDialog pBar;
    public static String DOWNLOADURL;


    private UpdateDownloadService.DownloadBinder binder;

    public UpdateNewApk(Context context, Handler handler) {
        this.context = context;
        this.mCheckVersionHandler = handler;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkNewVersion(final Boolean isNeedNotice) {
        if (context != null) {
            LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
            requestMap.put("[url]", HelpTools.getUrl(CommonConfig.UpdateUrl));
            requestMap.put("app_platform", "Android");
            requestMap.put("app_version", AppUtils.getVersionName(context));
            HttpRequest mHttpRequest = HttpRequest.getInstance();
            RequestParams requestParams = new RequestParams();
            requestParams.put("app_platform", "Android");
            requestParams.put("app_version", AppUtils.getVersionName(context));
            mHttpRequest.get(false, HelpTools.getUrl(CommonConfig.UpdateUrl), requestParams, new RequestCallback() {
                @Override
                public void onError(Exception e) {
                    if (mCheckVersionHandler != null) {
                        mCheckVersionHandler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtil.logXXfigo(HelpTools.getUrl(CommonConfig.UpdateUrl) + "  " + response);
                    UpdateBean commBean = null;
                    try {
                        commBean = GsonTools.jsonToBean(response, UpdateBean.class);
                    } catch (Exception e) {
                        LogUtil.logXXfigo(e.toString());
                    }

                    if (commBean != null && commBean.getError() == 0 && commBean.isHad_upgrade()) {
                        UpdateNewApk.DOWNLOADURL = commBean.getNewest_version()
                                .getDownload_url();// 赋值链接
                        if (!version.equals(commBean.getNewest_version().getApp_version()
                        )) {
                            if (commBean.getNewest_version().isForce_upgrade()) { // 强制更新
                                showForcedDownLoadDialog(true, commBean.getNewest_version()
                                        .getRelease_note());
                            } else { // 选择性更新
                                showForcedDownLoadDialog(false, commBean.getNewest_version()
                                        .getRelease_note());
                            }
                        } else {
                            if (mCheckVersionHandler != null) {
                                mCheckVersionHandler.sendEmptyMessage(1);
                            }
                            if (isNeedNotice) {
                                CustomToast.showShort(context, "当前已经是最新版本");
                            }
                        }
                    } else {//没有更新
                        if (mCheckVersionHandler != null) {
                            mCheckVersionHandler.sendEmptyMessage(1);
                        }
                        if (isNeedNotice) {
                            CustomToast.showShort(context, "当前已经是最新版本");
                        }
                    }
                }
            });
        }
    }

    /**
     * 下载提示框
     *
     * @param isForced     //是否为强制更新
     * @param describition //更新描述
     */
    private void showForcedDownLoadDialog(final Boolean isForced, String describition) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        View v = LayoutInflater.from(context).inflate(R.layout.update_apk_notice, null);

        TextView text_description = (TextView) v.findViewById(R.id.text_description);
        TextView tbt_cancel = (TextView) v.findViewById(R.id.tbt_cancel);
        TextView tbt_confirm = (TextView) v.findViewById(R.id.tbt_confirm);

        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);

        // 更新描述,根据不同的更新要求显示按钮不同的文字
        text_description.setText(describition);
        if (isForced) {
            tbt_cancel.setText("取消");
            tbt_confirm.setText("更新");
            dialog.setCancelable(false);
        } else {
            tbt_cancel.setText("忽略");
            tbt_confirm.setText("更新");
            dialog.setCancelable(true);
        }
        tbt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 如果是强制更新，则退出
                if (isForced) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } else {
                    // 选择更新
                    if (mCheckVersionHandler != null) {
                        mCheckVersionHandler.sendEmptyMessage(1);
                    }
                }
            }
        });
        tbt_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isForced) {
                    // 此处为强制性更新
                    pBar = new ProgressDialog(context);
                    pBar.setTitle(context.getString(R.string.update_ing)  + BaseApplication.getContext().getResources().getString(R.string.app_name));
                    pBar.setMessage(context.getString(R.string.update_wait));
                    pBar.setCancelable(false);
                    pBar.setCanceledOnTouchOutside(false);
                    pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pBar.show();
                    startDownload();
                } else {
                    // 此处为非强制性更新进入后台更新
                    Intent it = new Intent(context, UpdateDownloadService.class);
                    context.startService(it);
                    context.bindService(it, conn, Context.BIND_AUTO_CREATE);
                    CustomToast.showShort(context, context.getString(R.string.update_backupdate));
                    // 选择更新
                    if (mCheckVersionHandler != null) {
                        mCheckVersionHandler.sendEmptyMessage(1);
                    }
                }
            }
        });
        dialog.show();
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (UpdateDownloadService.DownloadBinder) service;
            // 开始下载
            binder.start();
        }
    };

    public interface ICallbackResult {
        public void OnBackResult(Object result);
    }

    private void updateProgress(Integer... values) {
        if (pBar == null)
            return;
        if (values.length > 1) {
            int contentLength = values[1];
            if (contentLength == -1) {
                pBar.setIndeterminate(true);
            } else {
                pBar.setMax(contentLength);
            }
        } else {
            pBar.setProgress(values[0].intValue());
        }
    }

    private UpdateDownTask updateDownTask = null;

    /**
     * 开启下载任务
     */
    private void startDownload() {
        if (updateDownTask == null) {
            updateDownTask = new UpdateDownTask(mHandler);
            updateDownTask.execute(UpdateNewApk.DOWNLOADURL);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 下载完毕
                    updateDownTask = null;
                    UpdateHelp.getInstance().installApk();
                    break;
                case 1:
                    int rate = msg.arg1;
                    if (rate < 100) {
                        updateProgress(rate);
                    }
                    break;
            }
        }
    };
}
