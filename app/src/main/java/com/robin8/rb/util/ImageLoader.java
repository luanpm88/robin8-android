package com.robin8.rb.util;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.widget.ImageView;


/**
 * @description 
 * @author figo
 * @date 2016-6-24
 */
public class ImageLoader {
	
     public static void with(Context context,ImageView imageView,String url){
    	 Glide.with(context)
    	      .load(url)
    	      .into(imageView);
     }
     
     public static void with(Context context,ImageView imageView,String url,int resourceId){
    	 Glide.with(context)
    	      .load(url)
    	      .placeholder(resourceId)
    	      .error(resourceId)
    	      .into(imageView);
     }
}
