package com.robin8.rb.indiana.model;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;

/**
 * Created by IBM on 2016/7/25.
 */
public class OrderBean extends BaseBean implements Serializable  {
    private int id;
    private String code;
    private String created_at;
    private int number;
    private int credits;
    private KOL kol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public KOL getKol() {
        return kol;
    }

    public void setKol(KOL kol) {
        this.kol = kol;
    }


    public class KOL implements Serializable {

        private int id;
        private String name;
        private String avatar_url;

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

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
