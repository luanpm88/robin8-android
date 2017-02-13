package com.robin8.rb.indiana.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class DetailModel extends BaseBean {
    private List<OrderBean> orders;

    public List<OrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderBean> orders) {
        this.orders = orders;
    }
}
