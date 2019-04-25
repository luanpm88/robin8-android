package com.robin8.rb.ui.model;

/**
 * @author Figo
 * @date 2016/7/22
 */
public class WithdrawBean extends BaseBean {

    /*{
        "error": 0,
            "withdraw": {
        "real_name": "阿克",
                "credits": 102,
                "withdraw_type": "alipay",
                "alipay_no": "123456",
                "bank_name": null,
                "bank_no": null,
                "status": "pending",
                "remark": null,
                "created_at": "2016-07-22T09:55:04+08:00"
    }
    }*/

    private Withdraw withdraw;

    public Withdraw getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Withdraw withdraw) {
        this.withdraw = withdraw;
    }

    public class Withdraw{
        private String real_name;
        private String credits;
        private String withdraw_type;
        private String alipay_no;
        private String bank_name;
        private String bank_no;
        private String status;
        private String remark;
        private String created_at;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
