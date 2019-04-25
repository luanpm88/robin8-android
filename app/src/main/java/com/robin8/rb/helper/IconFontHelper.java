package com.robin8.rb.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.SPConstants;

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
    public static void setTextIconFont(Context context,TextView view, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }
}
