package com.robin8.rb.model;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class MyCampaignModel extends BaseBean{

//    public int getTotal_pages() {
//        return total_pages;
//    }
//
//    public void setTotal_pages(int total_pages) {
//        this.total_pages = total_pages;
//    }
//
//    public int getCurrent_page() {
//        return current_page;
//    }
//
//    public void setCurrent_page(int current_page) {
//        this.current_page = current_page;
//    }
//
//    public int getPer_page() {
//        return per_page;
//    }
//
//    public void setPer_page(int per_page) {
//        this.per_page = per_page;
//    }

   /* "error": 0,
            "total_pages": 1,
            "current_page": 1,
            "per_page": 10,
            "campaigns": [
    {
        "id": 772,
            "need_pay_amount": 100,
            "status": "unpay",
            "img_url": "http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/8724920c04836ee7e15d657f9192de4c.jpg",
            "name": "ddj",
            "budget": 100,
            "per_budget_type": "click",
            "per_action_budget": 0.6,
            "deadline": "2016-07-26T12:07:00+08:00",
            "start_time": "2016-07-25T12:07:00+08:00"
    },*/

//    private int total_pages;
//    private int current_page;
//    private int per_page;
    private List<LaunchRewordModel.Campaign> campaigns;

    public List<LaunchRewordModel.Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<LaunchRewordModel.Campaign> campaigns) {
        this.campaigns = campaigns;
    }
}
