package com.robin8.rb.ui.model;

import com.robin8.rb.ui.module.first.model.SocialAccountsBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2018/10/31. */

public class UserShowBean extends BaseBean {

    /**
     * kol : {"id":109050,"name":"啦啦啦哇哇哈哈呵呵啧啧","mobile_number":"15910439098","email":null,"age":16,"age_show":16,"gender":1,"job_info":"","completed_rate":0.67,"wechat_friends_count":0,"avatar_url":"http://img.robin8.net/uploads/kol/avatar/109050/9e684b637d!200","circles":[],"is_big_v":true,"is_creator":true,"weibo_account":{"id":2,"nickname":"你好","auth_type":2,"fwd_price":77,"price":87,"live_price":44,"quote_expired_at":"2020-10-02T08:00:00.000+08:00","fans_count":775,"gender":1,"content_show":"链接","remark":"哈哈","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"cities":[]},"public_wechat_account":{"id":1,"nickname":"呼啦","price":8745,"mult_price":7555,"sub_price":5874,"n_price":7555,"quote_expired_at":"2018-11-01T08:00:00.000+08:00","fans_count":48,"gender":1,"content_show":"455","remark":"","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"cities":[]},"creator":{"id":1,"price":555,"video_price":556,"fans_count":888,"gender":1,"age_range":3,"content_show":"888","remark":"58","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"terraces":[{"id":8,"name":"斗鱼","address":"http://img.robin8.net/douyu.png"},{"id":12,"name":"淘宝","address":"http://img.robin8.net/taobao.png"}],"cities":[]},"social_accounts":[{"id":30132,"provider":"weibo","uid":null,"username":"phoenixLove","homepage":"http://m.weibo.cn/u/3985670549","avatar_url":null,"brief":null,"followers_count":1,"friends_count":null,"like_count":null,"reposts_count":null,"statuses_count":null,"price":"1","search_kol_id":null,"provider_name":"微博"}]}
     */

    private KolBean kol;

    public KolBean getKol() {
        return kol;
    }

    public void setKol(KolBean kol) {
        this.kol = kol;
    }

    public static class KolBean implements Serializable{
        /**
         * id : 109050
         * name : 啦啦啦哇哇哈哈呵呵啧啧
         * mobile_number : 15910439098
         * email : null
         * age : 16
         * age_show : 16
         * gender : 1
         * job_info :
         * completed_rate : 0.67
         * wechat_friends_count : 0
         * avatar_url : http://img.robin8.net/uploads/kol/avatar/109050/9e684b637d!200
         * circles : []
         * is_big_v : true
         * is_creator : true
         * weibo_account : {"id":2,"nickname":"你好","auth_type":2,"fwd_price":77,"price":87,"live_price":44,"quote_expired_at":"2020-10-02T08:00:00.000+08:00","fans_count":775,"gender":1,"content_show":"链接","remark":"哈哈","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"cities":[]}
         * public_wechat_account : {"id":1,"nickname":"呼啦","price":8745,"mult_price":7555,"sub_price":5874,"n_price":7555,"quote_expired_at":"2018-11-01T08:00:00.000+08:00","fans_count":48,"gender":1,"content_show":"455","remark":"","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"cities":[]}
         * creator : {"id":1,"price":555,"video_price":556,"fans_count":888,"gender":1,"age_range":3,"content_show":"888","remark":"58","status":1,"circles":[{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}],"terraces":[{"id":8,"name":"斗鱼","address":"http://img.robin8.net/douyu.png"},{"id":12,"name":"淘宝","address":"http://img.robin8.net/taobao.png"}],"cities":[]}
         * social_accounts : [{"id":30132,"provider":"weibo","uid":null,"username":"phoenixLove","homepage":"http://m.weibo.cn/u/3985670549","avatar_url":null,"brief":null,"followers_count":1,"friends_count":null,"like_count":null,"reposts_count":null,"statuses_count":null,"price":"1","search_kol_id":null,"provider_name":"微博"}]
         */

        private int id;
        private String name;
        private String mobile_number;
        private String email;
        private int age;
        private String birthday;
        private int age_show;
        private int gender;
        private String job_info;
        private double completed_rate;
        private int wechat_friends_count;
        private String avatar_url;
        private boolean is_big_v;
        private boolean is_creator;
        private WeiboAccountBean weibo_account;
        private PublicWechatAccountBean public_wechat_account;
        private CreatorBean creator;
        private List<CirclesBean> circles;
        private List<SocialAccountsBean> social_accounts;
        private String city_name;
        private String country_code;
        private String country_name;

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

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

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge_show() {
            return age_show;
        }

        public void setAge_show(int age_show) {
            this.age_show = age_show;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getJob_info() {
            return job_info;
        }

        public void setJob_info(String job_info) {
            this.job_info = job_info;
        }

        public double getCompleted_rate() {
            return completed_rate;
        }

        public void setCompleted_rate(double completed_rate) {
            this.completed_rate = completed_rate;
        }

        public int getWechat_friends_count() {
            return wechat_friends_count;
        }

        public void setWechat_friends_count(int wechat_friends_count) {
            this.wechat_friends_count = wechat_friends_count;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public boolean isIs_big_v() {
            return is_big_v;
        }

        public void setIs_big_v(boolean is_big_v) {
            this.is_big_v = is_big_v;
        }

        public boolean isIs_creator() {
            return is_creator;
        }

        public void setIs_creator(boolean is_creator) {
            this.is_creator = is_creator;
        }

        public WeiboAccountBean getWeibo_account() {
            return weibo_account;
        }

        public void setWeibo_account(WeiboAccountBean weibo_account) {
            this.weibo_account = weibo_account;
        }

        public PublicWechatAccountBean getPublic_wechat_account() {
            return public_wechat_account;
        }

        public void setPublic_wechat_account(PublicWechatAccountBean public_wechat_account) {
            this.public_wechat_account = public_wechat_account;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public List<CirclesBean> getCircles() {
            return circles;
        }

        public void setCircles(List<CirclesBean> circles) {
            this.circles = circles;
        }

        public List<SocialAccountsBean> getSocial_accounts() {
            return social_accounts;
        }

        public void setSocial_accounts(List<SocialAccountsBean> social_accounts) {
            this.social_accounts = social_accounts;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public static class WeiboAccountBean {
            /**
             * id : 2
             * nickname : 你好
             * auth_type : 2
             * fwd_price : 77
             * price : 87
             * live_price : 44
             * quote_expired_at : 2020-10-02T08:00:00.000+08:00
             * fans_count : 775
             * gender : 1
             * content_show : 链接
             * remark : 哈哈
             * status : 1
             * circles : [{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}]
             * cities : []
             */

            private int id;
            private String nickname;
            private int auth_type;
            private int fwd_price;
            private int price;
            private int live_price;
            private String quote_expired_at;
            private int fans_count;
            private int gender;
            private String content_show;
            private String remark;
            private int status;
            private List<CirclesBean> circles;
            private List<String> cities;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getAuth_type() {
                return auth_type;
            }

            public void setAuth_type(int auth_type) {
                this.auth_type = auth_type;
            }

            public int getFwd_price() {
                return fwd_price;
            }

            public void setFwd_price(int fwd_price) {
                this.fwd_price = fwd_price;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getLive_price() {
                return live_price;
            }

            public void setLive_price(int live_price) {
                this.live_price = live_price;
            }

            public String getQuote_expired_at() {
                return quote_expired_at;
            }

            public void setQuote_expired_at(String quote_expired_at) {
                this.quote_expired_at = quote_expired_at;
            }

            public int getFans_count() {
                return fans_count;
            }

            public void setFans_count(int fans_count) {
                this.fans_count = fans_count;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getContent_show() {
                return content_show;
            }

            public void setContent_show(String content_show) {
                this.content_show = content_show;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<CirclesBean> getCircles() {
                return circles;
            }

            public void setCircles(List<CirclesBean> circles) {
                this.circles = circles;
            }

            public List<String> getCities() {
                return cities;
            }

            public void setCities(List<String> cities) {
                this.cities = cities;
            }
        }

        public static class PublicWechatAccountBean {
            /**
             * id : 1
             * nickname : 呼啦
             * price : 8745
             * mult_price : 7555
             * sub_price : 5874
             * n_price : 7555
             * quote_expired_at : 2018-11-01T08:00:00.000+08:00
             * fans_count : 48
             * gender : 1
             * content_show : 455
             * remark :
             * status : 1
             * circles : [{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}]
             * cities : []
             */

            private int id;
            private String nickname;
            private int price;
            private int mult_price;
            private int sub_price;
            private int n_price;
            private String quote_expired_at;
            private int fans_count;
            private int gender;
            private String content_show;
            private String remark;
            private int status;
            private List<CirclesBean> circles;
            private List<String> cities;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getMult_price() {
                return mult_price;
            }

            public void setMult_price(int mult_price) {
                this.mult_price = mult_price;
            }

            public int getSub_price() {
                return sub_price;
            }

            public void setSub_price(int sub_price) {
                this.sub_price = sub_price;
            }

            public int getN_price() {
                return n_price;
            }

            public void setN_price(int n_price) {
                this.n_price = n_price;
            }

            public String getQuote_expired_at() {
                return quote_expired_at;
            }

            public void setQuote_expired_at(String quote_expired_at) {
                this.quote_expired_at = quote_expired_at;
            }

            public int getFans_count() {
                return fans_count;
            }

            public void setFans_count(int fans_count) {
                this.fans_count = fans_count;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getContent_show() {
                return content_show;
            }

            public void setContent_show(String content_show) {
                this.content_show = content_show;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<CirclesBean> getCircles() {
                return circles;
            }

            public void setCircles(List<CirclesBean> circles) {
                this.circles = circles;
            }

            public List<String> getCities() {
                return cities;
            }

            public void setCities(List<String> cities) {
                this.cities = cities;
            }

        }

        public static class CreatorBean {
            /**
             * id : 1
             * price : 555
             * video_price : 556
             * fans_count : 888
             * gender : 1
             * age_range : 3
             * content_show : 888
             * remark : 58
             * status : 1
             * circles : [{"id":1,"label":"二次元","color":"#FFBFAD"},{"id":2,"label":"车友圈","color":"#99B8FF"},{"id":3,"label":"旅游圈","color":"#FFBAAB"},{"id":4,"label":"吃货圈","color":"#90D6FF"},{"id":5,"label":"房产圈","color":"#D7B9FF"},{"id":6,"label":"游戏圈","color":null}]
             * terraces : [{"id":8,"name":"斗鱼","address":"http://img.robin8.net/douyu.png"},{"id":12,"name":"淘宝","address":"http://img.robin8.net/taobao.png"}]
             * cities : []
             */

            private int id;
            private int price;
            private int video_price;
            private int fans_count;
            private int gender;
            private int age_range;
            private String content_show;
            private String remark;
            private int status;
            private List<CirclesBean> circles;
            private List<TerracesBean> terraces;
            private List<String> cities;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getVideo_price() {
                return video_price;
            }

            public void setVideo_price(int video_price) {
                this.video_price = video_price;
            }

            public int getFans_count() {
                return fans_count;
            }

            public void setFans_count(int fans_count) {
                this.fans_count = fans_count;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getAge_range() {
                return age_range;
            }

            public void setAge_range(int age_range) {
                this.age_range = age_range;
            }

            public String getContent_show() {
                return content_show;
            }

            public void setContent_show(String content_show) {
                this.content_show = content_show;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<CirclesBean> getCircles() {
                return circles;
            }

            public void setCircles(List<CirclesBean> circles) {
                this.circles = circles;
            }

            public List<TerracesBean> getTerraces() {
                return terraces;
            }

            public void setTerraces(List<TerracesBean> terraces) {
                this.terraces = terraces;
            }

            public List<String> getCities() {
                return cities;
            }

            public void setCities(List<String> cities) {
                this.cities = cities;
            }
            public static class TerracesBean {
                /**
                 * id : 8
                 * name : 斗鱼
                 * address : http://img.robin8.net/douyu.png
                 */

                private int id;
                private String name;
                private String address;

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
            }


        }
    }
    public static class CirclesBean implements Serializable{
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
}
