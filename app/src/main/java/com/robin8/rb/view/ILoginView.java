package com.robin8.rb.view;

import android.view.View;

/**
 * Created by Figo on 2016/6/24.
 */
public interface ILoginView {


    String  getPhoneNumber();

    String  getCheckCode();

    String  getInvitationCode();

    void clearEdit(int i);

    View getTv();

}
