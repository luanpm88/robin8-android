package com.robin8.rb.ui.module.reword.chose_photo;

/**
 * 照片选择类型
 *
 */
public enum SelectModel {
    SINGLE(PhotoPickerActivity.MODE_SINGLE),
    MULTI(PhotoPickerActivity.MODE_MULTI);

    private int model;

    SelectModel(int model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return String.valueOf(this.model);
    }
}
