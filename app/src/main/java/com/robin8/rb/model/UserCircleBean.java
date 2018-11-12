package com.robin8.rb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 Created by zc on 2018/10/30. */

public class UserCircleBean extends BaseBean{

    private List<CirclesListBean> circles_list;
    private List<TerracesListBean> terraces_list;
    private List<String> ages_list;
    private List<String> weibo_auth_list;

    public List<CirclesListBean> getCircles_list() {
        return circles_list;
    }

    public void setCircles_list(List<CirclesListBean> circles_list) {
        this.circles_list = circles_list;
    }

    public List<TerracesListBean> getTerraces_list() {
        return terraces_list;
    }

    public void setTerraces_list(List<TerracesListBean> terraces_list) {
        this.terraces_list = terraces_list;
    }

    public List<String> getAges_list() {
        return ages_list;
    }

    public void setAges_list(List<String> ages_list) {
        this.ages_list = ages_list;
    }

    public List<String> getWeibo_auth_list() {
        return weibo_auth_list;
    }

    public void setWeibo_auth_list(List<String> weibo_auth_list) {
        this.weibo_auth_list = weibo_auth_list;
    }

    public static class CirclesListBean {
        /**
         * id : 1
         * label : 二次元
         * color : #FFBFAD
         */

        private int id;
        private String label;
        private String color;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static class TerracesListBean {
        /**
         * id : 1
         * name : 微信
         * address : http://img.robin8.net/wechat.png
         * na me : 豆瓣
         */

        private int id;
        private String name;
        private String address;
        @SerializedName("na me")
        private String _$NaMe69; // FIXME check this code

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String get_$NaMe69() {
            return _$NaMe69;
        }

        public void set_$NaMe69(String _$NaMe69) {
            this._$NaMe69 = _$NaMe69;
        }
    }
}
