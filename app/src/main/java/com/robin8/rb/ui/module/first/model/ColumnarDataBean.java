package com.robin8.rb.ui.module.first.model;

public class ColumnarDataBean {
    private String label;
    private int freq;

    public ColumnarDataBean(String label, int freq) {
        this.label = label;
        this.freq = freq;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}