package com.robin8.rb.ui.module.mine.model;

import com.robin8.rb.ui.model.BaseBean;

/**
 * Created by IBM on 2016/8/22.
 */
public class InviteFridensModel extends BaseBean {
    /**
     * invite_count : 0
     * invite_amount : 0.0
     * invite_code : 54126398
     */

    private int invite_count;
    private String invite_amount;
    private int invite_code;
    private boolean is_show_newbie;
    private String invite_desc;
    private String desc;

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

    public int getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(int invite_code) {
        this.invite_code = invite_code;
    }

    public boolean is_show_newbie() {
        return is_show_newbie;
    }

    public void setIs_show_newbie(boolean is_show_newbie) {
        this.is_show_newbie = is_show_newbie;
    }

    public String getInvite_desc() {
        return invite_desc;
    }

    public void setInvite_desc(String invite_desc) {
        this.invite_desc = invite_desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * invite_count : 3
     * invite_amount : 6.0
     */

//    private int invite_count;
//    private String invite_amount;
//
//    public int getInvite_count() {
//        return invite_count;
//    }
//
//    public void setInvite_count(int invite_count) {
//        this.invite_count = invite_count;
//    }
//
//    public String getInvite_amount() {
//        return invite_amount;
//    }
//
//    public void setInvite_amount(String invite_amount) {
//        this.invite_amount = invite_amount;
//    }
}
