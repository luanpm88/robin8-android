package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

/**
 * Created by IBM on 2016/8/22.
 */
public class InviteFridensModel extends BaseBean {

    /**
     * invite_count : 3
     * invite_amount : 6.0
     */

    private int invite_count;
    private String invite_amount;

    public int getInvite_count() {
        return invite_count;
    }

    public void setInvite_count(int invite_count) {
        this.invite_count = invite_count;
    }

    public String getInvite_amount() {
        return invite_amount;
    }

    public void setInvite_amount(String invite_amount) {
        this.invite_amount = invite_amount;
    }
}
