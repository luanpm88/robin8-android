package com.robin8.rb.helper;

import android.graphics.Typeface;
import android.widget.TextView;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.SPConstants;

/**
 * Created by IBM on 2016/8/12.
 */
public class IconFontHelper {

    public static Typeface iconFont = Typeface.createFromAsset(BaseApplication.getContext().getAssets(), SPConstants.ICON_FONT_TTF);

    public static void setTextIconFont(TextView view, String icons) {
        view.setTypeface(iconFont);
        view.setText(icons);
    }

    public static void setTextIconFont(TextView view, int id) {
        view.setTypeface(iconFont);
        view.setText(BaseApplication.getContext().getString(id));
    }
}
