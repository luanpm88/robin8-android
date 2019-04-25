package com.robin8.rb.ui.module.bigv.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2019/1/18. */

public class BigvDetailModel extends BaseBean {

    /**
     creation : {"base_info":{"id":1,"name":"寻找大V","description":"全新黑鸦片香水，重新诠释大胆无畏的摇滚风格，以前所未有的个性奢华氛围，释放女人灵魂中追寻真实自我的渴望，专属于YSL 女人黑色华丽摇滚的前卫刺激，无限魅惑的性感挑逗。独特的黑色浓郁咖啡香调，喷上后就有种Rock n roll 的沉醉感觉，然后是印度茉莉和白色橙花的温婉清新，最后是甜蜜的香草调，轻柔优雅。只轻轻触碰，如咖啡因般令人上瘾！\n*圣罗兰黑色奥飘茗女士香水","img_url":"https://cdn.robin8.net/ysl.jpeg","user_id":829,"pre_kols_count":20,"notice":"你牛，你上","price_range":"¥2000--¥100000","terrace_names":"公众号","brand_info":{"name":"PD","avatar_url":null},"start_at":"2019-01-19T15:20:30+08:00","end_at":"2019-02-08T15:20:30+08:00","trademark":{"id":1,"name":"YSL","description":"YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。"}},"selected_kols":[{"kol_id":116045,"plateform_name":"public_wechat_account","plateform_uuid":"weikagirl","name":"卡妞范儿","avatar_url":"http://wx.qlogo.cn/mmhead/Q3auHgzwzM7ot5ItQtkQMM1nrb5nnQ7iaNdlQAV4w8aibKsPcibVg203g/132","desc":"深圳量子云科技有限公司","quoted":true,"tenders":[{"id":1,"kol_id":116045,"creation_id":1,"from_terrace":"wechat_public_account","price":30000,"title":null,"link":null,"image_url":null,"description":null}]}]}
     */

    private CreationBean creation;

    public CreationBean getCreation() {
        return creation;
    }

    public void setCreation(CreationBean creation) {
        this.creation = creation;
    }

    public static class CreationBean {
        /**
         base_info : {"id":1,"name":"寻找大V","description":"全新黑鸦片香水，重新诠释大胆无畏的摇滚风格，以前所未有的个性奢华氛围，释放女人灵魂中追寻真实自我的渴望，专属于YSL 女人黑色华丽摇滚的前卫刺激，无限魅惑的性感挑逗。独特的黑色浓郁咖啡香调，喷上后就有种Rock n roll 的沉醉感觉，然后是印度茉莉和白色橙花的温婉清新，最后是甜蜜的香草调，轻柔优雅。只轻轻触碰，如咖啡因般令人上瘾！\n*圣罗兰黑色奥飘茗女士香水","img_url":"https://cdn.robin8.net/ysl.jpeg","user_id":829,"pre_kols_count":20,"notice":"你牛，你上","price_range":"¥2000--¥100000","terrace_names":"公众号","brand_info":{"name":"PD","avatar_url":null},"start_at":"2019-01-19T15:20:30+08:00","end_at":"2019-02-08T15:20:30+08:00","trademark":{"id":1,"name":"YSL","description":"YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。"}}
         selected_kols : [{"kol_id":116045,"plateform_name":"public_wechat_account","plateform_uuid":"weikagirl","name":"卡妞范儿","avatar_url":"http://wx.qlogo.cn/mmhead/Q3auHgzwzM7ot5ItQtkQMM1nrb5nnQ7iaNdlQAV4w8aibKsPcibVg203g/132","desc":"深圳量子云科技有限公司","quoted":true,"tenders":[{"id":1,"kol_id":116045,"creation_id":1,"from_terrace":"wechat_public_account","price":30000,"title":null,"link":null,"image_url":null,"description":null}]}]
         */
        private String my_tender_status;
        private BaseInfoBean base_info;
        private List<SelectedKolsBean> selected_kols;
        private List<MyTendersBean> my_tenders;

        public BaseInfoBean getBase_info() {
            return base_info;
        }

        public void setBase_info(BaseInfoBean base_info) {
            this.base_info = base_info;
        }

        public List<SelectedKolsBean> getSelected_kols() {
            return selected_kols;
        }

        public void setSelected_kols(List<SelectedKolsBean> selected_kols) {
            this.selected_kols = selected_kols;
        }

        public List<MyTendersBean> getMy_tenders() {
            return my_tenders;
        }

        public void setMy_tenders(List<MyTendersBean> my_tenders) {
            this.my_tenders = my_tenders;
        }

        public String getMy_tender_status() {
            return my_tender_status;
        }

        public void setMy_tender_status(String my_tender_status) {
            this.my_tender_status = my_tender_status;
        }

        public static class BaseInfoBean implements Serializable{

            /**
             id : 12
             name : 寻找大V_finished
             description : 推荐YSL黑鸦片
             img_url : https://cdn.robin8.net/ysl.jpeg
             user_id : 829
             pre_kols_count : 20
             notice : 你牛，你上
             price_range : ¥2000--¥100000
             terrace_names : 公众号，wechat
             terrace_infos : [{"id":5,"name":"公众号","short_name":"公众号","avatar":"http://img.robin8.net/public_wechat_account.png"},{"id":6,"name":"wechat","short_name":"wechat","avatar":"http://img.robin8.net/wechat.png"}]
             brand_info : {"name":"PD","avatar_url":null}
             start_at : 2019-01-23T11:22:07+08:00
             end_at : 2019-02-12T11:22:07+08:00
             trademark : {"id":1,"name":"YSL","description":"YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。"}
             */

            private int id;
            private String name;
            private String description;
            private String img_url;
            private int user_id;
            private int pre_kols_count;
            private String notice;
            private String price_range;
            private String terrace_names;
            private String status;
            private BrandInfoBean brand_info;
            private String start_at;
            private String end_at;
            private TrademarkBean trademark;
            private List<TerraceInfosBean> terrace_infos;

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

            public List<TerraceInfosBean> getTerrace_infos() {
                return terrace_infos;
            }

            public void setTerrace_infos(List<TerraceInfosBean> terrace_infos) {
                this.terrace_infos = terrace_infos;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class BrandInfoBean implements Serializable{
                /**
                 name : PD
                 avatar_url : null
                 */

                private String name;
                private String avatar_url;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }
            }

            public static class TrademarkBean implements Serializable{
                /**
                 id : 1
                 name : YSL
                 description : YSL（Yves Saint laurent）的简称，中文译名圣罗兰，是法国著名奢侈品牌，由1936年8月1日出生于法属北非阿尔及利亚的伊夫圣罗兰先生创立，主要有时装、护肤品，香水，箱包，眼镜，配饰等。
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

            public static class TerraceInfosBean implements Serializable {
                /**
                 id : 5
                 name : 公众号
                 short_name : 公众号
                 avatar : http://img.robin8.net/public_wechat_account.png
                 */

                private int id;
                private String name;
                private String short_name;
                private String avatar;
                private String address;
                private float price;


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

                public String getShort_name() {
                    return short_name;
                }

                public void setShort_name(String short_name) {
                    this.short_name = short_name;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }
        }

        public static class SelectedKolsBean implements Serializable{
            /**
             kol_id : 116045
             plateform_name : public_wechat_account
             plateform_uuid : weikagirl
             name : 卡妞范儿
             avatar_url : http://wx.qlogo.cn/mmhead/Q3auHgzwzM7ot5ItQtkQMM1nrb5nnQ7iaNdlQAV4w8aibKsPcibVg203g/132
             desc : 深圳量子云科技有限公司
             quoted : true
             tenders : [{"id":1,"kol_id":116045,"creation_id":1,"from_terrace":"wechat_public_account","price":30000,"title":null,"link":null,"image_url":null,"description":null}]
             */

            private int kol_id;
            private String plateform_name;
            private String plateform_uuid;
            private String name;
            private String avatar_url;
            private String desc;
            private boolean quoted;
            private List<TendersBean> tenders;

            public int getKol_id() {
                return kol_id;
            }

            public void setKol_id(int kol_id) {
                this.kol_id = kol_id;
            }

            public String getPlateform_name() {
                return plateform_name;
            }

            public void setPlateform_name(String plateform_name) {
                this.plateform_name = plateform_name;
            }

            public String getPlateform_uuid() {
                return plateform_uuid;
            }

            public void setPlateform_uuid(String plateform_uuid) {
                this.plateform_uuid = plateform_uuid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public boolean isQuoted() {
                return quoted;
            }

            public void setQuoted(boolean quoted) {
                this.quoted = quoted;
            }

            public List<TendersBean> getTenders() {
                return tenders;
            }

            public void setTenders(List<TendersBean> tenders) {
                this.tenders = tenders;
            }

            public static class TendersBean implements Serializable{
                /**
                 id : 1
                 kol_id : 116045
                 creation_id : 1
                 from_terrace : wechat_public_account
                 price : 30000.0
                 title : null
                 link : null
                 image_url : null
                 description : null
                 */

                private int id;
                private int kol_id;
                private int creation_id;
                private String from_terrace;
                private double price;
                private Object title;
                private Object link;
                private Object image_url;
                private Object description;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getKol_id() {
                    return kol_id;
                }

                public void setKol_id(int kol_id) {
                    this.kol_id = kol_id;
                }

                public int getCreation_id() {
                    return creation_id;
                }

                public void setCreation_id(int creation_id) {
                    this.creation_id = creation_id;
                }

                public String getFrom_terrace() {
                    return from_terrace;
                }

                public void setFrom_terrace(String from_terrace) {
                    this.from_terrace = from_terrace;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public Object getTitle() {
                    return title;
                }

                public void setTitle(Object title) {
                    this.title = title;
                }

                public Object getLink() {
                    return link;
                }

                public void setLink(Object link) {
                    this.link = link;
                }

                public Object getImage_url() {
                    return image_url;
                }

                public void setImage_url(Object image_url) {
                    this.image_url = image_url;
                }

                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                }
            }
        }

        public static class MyTendersBean implements Serializable {

            /**
             id : 6
             kol_id : 109050
             creation_id : 1
             from_terrace : 公众号
             price : 555
             title : 你说呢
             link : hah
             image_url : 没有
             description : 什么
             */

            private int id;
            private int kol_id;
            private int creation_id;
            private String from_terrace;
            private float price;
            private String title;
            private String link;
            private String image_url;
            private String description;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getKol_id() {
                return kol_id;
            }

            public void setKol_id(int kol_id) {
                this.kol_id = kol_id;
            }

            public int getCreation_id() {
                return creation_id;
            }

            public void setCreation_id(int creation_id) {
                this.creation_id = creation_id;
            }

            public String getFrom_terrace() {
                return from_terrace;
            }

            public void setFrom_terrace(String from_terrace) {
                this.from_terrace = from_terrace;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
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
