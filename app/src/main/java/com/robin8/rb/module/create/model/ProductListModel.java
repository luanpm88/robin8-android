package com.robin8.rb.module.create.model;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/9/12.
 */
public class ProductListModel extends BaseBean {

    /**
     * id : 3
     * sku_id : 10382210186
     * img_url : http://img14.360buyimg.com/n1/jfs/t2620/95/3883443021/123619/646c40f2/579e0aa5Nc84d66f2.jpg
     * material_url : http://item.jd.com/10382210186.html
     * goods_name : 瑞银信 瑞刷刷卡器POS机手机刷卡器 收款机手机POS机 移动POS机 信用卡 瑞刷秒到账-韵达包邮-激活刷1000好评返购机款 0.49费率带积分
     * shop_id : 155061
     * unit_price : 8.88
     * start_date : 2016-05-31
     * end_date : 2018-01-28
     * kol_commision_wl : 4.97
     * category : digital
     * category_label : null
     */

    private List<CpsMaterialsBean> cps_materials;

    public List<CpsMaterialsBean> getCps_materials() {
        return cps_materials;
    }

    public void setCps_materials(List<CpsMaterialsBean> cps_materials) {
        this.cps_materials = cps_materials;
    }

    public static class CpsMaterialsBean implements Serializable {
        private int id;
        private String sku_id;
        private String img_url;
        private String material_url;
        private String goods_name;
        private int shop_id;
        private double unit_price;
        private String start_date;
        private String end_date;
        private double kol_commision_wl;
        private String category;
        private String category_label;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSku_id() {
            return sku_id;
        }

        public void setSku_id(String sku_id) {
            this.sku_id = sku_id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getMaterial_url() {
            return material_url;
        }

        public void setMaterial_url(String material_url) {
            this.material_url = material_url;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public double getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(double unit_price) {
            this.unit_price = unit_price;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public double getKol_commision_wl() {
            return kol_commision_wl;
        }

        public void setKol_commision_wl(double kol_commision_wl) {
            this.kol_commision_wl = kol_commision_wl;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategory_label() {
            return category_label;
        }

        public void setCategory_label(String category_label) {
            this.category_label = category_label;
        }
    }
}
