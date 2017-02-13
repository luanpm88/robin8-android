package com.robin8.rb.module.create.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/10/19.
 */
public class CategoriesModel extends BaseBean {

    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
