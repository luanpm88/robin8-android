package com.robin8.rb.indiana.model;

import com.robin8.rb.model.BaseBean;

/**
 * Created by IBM on 2016/7/25.
 */
public class PayOrderModel extends BaseBean {
    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }
}
