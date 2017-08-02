package com.robin8.rb.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.robin8.rb.R;
import com.robin8.rb.activity.SplashActivity;
import com.robin8.rb.model.PushBean;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {

    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
          //  LogUtil.LogShitou("透传数据", "onReceiveMessageData -> " + data);
            //判断是否登录
            if (!TextUtils.isEmpty(HelpTools.getLoginInfo(HelpTools.Token))) {
                try {
                    PushBean pushBean = GsonTools.jsonToBean(data, PushBean.class);
                    switch (pushBean.getAction()) {
                        case "common":
                        case "campaign":
                           fenCeAlarmNotice(context, pushBean.getTitle(), pushBean.getSender() + pushBean.getName());
                            break;
//                        case "campaign_detail":
//                           // LogUtil.LogShitou("收到推送","------------------------------");
//                            if (AppUtils.isAppAlive(context,"com.robin8.rb")){
//
//                            }else {
//                                LogUtil.LogShitou("收到推送","------------------------------");
//                            }
//                            fenCeAlarmNotice(context, pushBean.getTitle(), pushBean.getSender() + pushBean.getName(),data);
//
//                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void fenCeAlarmNotice(Context context, String title, String content) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);// 创建通知栏
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        PendingIntent pendingIntent = null;

        Bundle bundle = new Bundle();
        bundle.putInt("unread", 1);
        //        bundle.putString("headUrl", headUrl);
        pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, SplashActivity.class).putExtras(bundle),
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
//    private void fenCeAlarmNotice(Context context, String title, String content,String data) {
//
//        NotificationManager mNotificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);// 创建通知栏
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        mBuilder.setContentTitle(title);
//        mBuilder.setContentText(content);
//        mBuilder.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL,data));
//        mBuilder.setPriority(Notification.PRIORITY_DEFAULT); // 设置该通知优先级
//        mBuilder.setTicker(title);
//        //        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        mBuilder.setAutoCancel(true);// 设置这个标志当用户单击面板就可以让通知将自动取消
//        mBuilder.setOngoing(false);// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,
//        // 因此占用设备(如一个文件下载,同步操作,主动网络连接)
//        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//
//        // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小ICON
//        mBuilder.build().flags = Notification.DEFAULT_ALL;
//        mNotificationManager.notify(123244, mBuilder.build());
//        //mNotificationManager.notify(123244, mBuilder.build());
//    }
    public PendingIntent getDefalutIntent(int flags,String data){

//        Intent serviceIntent = new Intent(this, SplashActivity.class);
//        serviceIntent .putExtra("unread",1);
//        //getService 跳到服务   getActivity 跳到activity
//        PendingIntent pendingIntent= PendingIntent.getService(this, 0,serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Bundle bundle = new Bundle();
       // bundle.putInt("unread", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, new Intent(this, GeTuiReceiver.class), flags);
//        Bundle bundle = new Bundle();
//        bundle.putInt("unread", 2);
//        startActivity(new Intent(this, SplashActivity.class).putExtras(bundle));
        return pendingIntent;
    }
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtil.LogShitou("GrTui", "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        LogUtil.LogShitou("GrTui再现还是离线", "+++》》"+online);
//        if (online){
//            LogUtil.LogShitou("GrTui再现还是离线tttt","------------------------------");
//        }else {
//            LogUtil.LogShitou("GrTui再现还是离线aaaatt","------------------------------");
//
//        }
    }


    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
       // LogUtil.LogShitou("处理回执", "onReceiveCommandResult -> " + cmdMessage);
        int action = cmdMessage.getAction();
        if (action == PushConsts.SET_TAG_RESULT) {
           // setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
          //  feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

}
