package com.robin8.rb.ui.widget.citylist;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.util.UIUtils;

import java.util.List;


public class CityAdapter extends ContactListAdapter {

    public CityAdapter(Context _context, int _resource, List<ContactItemInterface> _items) {
        super(_context, _resource, _items);
    }

    public void populateDataForRow(View parentView, ContactItemInterface item, int position) {
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView nicknameView = (TextView) infoView .findViewById(R.id.cityName);
        TextView tvSelect = (TextView) infoView .findViewById(R.id.tv_select);

        String displayInfo = item.getDisplayInfo();
        if(!TextUtils.isEmpty(displayInfo) && displayInfo.startsWith("#")){
            displayInfo = displayInfo.substring(1);
        }

        if(item.isSelected()){
            tvSelect.setTextColor(UIUtils.getColor(R.color.green_custom));
            IconFontHelper.setTextIconFont(tvSelect,R.string.selected);
        }else {
            tvSelect.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
            IconFontHelper.setTextIconFont(tvSelect,R.string.unselected);
        }
        nicknameView.setText(displayInfo);
    }
}
