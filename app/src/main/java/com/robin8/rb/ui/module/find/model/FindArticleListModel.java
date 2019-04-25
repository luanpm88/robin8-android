package com.robin8.rb.ui.module.find.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2018/4/3. */

public class FindArticleListModel extends BaseBean{


    private List<List<String>> labels;
    private List<ListBean> list;

    public List<List<String>> getLabels() {
        return labels;
    }

    public void setLabels(List<List<String>> labels) {
        this.labels = labels;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * post_id : 4224811821095271
         * avatar_url : https://tvax4.sinaimg.cn/crop.25.0.1192.1192.50/d3ff93e0ly8fk9unssz6rj20yi0x4wfx.jpg
         * user_name : hollandGin
         * post_date : 2018-04-03 20:04:54 +0800
         * title : 我大王就是死，热死在这里！跳下去！也不要去湿哒哒的地方一次！
         洗衣机真舒服，嘻嘻。
         （此后大王已被禁止踏入洗衣机[二哈]） ​
         * content : null
         * pics : [["http://ww3.sinaimg.cn/thumbnail/d3ff93e0gy1fpzq0b22nfj20qo0vygr2.jpg"],["http://ww3.sinaimg.cn/large/d3ff93e0gy1fpzq0b22nfj20qo0vygr2.jpg"]]
         * tag : appliances
         * reads_count : 0
         * likes_count : 16
         * collects_count : 0
         * forwards_count : 0
         * is_liked : true
         * is_collected : true
         */

        private String post_id;
        private String avatar_url;
        private String user_name;
        private String post_date;
        private String title;
        private Object content;
        private String tag;
        private int reads_count;
        private int likes_count;
        private int collects_count;
        private int forwards_count;
        private boolean is_liked;
        private boolean is_collected;
        private String forward_url;
        private List<List<String>> pics;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPost_date() {
            return post_date;
        }

        public void setPost_date(String post_date) {
            this.post_date = post_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getReads_count() {
            return reads_count;
        }

        public void setReads_count(int reads_count) {
            this.reads_count = reads_count;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getCollects_count() {
            return collects_count;
        }

        public void setCollects_count(int collects_count) {
            this.collects_count = collects_count;
        }

        public int getForwards_count() {
            return forwards_count;
        }

        public void setForwards_count(int forwards_count) {
            this.forwards_count = forwards_count;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public boolean isIs_collected() {
            return is_collected;
        }

        public void setIs_collected(boolean is_collected) {
            this.is_collected = is_collected;
        }

        public List<List<String>> getPics() {
            return pics;
        }

        public void setPics(List<List<String>> pics) {
            this.pics = pics;
        }

        public String getForward_url() {
            return forward_url;
        }

        public void setForward_url(String forward_url) {
            this.forward_url = forward_url;
        }
    }
}
