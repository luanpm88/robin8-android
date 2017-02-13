package com.robin8.rb.module.first.model;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/10/12.
 */
public class AnalysisResultModel extends BaseBean {


    private CampaignInputBean campaign_input;

    private AnalysisInfoBean analysis_info;

    public CampaignInputBean getCampaign_input() {
        return campaign_input;
    }

    public void setCampaign_input(CampaignInputBean campaign_input) {
        this.campaign_input = campaign_input;
    }

    public AnalysisInfoBean getAnalysis_info() {
        return analysis_info;
    }

    public void setAnalysis_info(AnalysisInfoBean analysis_info) {
        this.analysis_info = analysis_info;
    }

    public static class CampaignInputBean implements Serializable {
        private String id;
        private String name;
        private String description;
        private String status;
        private String url;
        private String img_url;
        private String per_budget_type;
        private double per_action_budget;
        private int budget;
        private String remark;
        private String cpi_example_screenshot;
        private String deadline;
        private String start_time;
        private String cal_settle_time;
        private int avail_click;
        private int total_click;
        private int take_budget;
        private int share_times;
        private String tag_labels;
        private String region;
        private String gender;
        private String age;
        private List<List<String>> stats_data;
        private List<String> tags;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getPer_budget_type() {
            return per_budget_type;
        }

        public void setPer_budget_type(String per_budget_type) {
            this.per_budget_type = per_budget_type;
        }

        public double getPer_action_budget() {
            return per_action_budget;
        }

        public void setPer_action_budget(double per_action_budget) {
            this.per_action_budget = per_action_budget;
        }

        public int getBudget() {
            return budget;
        }

        public void setBudget(int budget) {
            this.budget = budget;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCpi_example_screenshot() {
            return cpi_example_screenshot;
        }

        public void setCpi_example_screenshot(String cpi_example_screenshot) {
            this.cpi_example_screenshot = cpi_example_screenshot;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getCal_settle_time() {
            return cal_settle_time;
        }

        public void setCal_settle_time(String cal_settle_time) {
            this.cal_settle_time = cal_settle_time;
        }

        public int getAvail_click() {
            return avail_click;
        }

        public void setAvail_click(int avail_click) {
            this.avail_click = avail_click;
        }

        public int getTotal_click() {
            return total_click;
        }

        public void setTotal_click(int total_click) {
            this.total_click = total_click;
        }

        public int getTake_budget() {
            return take_budget;
        }

        public void setTake_budget(int take_budget) {
            this.take_budget = take_budget;
        }

        public int getShare_times() {
            return share_times;
        }

        public void setShare_times(int share_times) {
            this.share_times = share_times;
        }

        public String getTag_labels() {
            return tag_labels;
        }

        public void setTag_labels(String tag_labels) {
            this.tag_labels = tag_labels;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public List<List<String>> getStats_data() {
            return stats_data;
        }

        public void setStats_data(List<List<String>> stats_data) {
            this.stats_data = stats_data;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    public static class AnalysisInfoBean implements Serializable {
        private String text;
        /**
         * label : 整体
         * probability : 0.04
         */

        private List<PieDataBean> keywords;
        /**
         * label : negative
         * probability : 0.51
         */

        private List<PieDataBean> sentiment;
        /**
         * label : 从业人员
         * freq : 3
         */

        private List<ColumnarDataBean> persons_brands;
        /**
         * label : 市场
         * freq : 5
         */

        private List<ColumnarDataBean> products;
        private List<String> cities;
        /**
         * label : 航空
         * probability : 0.54
         */

        private List<PieDataBean> categories;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<PieDataBean> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<PieDataBean> keywords) {
            this.keywords = keywords;
        }

        public List<PieDataBean> getSentiment() {
            return sentiment;
        }

        public void setSentiment(List<PieDataBean> sentiment) {
            this.sentiment = sentiment;
        }

        public List<ColumnarDataBean> getPersons_brands() {
            return persons_brands;
        }

        public void setPersons_brands(List<ColumnarDataBean> persons_brands) {
            this.persons_brands = persons_brands;
        }

        public List<ColumnarDataBean> getProducts() {
            return products;
        }

        public void setProducts(List<ColumnarDataBean> products) {
            this.products = products;
        }

        public List<String> getCities() {
            return cities;
        }

        public void setCities(List<String> cities) {
            this.cities = cities;
        }

        public List<PieDataBean> getCategories() {
            return categories;
        }

        public void setCategories(List<PieDataBean> categories) {
            this.categories = categories;
        }

    }
}
