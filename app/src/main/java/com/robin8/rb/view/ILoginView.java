package com.robin8.rb.view;

import android.view.View;

/**
 Created by Figo on 2016/6/24. */
public interface ILoginView {

    //手机号
    String getPhoneNumber();
    //验证码
    String getCheckCode();
    //邀请码
    String getInvitationCode();
    //清空edit
    void clearEdit(int i);

    //邮箱名字
    String getEmailNumber();
    //邮箱密码
    String getEmailPwd();

    View getTv();

}
