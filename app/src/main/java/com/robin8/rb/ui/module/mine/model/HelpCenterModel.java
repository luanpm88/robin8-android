package com.robin8.rb.ui.module.mine.model;

import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class HelpCenterModel extends BaseBean {

    /**
     * question : 活动类型
     * answer : 点击、转发、效果类活动即KOL分享文章到朋友圈:按照好友有效点击次数付费
     按照KOL转发一次性付费
     按照KOL分享后效果付费;
     参与类活动:广告主创建活动,确定价格，KOL进行报名,广告主同意报名后即可接单的一次性付费活动;
     特邀活动:广告主创建活动,邀请指定的KOL接单,KOL定价,广告主接受KOL定价后KOL即可接单的一次性付费活动!
     */

    private List<NoticesBean> notices;

    public List<NoticesBean> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticesBean> notices) {
        this.notices = notices;
    }

    public static class NoticesBean {
        private String question;
        private String answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
