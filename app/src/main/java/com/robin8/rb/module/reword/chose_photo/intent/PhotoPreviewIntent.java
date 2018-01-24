package com.robin8.rb.module.reword.chose_photo.intent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.robin8.rb.module.reword.chose_photo.PhotoPreviewActivity;
import com.robin8.rb.module.reword.chose_photo.SerializableMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * 预览照片
 */
public class PhotoPreviewIntent extends Intent {

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
    }

    /**
     * 照片地址
     * @param paths
     */
    public void setPhotoPaths(ArrayList<String> paths){
        this.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, paths);
    }
    /**
     * 照片地址Map类型
     * 为中途缺少图片时预览的状况坐准备
     * @param paths
     */
    public void setPhotoMapPaths(Map<Integer,String> paths){
        SerializableMap map = new SerializableMap();
        map.setMap(paths);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PhotoPreviewActivity.EXTRA_PHOTOS_MAP,map);
        this.putExtras(bundle);
    }
    /**
     * 非满照照片地址
     * @param path
     */
    public void setPhotoPath(String path){
        this.putExtra(PhotoPreviewActivity.EXTRA_PHOTO, path);
    }
  /**
     * 图片title
     * @param title
     */
    public void setPhotoTitle(ArrayList<String> title){
        this.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTO_TITLE, title);
    }

    /**
     * 当前照片的下标
     * @param currentItem
     */
    public void setCurrentItem(int currentItem){
        this.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
    }
}
