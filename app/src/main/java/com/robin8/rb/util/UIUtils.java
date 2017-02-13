package com.robin8.rb.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.base.BaseApplication;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UIUtils {

	// 字体大小
	public static int SMALLEST = 15;
	public static int NORMAL = 17;
	public static int LARGER = 19;
	public static int LARGEST = 21;
	public static int TEXTSIZE = NORMAL;
	private static int flag = 1;
	private final static String TIME_FORMAT_hhmm = "hh:mm";
	private static StringBuffer sb;

	public static float mPixelDensityF;

	// dip--->px 1dp = 1px 1dp = 2px
	public static int dip2px(int dip) {
		// dp和px的转换关系比例
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5);
	}

	// px---->dp
	public static int px2dip(int px) {
		// dp和px的转换关系比例
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5);
	}

	// 判断是否是主线的方法
	public static boolean isRunInMainThread() {
		return BaseApplication.getMainThreadId() == android.os.Process.myTid();
	}

	// 保证当前的UI操作在主线程里面运行
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			// 如果现在就是在珠现场中，就直接运行run方法
			runnable.run();
		} else {
			// 否则将其传到主线程中运行
			BaseApplication.getHandler().post(runnable);
		}
	}

	/**
	 * edittext设置光标到末尾
	 * 
	 * @param editText
	 */
	public static void setEditTextCursorLocation(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	// java代码区设置颜色择器的方法
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getContext().getResources().getColorStateList(mTabTextColorResId);
	}

	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	public static int getDimens(int id) {
		// 根据dimens中提供的id，将其对应的dp值转换成相应的像素大小
		return UIUtils.getContext().getResources().getDimensionPixelSize(id);
	}

	public static void postDelayed(Runnable runnable, long delayTime) {
		BaseApplication.getHandler().postDelayed(runnable, delayTime);
	}

	public static void removeCallBack(Runnable runnableTask) {
		// 移除传递进来任务
		BaseApplication.getHandler().removeCallbacks(runnableTask);
	}

	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}

	public static ColorStateList getColorselector(int id) {
		return getContext().getResources().getColorStateList(id);
	}

	/**
	 * 根据日期 yyyy-MM-dd HH:mm 获取毫秒值
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getMillionSeconds(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long time = sdf.parse(date).getTime();
		return time;
	}

	/**
	 * 叠加式信息流刷新点击提示
	 * 
	 * @param millionSeconds
	 * @return
	 */
	public static String transfer1(long millionSeconds) {
		String str2 = null;
		long currentTimeMillis = System.currentTimeMillis();
		int time = (int) ((currentTimeMillis - millionSeconds) / 60000);
		if (time > 0) {
			if (time <= 60) {
				str2 = time + "分钟前";
			} else if (time < 24 * 60 && time >= 60) {
				str2 = time / (60) + "小时前";
			} else {
				Date d = new Date(millionSeconds);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				str2 = sdf.format(d);
			}
		} else {
			str2 = "1分钟前";
		}
		return str2;
	}

	public static void setBackgroundColor(View view, int id) {
		if (view != null) {
			view.setBackgroundColor(getContext().getResources().getColor(id));
		}
	}

	public static void setTextcolor(TextView mtv, int id) {
		mtv.setTextColor(getContext().getResources().getColor(id));
	}

	/**
	 * 根据format格式获取时间
	 * 
	 * @param format
	 *            (如hh：mm)
	 * @return
	 */
	public static String getTime(long now, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		return formatter.format(calendar.getTime());
	}

	@TargetApi(19)
	public static void setTranslucentStatus(Activity activity, boolean on) {

		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 取系统的字体大小缩放比例
	 * 
	 * @return
	 */
	public static float getFontScale() {
		Configuration mCurConfig = new Configuration();
		try {
			Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
			Method method = am.getClass().getMethod("getConfiguration", new Class[0]);
			Object ob = method.invoke(am, new Object[0]);
			mCurConfig.updateFrom((Configuration) ob);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mCurConfig.fontScale;
	}


	public static Context getContext() {
		return BaseApplication.getContext();
	}
}
