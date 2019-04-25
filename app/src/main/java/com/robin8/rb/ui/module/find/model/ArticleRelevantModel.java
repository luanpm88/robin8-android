package com.robin8.rb.ui.module.find.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2018/4/19. */

public class ArticleRelevantModel extends BaseBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * post_id : 4221215973756278
         * avatar_url : https://tvax3.sinaimg.cn/crop.0.0.512.512.50/69e273f8ly8fnvmu255jyj20e80e8wev.jpg
         * user_name : 蔡徐坤
         * post_date : 2018-03-24 21:56:17
         * title : 刚躺下…
         你们要的500w福利
         3.31号见[月亮]
         #偶像练习生##偶像练习生VIP千人见面会# ​​​
         * content : null
         * pics : [["http://ww3.sinaimg.cn/bmiddle/69e273f8ly1fpo924ba27j22yo1nyu0x.jpg"],["http://ww3.sinaimg.cn/large/69e273f8ly1fpo924ba27j22yo1nyu0x.jpg"]]
         * tag : music
         * reads_count : 554
         * likes_count : 253346
         * collects_count : 0
         * forwards_count : 296751
         * is_liked : false
         * is_collected : false
         * forward_url : elastic_articles/4221215973756278/forward
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

        public String getForward_url() {
            return forward_url;
        }

        public void setForward_url(String forward_url) {
            this.forward_url = forward_url;
        }

        public List<List<String>> getPics() {
            return pics;
        }

        public void setPics(List<List<String>> pics) {
            this.pics = pics;
        }
    }
}
