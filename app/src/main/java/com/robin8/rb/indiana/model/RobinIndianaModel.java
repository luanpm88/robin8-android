package com.robin8.rb.indiana.model;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class RobinIndianaModel extends BaseBean {

    private List<IndianaActivity> activities;

    public List<IndianaActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<IndianaActivity> activities) {
        this.activities = activities;
    }

    public class IndianaActivity implements Serializable {
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getTotal_number() {
            return total_number;
        }

        public void setTotal_number(int total_number) {
            this.total_number = total_number;
        }

        public int getActual_number() {
            return actual_number;
        }

        public void setActual_number(int actual_number) {
            this.actual_number = actual_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPublished_at() {
            return published_at;
        }

        public void setPublished_at(String published_at) {
            this.published_at = published_at;
        }

        public String getPoster_url() {
            return poster_url;
        }

        public void setPoster_url(String poster_url) {
            this.poster_url = poster_url;
        }

        private int id;
        private String name;
        private String code;
        private int total_number;
        private int actual_number;
        private String status;
        private String published_at;
        private String poster_url;
    }
}
