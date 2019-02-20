package com.robin8.rb.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.robin8.rb.R;
import com.robin8.rb.activity.SplashActivity;
import com.robin8.rb.base.BaseApplication;

public class UpdateDownloadService extends Service {
    private static final int NOTIFY_ID = 0;
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    private DownloadBinder binder;
    private boolean serviceIsDestroy = false;

    private Context mContext = this;
    private Handler mHandler = new Handler() {

        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    updateDownTask = null;
                    // 下载完毕
                    mNotificationManager.cancel(NOTIFY_ID);
                    UpdateHelp.getInstance().installApk();
                    UpdateDownloadService.this.stopSelf();
                    break;
                case 2:
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:
                    int rate = msg.arg1;
                    if (rate < 100) {
                        RemoteViews contentview = mNotification.contentView;
                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
                        contentview.setProgressBar(R.id.progressbar, 100, rate,
                                false);
                    } else {
                        // 下载完毕后变换通知形式
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotification.contentView = null;
                        Intent intent = new Intent(mContext, SplashActivity.class);
                        // 告知已完成
                        intent.putExtra("completed", "yes");
                        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                        PendingIntent contentIntent = PendingIntent.getActivity(
                                mContext, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                      //  mNotification.setLatestEventInfo(mContext, mContext.getResources().getString(R.string.update_downfinish), mContext.getResources().getString(R.string.update_apkdownfinish), contentIntent);

                        serviceIsDestroy = true;
                        stopSelf();// 停掉服务自身
                    }
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("running onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("downloadservice onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub

        super.onRebind(intent);
        System.out.println("downloadservice onRebind");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public class DownloadBinder extends Binder {
        public void start() {
            if (updateDownTask == null || !updateDownTask.isAlive()) {
                progress = 0;
                setUpNotification();
                new Thread() {
                    public void run() {
                        // 下载
                        startDownload();
                    }

                    ;
                }.start();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            mHandler.sendEmptyMessage(2);
        }
    }

    Notification mNotification;

    /**
     * 创建通知
     */
    @SuppressWarnings("deprecation")
    private void setUpNotification() {
        CharSequence tickerText = mContext.getResources().getString(
                R.string.update_beginload);
        long when = System.currentTimeMillis();
     //   mNotification = new Notification(R.mipmap.ic_launcher, tickerText,when);
        Notification.Builder builder = new Notification.Builder(mContext);//分版本
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(tickerText);
        builder.setWhen(when);
        // 放置在"正在运行"栏目中
        builder.setDefaults(Notification.FLAG_ONGOING_EVENT);

        mNotification = builder.build();

        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.update_notification_layout);
        contentView.setTextViewText(R.id.name, mContext.getResources()
                .getString(R.string.update_loading)
                + BaseApplication.getContext().getResources().getString(R.string.app_name)
                + " ...");
        // 指定个性化视图
        mNotification.contentView = contentView;

        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 指定内容意图
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    private UpdateDownTask updateDownTask;

    /**
     * 开启下载任务
     */
    private void startDownload() {
        canceled = false;
        updateDownTask = new UpdateDownTask(mHandler);
        updateDownTask.execute(UpdateNewApk.DOWNLOADURL);
    }
}
