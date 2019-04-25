package com.robin8.rb.ui.indiana.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;

/**
 * Created by IBM on 2016/7/25.
 */
public class IndianaDetailBean extends BaseBean implements Serializable  {

    private IndianaActivity activity;
    private String[] tickets;
    private String created_at;
    private int token_number;
    private float kol_amount;

    public float getKol_amount() {
        return kol_amount;
    }

    public void setKol_amount(float kol_amount) {
        this.kol_amount = kol_amount;
    }

    public IndianaActivity getActivity() {
        return activity;
    }

    public void setActivity(IndianaActivity activity) {
        this.activity = activity;
    }

    public String[] getTickets() {
        return tickets;
    }

    public void setTickets(String[] tickets) {
        this.tickets = tickets;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getToken_number() {
        return token_number;
    }

    public void setToken_number(int token_number) {
        this.token_number = token_number;
    }

    public class IndianaActivity implements Serializable {
        private int id;
        private String name;
        private String code;
        private String poster_url;
        private String status;
        private String published_at;
        private int total_number;
        private int actual_number;
        private String description;
        private String lucky_number;
        private String draw_at;
        private String[] pictures;
        private boolean winner_self;
        private String winner_name;
        private String winner_avatar_url;
        private String winner_token_number;
        private String token_number;

        public String getWinner_token_number() {
            return winner_token_number;
        }

        public void setWinner_token_number(String winner_token_number) {
            this.winner_token_number = winner_token_number;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPoster_url() {
            return poster_url;
        }

        public void setPoster_url(String poster_url) {
            this.poster_url = poster_url;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLucky_number() {
            return lucky_number;
        }

        public void setLucky_number(String lucky_number) {
            this.lucky_number = lucky_number;
        }

        public String getDraw_at() {
            return draw_at;
        }

        public void setDraw_at(String draw_at) {
            this.draw_at = draw_at;
        }

        public String[] getPictures() {
            return pictures;
        }

        public void setPictures(String[] pictures) {
            this.pictures = pictures;
        }

        public boolean isWinner_self() {
            return winner_self;
        }

        public void setWinner_self(boolean winner_self) {
            this.winner_self = winner_self;
        }

        public String getWinner_name() {
            return winner_name;
        }

        public void setWinner_name(String winner_name) {
            this.winner_name = winner_name;
        }

        public String getWinner_avatar_url() {
            return winner_avatar_url;
        }

        public void setWinner_avatar_url(String winner_avatar_url) {
            this.winner_avatar_url = winner_avatar_url;
        }

        public String getToken_number() {
            return token_number;
        }

        public void setToken_number(String token_number) {
            this.token_number = token_number;
        }
    }
}
