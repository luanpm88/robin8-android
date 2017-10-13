package com.robin8.rb.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/2/17 17:27
 */
public class CampaignInviteBean extends BaseBean {

    private CampaignListBean.CampaignInviteEntity campaign_invite;
    private List<InviteesBean> invitees;
    private int invitees_count;
    private String leader_club;
    public CampaignListBean.CampaignInviteEntity getCampaign_invite() {
        return campaign_invite;
    }

    public void setCampaign_invite(CampaignListBean.CampaignInviteEntity campaign_invite) {
        this.campaign_invite = campaign_invite;
    }

    public List<InviteesBean> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<InviteesBean> invitees) {
        this.invitees = invitees;
    }

    public int getInvitees_count() {
        return invitees_count;
    }

    public void setInvitees_count(int invitees_count) {
        this.invitees_count = invitees_count;
    }

    public String getLeader_club() {
        return leader_club;
    }

    public void setLeader_club(String leader_club) {
        this.leader_club = leader_club;
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
