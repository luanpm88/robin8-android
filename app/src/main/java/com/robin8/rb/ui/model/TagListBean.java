package com.robin8.rb.ui.model;

import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/28 20:58
 */
public class TagListBean extends BaseBean {

    /**
     * name : career
     * label : 找工作
     */
    private List<TagsEntity> tags;

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public static class TagsEntity {
        private String name;
        private String label;
        private String cover_url;

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }
    }
}
