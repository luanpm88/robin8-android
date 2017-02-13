package com.robin8.rb.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.igexin.sdk.PushConsts;
import com.robin8.rb.R;
import com.robin8.rb.activity.SplashActivity;
import com.robin8.rb.model.PushBean;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;


/**
 * 接收个推推送
 */
public class PushReceiver extends BroadcastReceiver {
    public PushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        L.("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用通常需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送。
                // 部分特殊情况下CID可能会发生变化，为确保应用服务端保存的最新的CID，应用程序在每次获取CID广播后，如果发现CID出现变化，需要重新进行一次关联绑定
                String cid = bundle.getString("clientid");
//                Log.d("GetuiSdkDemo", "Got CID:" + cid);

                break;
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    //判断是否登录
                    if (!TextUtils.isEmpty(HelpTools.getLoginInfo(HelpTools.Token))) {
                        try {
                            PushBean pushBean = GsonTools.jsonToBean(data, PushBean.class);
                            switch (pushBean.getAction()) {
                                case "common":
                                case "campaign":
                                    fenCeAlarmNotice(context, pushBean.getTitle(), pushBean.getSender() + pushBean.getName());
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            //添加其他case
            //.........
            default:
                break;

        }
    }

    /**
     * 显示通知
     *
     * @param context
     * @param title
     * @param content
     */
    private void fenCeAlarmNotice(Context context, String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);// 创建通知栏
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        PendingIntent pendingIntent = null;

        Bundle bundle = new Bundle();
        bundle.putInt("unread", 1);
//        bundle.putString("headUrl", headUrl);
        pendingIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, SplashActivity.class).putExtras(bundle),
                PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(pendingIntent); // 设置通知栏点击意图
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT); // 设置该通知优先级
//        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);// 设置这个标志当用户单击面板就可以让通知将自动取消
        mBuilder.setOngoing(false);// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,
        // 因此占用设备(如一个文件下载,同步操作,主动网络连接)
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//
        // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小ICON
        mBuilder.build().flags = Notification.DEFAULT_ALL;
        mNotificationManager.notify(123244, mBuilder.build());
    }
}

