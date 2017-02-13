package com.robin8.rb.helper;

import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.util.UIUtils;

import java.util.Observable;

/**
 * 全局消息管理器，通过观察者向程序内发消息,类似于程序内的广播
 *
 * @author figo
 */
public class NotifyManager extends Observable {

    /**
     * 被观察的事件类型
     */
    public static final int TYPE_OTHER = -1;// 其他未知
    public static final int TYPE_LOGIN = 1; // 登录
    public static final int TYPE_LOGIN_OUT = 2;// 退出
    public static final int TYPE_SHARE_SUCCESS = 3;// 分享成功
    public static final int TYPE_PAY_SUCCESSFUL = 4;// 支付成功
    public static final int TYPE_INDIANA_PAY_SUCCESSFUL = 5;// 夺宝支付成功
    public static final int TYPE_REFRESH_PROFILE = 6;// 更新我的信息
    public static final int TYPE_REFRESH_PROFILE_REWORD_PAGE = 7;// 更新我的信息--悬赏
    public static final int TYPE_INSERT_PRODUCT = 8;//插入商品

    private static NotifyManager mNotifyManager;

    /**
     * 获取通知管理器
     *
     * @return
     */
    public static NotifyManager getNotifyManager() {
        if (mNotifyManager == null) {
            mNotifyManager = new NotifyManager();
        }
        return mNotifyManager;
    }

    private NotifyManager() {

    }

    /**
     * 事件发生后通知监听者
     *
     * @param code :事件代码号
     */
    public void notifyChange(int code) {
        NotifyMsgEntity msgEntity = new NotifyMsgEntity();
        msgEntity.setCode(code);
        notifyChange(msgEntity);
    }

    /**
     * 事件发生后通知监听者
     *
     * @param msgEntity 需要发送的消息数据
     */
    public void notifyChange(final NotifyMsgEntity msgEntity) {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(msgEntity);
            }
        });
    }

}
