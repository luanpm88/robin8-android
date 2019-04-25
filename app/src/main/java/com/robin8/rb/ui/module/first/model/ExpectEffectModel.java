package com.robin8.rb.ui.module.first.model;

import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/10/12.
 */
public class ExpectEffectModel extends BaseBean {

    /**
     * name : incr_exposure
     * label : 增加文章曝光
     * budget_type : click
     */

    private List<ExpectEffectListBean> expect_effect_list;

    public List<ExpectEffectListBean> getExpect_effect_list() {
        return expect_effect_list;
    }

    public void setExpect_effect_list(List<ExpectEffectListBean> expect_effect_list) {
        this.expect_effect_list = expect_effect_list;
    }

    public static class ExpectEffectListBean {
        private String name;
        private String label;
        private String budget_type;
        private boolean isSelected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getBudget_type() {
            return budget_type;
        }

        public void setBudget_type(String budget_type) {
            this.budget_type = budget_type;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
