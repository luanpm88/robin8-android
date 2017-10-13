package com.robin8.rb.module.reword.bean;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2017/10/10. */

public class PersonListBean extends BaseBean{

    private List<InviteesBean> invitees;

    public List<InviteesBean> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<InviteesBean> invitees) {
        this.invitees = invitees;
    }

    public static class InviteesBean  implements Serializable {
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
