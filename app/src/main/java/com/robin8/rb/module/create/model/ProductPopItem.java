package com.robin8.rb.module.create.model;

/**
 * Created by IBM on 2016/10/19.
 */
public class ProductPopItem {
    public ProductPopItem(String name,String label,boolean isSelected){
        this.name = name;
        this.label = label;
        this.isSelected = isSelected;
    }
    public String name;
    public String label;
    public boolean isSelected;
}
