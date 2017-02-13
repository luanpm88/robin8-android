package com.robin8.rb.ui.widget.citylist;

import android.content.Context;
import android.util.AttributeSet;

import com.robin8.rb.R;

public class ContactListViewImpl extends ContactListView
{

	public ContactListViewImpl(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void createScroller()
	{

		mScroller = new IndexScroller(getContext(), this);

		mScroller.setAutoHide(autoHide);

		// style 1
		// mScroller.setShowIndexContainer(false);
		// mScroller.setIndexPaintColor(Color.argb(255, 49, 64, 91));
		// style 2
		mScroller.setShowIndexContainer(true);
		mScroller.setIndexPaintColor(R.color.sub_gray_custom);

		if (autoHide)
			mScroller.hide();
		else
			mScroller.show();

	}
}
