package com.robin8.rb.module.reword.bean;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/10/17.
 */
public class PersonAnalysisResultModel extends BaseBean {

    private List<GenderAnalysisBean> gender_analysis;

    private List<AgeAnalysisBean> age_analysis;

    private List<TagAnalysisBean> tag_analysis;

    private List<RegionAnalysisBean> region_analysis;

    public List<GenderAnalysisBean> getGender_analysis() {
        return gender_analysis;
    }

    public void setGender_analysis(List<GenderAnalysisBean> gender_analysis) {
        this.gender_analysis = gender_analysis;
    }

    public List<AgeAnalysisBean> getAge_analysis() {
        return age_analysis;
    }

    public void setAge_analysis(List<AgeAnalysisBean> age_analysis) {
        this.age_analysis = age_analysis;
    }

    public List<TagAnalysisBean> getTag_analysis() {
        return tag_analysis;
    }

    public void setTag_analysis(List<TagAnalysisBean> tag_analysis) {
        this.tag_analysis = tag_analysis;
    }

    public List<RegionAnalysisBean> getRegion_analysis() {
        return region_analysis;
    }

    public void setRegion_analysis(List<RegionAnalysisBean> region_analysis) {
        this.region_analysis = region_analysis;
    }

    public static class GenderAnalysisBean {
        private String name;
        private float ratio;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getRatio() {
            return ratio;
        }

        public void setRatio(float ratio) {
            this.ratio = ratio;
        }
    }

    public static class AgeAnalysisBean {
        private String name;
        private int count;
        private float ratio;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getRatio() {
            return ratio;
        }

        public void setRatio(float ratio) {
            this.ratio = ratio;
        }
    }

    public static class TagAnalysisBean {
        private String name;
        private String code;
        private int count;
        private float ratio;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getRatio() {
            return ratio;
        }

        public void setRatio(float ratio) {
            this.ratio = ratio;
        }
    }

    public static class RegionAnalysisBean {
        private String city_name;
        private String city_code;
        private String province_name;
        private String province_code;

        public RegionAnalysisBean(String province_code) {
            this.province_code = province_code;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getProvince_code() {
            return province_code;
        }

        public void setProvince_code(String province_code) {
            this.province_code = province_code;
        }
    }
}
