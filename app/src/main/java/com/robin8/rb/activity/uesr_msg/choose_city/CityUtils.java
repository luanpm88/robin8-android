package com.robin8.rb.activity.uesr_msg.choose_city;

import java.util.ArrayList;
import java.util.List;

/**
 Created by zc on 2018/10/26. */

public class CityUtils {

    private static final String[] CITYS = {"热门城市", "A-F", "GHIJ", "KLNNM", "OPQR", "STUV", "WXYZ"};

    public static List<CityBean> getCityList() {
        List<CityBean> dataList = new ArrayList<>();
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("北京", CityBean.ITEM));
//        dataList.add(new CityBean("上海", CityBean.ITEM));
//        dataList.add(new CityBean("深圳", CityBean.ITEM));
//        dataList.add(new CityBean("广州", CityBean.ITEM));
//        dataList.add(new CityBean("杭州", CityBean.ITEM));
//        dataList.add(new CityBean("成都", CityBean.ITEM));
//        dataList.add(new CityBean("南京", CityBean.ITEM));
//        dataList.add(new CityBean("武汉", CityBean.ITEM));
//        dataList.add(new CityBean("西安", CityBean.ITEM));
//        dataList.add(new CityBean("厦门", CityBean.ITEM));
//        dataList.add(new CityBean("长沙", CityBean.ITEM));
//        dataList.add(new CityBean("天津", CityBean.ITEM));
//        dataList.add(new CityBean("苏州", CityBean.ITEM));
//
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("鞍山", CityBean.ITEM));
//        dataList.add(new CityBean("蚌埠", CityBean.ITEM));
//        dataList.add(new CityBean("保定", CityBean.ITEM));
//        dataList.add(new CityBean("北京", CityBean.ITEM));
//        dataList.add(new CityBean("长春", CityBean.ITEM));
//        dataList.add(new CityBean("成都", CityBean.ITEM));
//        dataList.add(new CityBean("重庆", CityBean.ITEM));
//        dataList.add(new CityBean("长沙", CityBean.ITEM));
//        dataList.add(new CityBean("常熟", CityBean.ITEM));
//        dataList.add(new CityBean("朝阳", CityBean.ITEM));
//        dataList.add(new CityBean("常州", CityBean.ITEM));
//        dataList.add(new CityBean("东莞", CityBean.ITEM));
//        dataList.add(new CityBean("大连", CityBean.ITEM));
//        dataList.add(new CityBean("东营", CityBean.ITEM));
//        dataList.add(new CityBean("德州", CityBean.ITEM));
//        dataList.add(new CityBean("佛山", CityBean.ITEM));
//        dataList.add(new CityBean("福州", CityBean.ITEM));
//
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("桂林", CityBean.ITEM));
//        dataList.add(new CityBean("贵阳", CityBean.ITEM));
//        dataList.add(new CityBean("广州", CityBean.ITEM));
//        dataList.add(new CityBean("哈尔滨",CityBean.ITEM));
//        dataList.add(new CityBean("合肥", CityBean.ITEM));
//        dataList.add(new CityBean("呼和浩特", CityBean.ITEM));
//        dataList.add(new CityBean("海口",CityBean.ITEM));
//        dataList.add(new CityBean("杭州",CityBean.ITEM));
//        dataList.add(new CityBean("惠州",CityBean.ITEM));
//        dataList.add(new CityBean("湖州",CityBean.ITEM));
//        dataList.add(new CityBean("金华",CityBean.ITEM));
//        dataList.add(new CityBean("江门",CityBean.ITEM));
//        dataList.add(new CityBean("济南",CityBean.ITEM));
//        dataList.add(new CityBean("济宁",CityBean.ITEM));
//        dataList.add(new CityBean("嘉兴", CityBean.ITEM));
//        dataList.add(new CityBean("江阴", CityBean.ITEM));
//        dataList.add(new CityBean("ss", CityBean.ITEM));
//        dataList.add(new CityBean("ss", CityBean.ITEM));
//
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("昆明", CityBean.ITEM));
//        dataList.add(new CityBean("昆山", CityBean.ITEM));
//        dataList.add(new CityBean("聊城", CityBean.ITEM));
//        dataList.add(new CityBean("廊坊", CityBean.ITEM));
//        dataList.add(new CityBean("丽水", CityBean.ITEM));
//        dataList.add(new CityBean("洛阳", CityBean.ITEM));
//        dataList.add(new CityBean("临沂", CityBean.ITEM));
//        dataList.add(new CityBean("龙岩", CityBean.ITEM));
//        dataList.add(new CityBean("连云港",CityBean.ITEM));
//        dataList.add(new CityBean("兰州", CityBean.ITEM));
//        dataList.add(new CityBean("柳州", CityBean.ITEM));
//        dataList.add(new CityBean("绵羊", CityBean.ITEM));
//        dataList.add(new CityBean("宁波", CityBean.ITEM));
//        dataList.add(new CityBean("南昌", CityBean.ITEM));
//        dataList.add(new CityBean("南京", CityBean.ITEM));
//        dataList.add(new CityBean("南宁", CityBean.ITEM));
//        dataList.add(new CityBean("南通", CityBean.ITEM));
//        dataList.add(new CityBean("ss", CityBean.ITEM));
//
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("青岛", CityBean.ITEM));
//        dataList.add(new CityBean("秦皇岛",CityBean.ITEM));
//        dataList.add(new CityBean("泉州", CityBean.ITEM));
//        dataList.add(new CityBean("日照", CityBean.ITEM));
//        dataList.add(new CityBean("ss", CityBean.ITEM));
//        dataList.add(new CityBean("ss", CityBean.ITEM));
//
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("上海", CityBean.ITEM));
//        dataList.add(new CityBean("石家庄", CityBean.ITEM));
//        dataList.add(new CityBean("汕头", CityBean.ITEM));
//        dataList.add(new CityBean("绍兴", CityBean.ITEM));
//        dataList.add(new CityBean("沈阳", CityBean.ITEM));
//        dataList.add(new CityBean("三亚", CityBean.ITEM));
//        dataList.add(new CityBean("深圳", CityBean.ITEM));
//        dataList.add(new CityBean("苏州", CityBean.ITEM));
//        dataList.add(new CityBean("天津", CityBean.ITEM));
//        dataList.add(new CityBean("唐山", CityBean.ITEM));
//        dataList.add(new CityBean("台州", CityBean.ITEM));
//        dataList.add(new CityBean("太原", CityBean.ITEM));
//        dataList.add(new CityBean("热门城市",CityBean.SECTION));
//        dataList.add(new CityBean("潍坊", CityBean.ITEM));
//        dataList.add(new CityBean("武汉", CityBean.ITEM));
//        dataList.add(new CityBean("芜湖", CityBean.ITEM));
//        dataList.add(new CityBean("威海", CityBean.ITEM));
//        dataList.add(new CityBean("乌鲁木齐",CityBean.ITEM));
//        dataList.add(new CityBean("无锡", CityBean.ITEM));
//        dataList.add(new CityBean("温州", CityBean.ITEM));
//        dataList.add(new CityBean("西安", CityBean.ITEM));
//        dataList.add(new CityBean("香港", CityBean.ITEM));
//        dataList.add(new CityBean("厦门", CityBean.ITEM));
//        dataList.add(new CityBean("西宁", CityBean.ITEM));
//        dataList.add(new CityBean("邢台", CityBean.ITEM));
//        dataList.add(new CityBean("徐州", CityBean.ITEM));
//        dataList.add(new CityBean("银川", CityBean.ITEM));
//        dataList.add(new CityBean("盐城", CityBean.ITEM));
//        dataList.add(new CityBean("烟台", CityBean.ITEM));
//        dataList.add(new CityBean("扬州", CityBean.ITEM));
//        dataList.add(new CityBean("珠海", CityBean.ITEM));
//        dataList.add(new CityBean("张家港",CityBean.ITEM));
//        dataList.add(new CityBean("肇庆", CityBean.ITEM));
//        dataList.add(new CityBean("中山", CityBean.ITEM));
//        dataList.add(new CityBean("郑州", CityBean.ITEM));
        return dataList;
    }
}
