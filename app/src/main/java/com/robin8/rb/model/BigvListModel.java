package com.robin8.rb.model;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2019/1/7. */

public class BigvListModel extends BaseBean{

    /**
     * errors : 0
     * list : [{"id":1,"name":"寻找大V","description":"推荐YSL黑鸦片","img_url":"https://cdn.robin8.net/ysl.jpeg","user_id":829}]
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{

        /**
         * id : 1
         * name : 寻找大V
         * description : 全新黑鸦片香水，重新诠释大胆无畏的摇滚风格，以前所未有的个性奢华氛围，释放女人灵魂中追寻真实自我的渴望，专属于YSL 女人黑色华丽摇滚的前卫刺激，无限魅惑的性感挑逗。独特的黑色浓郁咖啡香调，喷上后就有种Rock n roll 的沉醉感觉，然后是印度茉莉和白色橙花的温婉清新，最后是甜蜜的香草调，轻柔优雅。只轻轻触碰，如咖啡因般令人上瘾！
         *圣罗兰黑色奥飘茗女士香水
         * img_url : https://cdn.robin8.net/ysl.jpeg
         * user_id : 829
         * pre_kols_count : 20
         * notice : 你牛，你上
         * status : passed
         * price_range : ¥2000--¥100000
         * terrace_names : 公众号
         * brand_info : {"name":"PD","avatar_url":null}
         * start_at : 2019-01-19T15:20:30+08:00
         * end_at : 2019-02-08T15:20:30+08:00
         * trademark : {"id":1,"name":"YSL","description":"YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。"}
         */

        private int id;
        private String name;
        private String description;
        private String img_url;
        private int user_id;
        private int pre_kols_count;
        private String notice;
        private String status;
        private String status_zh;
        private String price_range;
        private String terrace_names;
        private BrandInfoBean brand_info;
        private String start_at;
        private String end_at;
        private TrademarkBean trademark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getPre_kols_count() {
            return pre_kols_count;
        }

        public void setPre_kols_count(int pre_kols_count) {
            this.pre_kols_count = pre_kols_count;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice_range() {
            return price_range;
        }

        public void setPrice_range(String price_range) {
            this.price_range = price_range;
        }

        public String getTerrace_names() {
            return terrace_names;
        }

        public void setTerrace_names(String terrace_names) {
            this.terrace_names = terrace_names;
        }

        public BrandInfoBean getBrand_info() {
            return brand_info;
        }

        public void setBrand_info(BrandInfoBean brand_info) {
            this.brand_info = brand_info;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public TrademarkBean getTrademark() {
            return trademark;
        }

        public void setTrademark(TrademarkBean trademark) {
            this.trademark = trademark;
        }

        public String getStatus_zh() {
            return status_zh;
        }

        public void setStatus_zh(String status_zh) {
            this.status_zh = status_zh;
        }

        public static class BrandInfoBean {
            /**
             * name : PD
             * avatar_url : null
             */

            private String name;
            private Object avatar_url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(Object avatar_url) {
                this.avatar_url = avatar_url;
            }
        }

        public static class TrademarkBean {
            /**
             * id : 1
             * name : YSL
             * description : YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。
             */

            private int id;
            private String name;
            private String description;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
