package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class ParticipateCrewListModel extends BaseBean {

    /**
     * kol_id : 48587
     * avatar_url : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/48587/c68118b96d.jpg
     * kol_name : 孙月清
     * avail_click : 0
     * total_click : 1
     */

    private List<CampaignInvitesBean> campaign_invites;

    public List<CampaignInvitesBean> getCampaign_invites() {
        return campaign_invites;
    }

    public void setCampaign_invites(List<CampaignInvitesBean> campaign_invites) {
        this.campaign_invites = campaign_invites;
    }

    public static class CampaignInvitesBean {
        private int kol_id;
        private String avatar_url;
        private String kol_name;
        private int avail_click;
        private int total_click;

        public int getKol_id() {
            return kol_id;
        }

        public void setKol_id(int kol_id) {
            this.kol_id = kol_id;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getKol_name() {
            return kol_name;
        }

        public void setKol_name(String kol_name) {
            this.kol_name = kol_name;
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
    }
}
