package com.robin8.rb.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 @author DLJ
 @date 2016/1/27 20:17 */
public class CampaignListBean extends BaseBean {
    private MsgStatusbean message_stat;
    private List<CampaignInviteEntity> campaign_invites;
    private List<AnnouncementEntity> announcements;
   // private String alert;

    public List<CampaignInviteEntity> getCampaign_invites() {
        return campaign_invites;
    }

    public MsgStatusbean getMessage_stat() {
        return message_stat;
    }

    public void setMessage_stat(MsgStatusbean message_stat) {
        this.message_stat = message_stat;
    }

    public void setCampaign_invites(List<CampaignInviteEntity> campaign_invite) {
        this.campaign_invites = campaign_invite;
    }

    public List<AnnouncementEntity> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<AnnouncementEntity> announcements) {
        this.announcements = announcements;
    }

//    public String getAlert() {
//        return alert;
//    }
//
//    public void setAlert(String alert) {
//        this.alert = alert;
//    }


    public static class MsgStatusbean {
        private String unread_message_count;
        private String new_income;

        public MsgStatusbean(String unread_message_count, String new_income) {
            this.unread_message_count = unread_message_count;
            this.new_income = new_income;
        }

        public MsgStatusbean(String unread_message_count) {
            this.unread_message_count = unread_message_count;
        }

        public String getNew_income() {
            return new_income;
        }

        public void setNew_income(String new_income) {
            this.new_income = new_income;
        }

        public String getUnread_message_count() {

            return unread_message_count;
        }

        public void setUnread_message_count(String unread_message_count) {
            this.unread_message_count = unread_message_count;
        }
    }

    public static class AnnouncementEntity extends BaseBean implements Serializable {
        private int id;
        private String title;
        private String desc;
        private String url;
        private String detail_type;
        private String banner_url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDetail_type() {
            return detail_type;
        }

        public void setDetail_type(String detail_type) {
            this.detail_type = detail_type;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(String banner_url) {
            this.banner_url = banner_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class CampaignInviteEntity extends BaseBean implements Serializable {
        private int id;
        private String status;
        private String img_status;
        private String sub_type;
        private String share_url;
        private boolean is_invited;
        private String avatar_url;
        private String kol_name;
        private String screenshot;
        private List<String> screenshots;
        private String reject_reason;
        private String approved_at;//转发时间
        private boolean can_upload_screenshot;//是否可以上传截图
        private int avail_click;
        private int total_click;
        private float earn_money;
        private String invite_status;
        private String tag;
        private List<Integer> upload_interval_time;
        private String uuid;
        private boolean start_upload_screenshot;//是否开始上传截图
        private String cpi_example_screenshot;
        private List<String> cpi_example_screenshots;
        private List<String> screenshot_comment;

        public List<String> getScreenshot_comment() {
            return screenshot_comment;
        }

        public void setScreenshot_comment(List<String> screenshot_comment) {
            this.screenshot_comment = screenshot_comment;
        }

        public String getKol_name() {
            return kol_name;
        }

        public void setKol_name(String kol_name) {
            this.kol_name = kol_name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }


        public boolean isStart_upload_screenshot() {
            return start_upload_screenshot;
        }

        public void setStart_upload_screenshot(boolean start_upload_screenshot) {
            this.start_upload_screenshot = start_upload_screenshot;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public List<Integer> getUpload_interval_time() {
            return upload_interval_time;
        }

        public void setUpload_interval_time(List<Integer> upload_interval_time) {
            this.upload_interval_time = upload_interval_time;
        }

        public boolean isCan_upload_screenshot() {
            return can_upload_screenshot;
        }

        public void setCan_upload_screenshot(boolean can_upload_screenshot) {
            this.can_upload_screenshot = can_upload_screenshot;
        }

        /**
         id : 315
         name : test_app
         description : test_app
         img_url :
         status : settled
         message : null
         url : http://test_app.com
         per_budget_type : click
         per_action_budget : 0.1
         budget : 2
         deadline : 2016-01-13T20:57:00+08:00
         start_time : 2016-01-11T15:57:00+08:00
         avail_click : 0
         total_click : 0
         take_budget : 0
         remain_budget : 2
         share_time : 5
         interval_time : [1,1,3]
         */

        private CampaignEntity campaign;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public float getEarn_money() {
            return earn_money;
        }

        public void setEarn_money(float earn_money) {
            this.earn_money = earn_money;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setImg_status(String img_status) {
            this.img_status = img_status;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public void setIs_invited(boolean is_invited) {
            this.is_invited = is_invited;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public void setReject_reason(String reject_reason) {
            this.reject_reason = reject_reason;
        }

        public void setApproved_at(String approved_at) {
            this.approved_at = approved_at;
        }

        public void setAvail_click(int avail_click) {
            this.avail_click = avail_click;
        }

        public void setTotal_click(int total_click) {
            this.total_click = total_click;
        }


        public void setCampaign(CampaignEntity campaign) {
            this.campaign = campaign;
        }

        public int getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getImg_status() {
            return img_status;
        }

        public String getShare_url() {
            return share_url;
        }

        public boolean isIs_invited() {
            return is_invited;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public String getReject_reason() {
            return reject_reason;
        }

        public String getApproved_at() {
            return approved_at;
        }

        public int getAvail_click() {
            return avail_click;
        }

        public int getTotal_click() {
            return total_click;
        }


        public CampaignEntity getCampaign() {
            return campaign;
        }

        public String getInvite_status() {
            return invite_status;
        }

        public void setInvite_status(String invite_status) {
            this.invite_status = invite_status;
        }

        public String getSub_type() {
            return sub_type;
        }

        public void setSub_type(String sub_type) {
            this.sub_type = sub_type;
        }

        public String getCpi_example_screenshot() {
            return cpi_example_screenshot;
        }

        public void setCpi_example_screenshot(String cpi_example_screenshot) {
            this.cpi_example_screenshot = cpi_example_screenshot;
        }

        public List<String> getCpi_example_screenshots() {
            return cpi_example_screenshots;
        }

        public void setCpi_example_screenshots(List<String> cpi_example_screenshots) {
            this.cpi_example_screenshots = cpi_example_screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }

        public void setScreenshots(List<String> screenshots) {
            this.screenshots = screenshots;
        }

        public static class CampaignEntity implements Serializable {
            private int id;
            private String name;
            private String kol_name;
            private String description;
            private String img_url;
            private String status;
            private String message;
            private String url;
            private String per_budget_type;
            private String per_action_type;
            private float per_action_budget;
            private float budget;// TODO BUG 改成 float
            private String deadline;
            private String start_time;
            private String recruit_end_time;
            private int avail_click;
            private int total_click;
            private int max_action;
            private float take_budget;
            private String brand_name;
            private float remain_budget;
            private int share_time;
            private List<Integer> interval_time;
            private List<String> cpi_example_screenshots;
            private String cpi_example_screenshot;
            private String remark;
            private boolean is_applying_note_required;
            private String applying_note_description;
            private boolean is_applying_picture_required;
            private String applying_picture_description;
            private String wechat_auth_type;


            public boolean is_applying_note_required() {
                return is_applying_note_required;
            }

            public void setIs_applying_note_required(boolean is_applying_note_required) {
                this.is_applying_note_required = is_applying_note_required;
            }

            public String getApplying_note_description() {
                return applying_note_description;
            }

            public void setApplying_note_description(String applying_note_description) {
                this.applying_note_description = applying_note_description;
            }

            public boolean is_applying_picture_required() {
                return is_applying_picture_required;
            }

            public void setIs_applying_picture_required(boolean is_applying_picture_required) {
                this.is_applying_picture_required = is_applying_picture_required;
            }

            public String getApplying_picture_description() {
                return applying_picture_description;
            }

            public void setApplying_picture_description(String applying_picture_description) {
                this.applying_picture_description = applying_picture_description;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public float getRemain_budget() {
                return remain_budget;
            }

            public void setRemain_budget(float remain_budget) {
                this.remain_budget = remain_budget;
            }

            public float getTake_budget() {
                return take_budget;
            }

            public void setTake_budget(float take_budget) {
                this.take_budget = take_budget;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setPer_budget_type(String per_budget_type) {
                this.per_budget_type = per_budget_type;
            }

            public void setPer_action_budget(float per_action_budget) {
                this.per_action_budget = per_action_budget;
            }

            public void setBudget(int budget) {
                this.budget = budget;
            }

            public void setDeadline(String deadline) {
                this.deadline = deadline;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setAvail_click(int avail_click) {
                this.avail_click = avail_click;
            }

            public void setTotal_click(int total_click) {
                this.total_click = total_click;
            }


            public void setShare_time(int share_time) {
                this.share_time = share_time;
            }

            public void setInterval_time(List<Integer> interval_time) {
                this.interval_time = interval_time;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getDescription() {
                return description;
            }

            public String getImg_url() {
                return img_url;
            }

            public String getStatus() {
                return status;
            }

            public String getMessage() {
                return message;
            }

            public String getUrl() {
                return url;
            }

            public String getPer_budget_type() {
                return per_budget_type;
            }

            public float getPer_action_budget() {
                return per_action_budget;
            }

            public float getBudget() {
                return budget;
            }

            public String getDeadline() {
                return deadline;
            }

            public String getStart_time() {
                return start_time;
            }

            public int getAvail_click() {
                return avail_click;
            }

            public int getTotal_click() {
                return total_click;
            }


            public int getShare_time() {
                return share_time;
            }

            public List<Integer> getInterval_time() {
                return interval_time;
            }

            public int getMax_action() {
                return max_action;
            }

            public void setMax_action(int max_action) {
                this.max_action = max_action;
            }

            public String getRecruit_end_time() {
                return recruit_end_time;
            }

            public void setRecruit_end_time(String recruit_end_time) {
                this.recruit_end_time = recruit_end_time;
            }

            public String getCpi_example_screenshot() {
                return cpi_example_screenshot;
            }

            public void setCpi_example_screenshot(String cpi_example_screenshot) {
                this.cpi_example_screenshot = cpi_example_screenshot;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPer_action_type() {
                return per_action_type;
            }

            public void setPer_action_type(String per_action_type) {
                this.per_action_type = per_action_type;
            }

            public String getWechat_auth_type() {
                return wechat_auth_type;
            }

            public void setWechat_auth_type(String wechat_auth_type) {
                this.wechat_auth_type = wechat_auth_type;
            }

            public List<String> getCpi_example_screenshots() {
                return cpi_example_screenshots;
            }

            public void setCpi_example_screenshots(List<String> cpi_example_screenshots) {
                this.cpi_example_screenshots = cpi_example_screenshots;
            }
        }
    }
}
