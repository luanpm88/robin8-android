package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 Created by zc on 2018/3/8. */

public class CollectMoneyModel extends BaseBean{

    /**
     * total_count : 2064
     * list : [{"kol_id":110829,"kol_name":"138****1200","avatar_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIgg9kzOCcHavXuPfTMvwIeIqBWCEOMpqvawl8cFOniagEExiaHfXdcEuMWrwKxTtynIB2J0M3f2VoQ/0","campaign_invites_count":22,"amount":"20.0"},{"kol_id":113130,"kol_name":"157****0147","avatar_url":"http://wx.qlogo.cn/mmopen/vi_32/l98JXX5gEZoJYd6xuNBFsicmmYpLia4bRfecUpEFWqT88riaK8uPWw7ia2URUEWpyVHiaXvrClxnSsI1qiaF5rVHgFlQ/0","campaign_invites_count":1,"amount":"20.0"},{"kol_id":91923,"kol_name":"136****0988","avatar_url":null,"campaign_invites_count":0,"amount":"20.0"},{"kol_id":80694,"kol_name":"139****1463","avatar_url":"http:///uploads/kol/avatar/80694/635f2fec4e!avatar","campaign_invites_count":330,"amount":"18.0"},{"kol_id":77266,"kol_name":"159****9572","avatar_url":"http:///uploads/kol/avatar/77266/ba8c72a526.jpg!avatar","campaign_invites_count":14,"amount":"18.0"},{"kol_id":113684,"kol_name":"\t王兴昌\t","avatar_url":"http:///uploads/kol/avatar/113684/b6622fcc24.jpg!avatar","campaign_invites_count":16,"amount":"14.0"},{"kol_id":103850,"kol_name":"BLEACH","avatar_url":"http://wx.qlogo.cn/mmopen/vyIdUlr1et2rZqic7bZUsSib04LoRialosxKrYtJXmLdRMZJS2TXCjJT2sIZXIKvLCYRwRpF6vQRiaJyLKJdnKbzpnyvkqzoq5go/0","campaign_invites_count":34,"amount":"14.0"},{"kol_id":28333,"kol_name":"13901906099","avatar_url":null,"campaign_invites_count":8,"amount":"14.0"},{"kol_id":61624,"kol_name":"言又?\t","avatar_url":"http:///uploads/kol/avatar/61624/1022e633da.jpg!avatar","campaign_invites_count":5,"amount":"12.0"},{"kol_id":56069,"kol_name":"150****6562","avatar_url":null,"campaign_invites_count":19,"amount":"12.0"}]
     */

    private int total_count;
    private List<ListBean> list;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * kol_id : 110829
         * kol_name : 138****1200
         * avatar_url : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIgg9kzOCcHavXuPfTMvwIeIqBWCEOMpqvawl8cFOniagEExiaHfXdcEuMWrwKxTtynIB2J0M3f2VoQ/0
         * campaign_invites_count : 22
         * amount : 20.0
         */

        private int kol_id;
        private String kol_name;
        private String avatar_url;
        private int campaign_invites_count;
        private String amount;

        public int getKol_id() {
            return kol_id;
        }

        public void setKol_id(int kol_id) {
            this.kol_id = kol_id;
        }

        public String getKol_name() {
            return kol_name;
        }

        public void setKol_name(String kol_name) {
            this.kol_name = kol_name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getCampaign_invites_count() {
            return campaign_invites_count;
        }

        public void setCampaign_invites_count(int campaign_invites_count) {
            this.campaign_invites_count = campaign_invites_count;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
