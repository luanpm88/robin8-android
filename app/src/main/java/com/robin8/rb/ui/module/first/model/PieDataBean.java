package com.robin8.rb.ui.module.first.model;

public class PieDataBean {
    private String label;
    private float probability;

    public PieDataBean(String label, float probability) {
        this.label = label;
        this.probability = probability;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}