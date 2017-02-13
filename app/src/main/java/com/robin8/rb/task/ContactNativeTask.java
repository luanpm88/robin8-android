package com.robin8.rb.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.ContactBean;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomRegExpUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 获取手机联系人
 */
public class ContactNativeTask extends AsyncTask<String, String, List<ContactBean>> {
    private WProgressDialog mWProgressDialog;
    //    private final CustomProgress mCustomProgress;
    private Context mContext;
    private IGetContactSuccess listener;
    private boolean showProgress;
    private Cursor cursor;

    public ContactNativeTask(Context mContext) {
        this(mContext, null);

    }

    public ContactNativeTask(Context mContext, IGetContactSuccess listener) {
        this(mContext, listener, false);

    }

    public ContactNativeTask(Context mContext, IGetContactSuccess listener, boolean showProgress) {
        this.mContext = mContext;
        this.listener = listener;
        this.showProgress = showProgress;
        this.cursor = mContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "has_phone_number", "contact_id"},
                "has_phone_number = 1", null, "contact_id desc limit 500");//!!!千万不能用order by
        // sort_key 会有莫名其妙的错误

        if (mWProgressDialog == null) {
            mWProgressDialog = new WProgressDialog(mContext);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgress && mWProgressDialog != null) {
            mWProgressDialog.setMessage("正在读取联系人...");
            mWProgressDialog.show();
        }
    }

    @Override
    protected List<ContactBean> doInBackground(String... params) {
        if (cursor == null)
            return new ArrayList<ContactBean>();
        ArrayList<ContactBean> arrayList = new ArrayList<ContactBean>();
        if (cursor.moveToFirst()) {
            do {
                int has_phone_number = cursor.getInt(2);
                if (has_phone_number >= 1) {
                    ContactBean contact = new ContactBean();
                    String name = cursor.getString(0);
                    String sortKey = getSortKey(cursor.getString(1));//排序用 这里不需要
                    contact.setName(name);
                    contact.setSortKey(sortKey);
                    // 查询号码
                    int contactId = cursor.getInt(3);
                    Cursor phones = mContext.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null, null);
                    if (phones.moveToFirst()) {
                        do {
                            String phoneNumber = phones.getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber = phoneNumber.replace("+", "");
                            phoneNumber = phoneNumber.replace(" ", "");
                            phoneNumber = phoneNumber.replace("\\", "");
                            if (phoneNumber.startsWith("86")) {
                                phoneNumber = phoneNumber.substring(2);
                            }
                            if (!TextUtils.isEmpty(contact.getPhone())) {
                                if (CustomRegExpUtil.checkPhone(phoneNumber))
                                    contact.setPhone(contact.getPhone() + ";" + phoneNumber);
                            } else {
                                if (CustomRegExpUtil.checkPhone(phoneNumber))
                                    contact.setPhone(phoneNumber);
                            }
                        } while (phones.moveToNext());

                    }
                    if (!TextUtils.isEmpty(contact.getPhone()))
                        arrayList.add(contact);
                    phones.close();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        // mContext.startManagingCursor(cursor);
        return arrayList;
    }

    @Override
    protected void onPostExecute(List<ContactBean> beans) {
        super.onPostExecute(beans);
        if (beans == null)
            return;
//        if (mWProgressDialog != null) {
//            mWProgressDialog.dismiss();
//        }
        formatContactBean(beans);
        BaseApplication.getInstance().setContactBeans(beans);
        if (listener != null)
            listener.onGetContactSuccess(beans);
    }

    /**
     * 对集合进行拆分 当一个联系人有多个电话是 拆分成多个联系人
     *
     * @param contacts2
     */
    private void formatContactBean(List<ContactBean> contacts2) {
        ArrayList<ContactBean> tempList = new ArrayList<ContactBean>();
        for (Iterator<ContactBean> iterator = contacts2.iterator(); iterator.hasNext(); ) {
            ContactBean bean = iterator.next();
            if (!TextUtils.isEmpty(bean.getPhone()) && bean.getPhone().contains(";")) {
                iterator.remove();
                String[] split = bean.getPhone().split(";");
                for (int i = 0; i < split.length; i++) {
                    ContactBean contact_Bean = new ContactBean();
                    contact_Bean.setName(bean.getName());
                    contact_Bean.setPhone(split[i]);
                    contact_Bean.setSortKey(bean.getSortKey());
                    tempList.add(contact_Bean);
                }
            }
        }
        contacts2.addAll(tempList);
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private String getSortKey(String sortKeyString) {
        if (TextUtils.isEmpty(sortKeyString))
            return "#";
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    public interface IGetContactSuccess {
        void onGetContactSuccess(List<ContactBean> beans);
    }
}
