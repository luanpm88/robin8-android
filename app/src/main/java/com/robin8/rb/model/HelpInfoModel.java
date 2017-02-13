package com.robin8.rb.model;

import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/25 11:29
 */
public class HelpInfoModel extends BaseBean {

    private List<QuestionEntity> notices;

    public List<QuestionEntity> getNotices() {
        return notices;
    }

    public void setNotices(List<QuestionEntity> notices) {
        this.notices = notices;
    }

    public static class QuestionEntity {
        private String question;
        private String answer;
        private String logo;

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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

}
