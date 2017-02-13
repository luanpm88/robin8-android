package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

/**
 * Created by IBM on 2016/8/23.
 */
public class AlipayModel extends BaseBean {

    /**
     * alipay_name : Tina
     * alipay_account : 15298670933
     * can_update_alipay : false
     * id_card : 320826199005261220
     */

    private String alipay_name;
    private String alipay_account;
    private boolean can_update_alipay;
    private String id_card;

    public String getAlipay_name() {
        return alipay_name;
    }

    public void setAlipay_name(String alipay_name) {
        this.alipay_name = alipay_name;
    }

    public String getAlipay_account() {
        return alipay_account;
    }

    public void setAlipay_account(String alipay_account) {
        this.alipay_account = alipay_account;
    }

    public boolean isCan_update_alipay() {
        return can_update_alipay;
    }

    public void setCan_update_alipay(boolean can_update_alipay) {
        this.can_update_alipay = can_update_alipay;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }
}
