package com.robin8.rb.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.module.find.SetResultCallBack;
import com.robin8.rb.ui.module.find.adapter.NineGridViewClickAdapter;
import com.robin8.rb.ui.module.find.model.FindArticleListModel;
import com.robin8.rb.ui.module.find.model.ImageInfo;
import com.robin8.rb.ui.module.find.view.ExpandableTextView;
import com.robin8.rb.ui.module.find.view.NineGridView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 Created by zc on 2018/3/30. */

public class MainFindListAdapter extends BaseRecyclerAdapter {
    private List<FindArticleListModel.ListBean> mDataList;
    private Context mContext;
    private ViewHolder mViewHolder;
    private RecyclerListener recyclerListener;
    private boolean isCollect;
    private boolean isLike;
    private CustomDialogManager mCustomDialogManager;
    private static final String IMAGE_URL = CommonConfig.APP_ICON;

    public interface RecyclerListener {

        void OnSimpleClick(View v, int position, SetResultCallBack setResultCallBack);

        void OnItemClick(View v, int position);
    }

    public void setClickListener(RecyclerListener listener) {
        this.recyclerListener = listener;
    }

    public MainFindListAdapter(Context context, List<FindArticleListModel.ListBean> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    public void setCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getLike() {
        return isLike;
    }

    public boolean getCollect() {
        return isCollect;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_find_item, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, boolean isItem) {
        if (mDataList == null || mDataList.size() == 0 || position < 0 || position >= mDataList.size()) {
            return;
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        final FindArticleListModel.ListBean listModel = mDataList.get(position);
        if (listModel == null) {
            return;
        }
        if (listModel.getAvatar_url() != null) {
            BitmapUtil.loadImage(mContext.getApplicationContext(), listModel.getAvatar_url(), viewHolder.imgUserPhoto);
        }

        if (listModel.getTitle() != null) {
            //15298670933
            //viewHolder.llContent.setText(listModel.getTitle());
            viewHolder.llExpand.setTextColor(mContext.getResources().getColor(R.color.black_custom));
            viewHolder.llExpand.setTextSize(16);
            viewHolder.llExpand.setTextLineSpacingExtra(3);
            viewHolder.llExpand.setText(listModel.getTitle());
            viewHolder.llExpand.post(new Runnable() {

                @Override
                public void run() {
                    if (viewHolder.llExpand.getmExpandText().getLineCount() <= 3) {
                        viewHolder.tvShow.setVisibility(View.GONE);
                    } else {
                        viewHolder.tvShow.setVisibility(View.VISIBLE);
                    }
                }
            });

            if (viewHolder.llExpand.isShow() == false) {
                viewHolder.tvShow.setText(mContext.getString(R.string.text_show));
            } else {
                viewHolder.tvShow.setText(mContext.getString(R.string.text_pack_up));
            }
            viewHolder.tvShow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    viewHolder.llExpand.switchs();
                    if (viewHolder.llExpand.isShow() == false) {
                        viewHolder.tvShow.setText(mContext.getString(R.string.text_show));
                    } else {
                        viewHolder.tvShow.setText(mContext.getString(R.string.text_pack_up));
                    }
                }
            });

        }
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i < listModel.getPics().get(0).size(); i++) {
            ImageInfo info = new ImageInfo();
            info.setBigImageUrl(listModel.getPics().get(1).get(i));
            info.setThumbnailUrl(listModel.getPics().get(0).get(i));
            imageInfo.add(info);
        }
        viewHolder.gridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
        viewHolder.tvUserName.setText(listModel.getUser_name());
        viewHolder.position = position;
        int likes_count = listModel.getLikes_count();
        if (String.valueOf(likes_count) == null) {
            likes_count = 0;
        }
        viewHolder.tvLikeNum.setText(String.valueOf(likes_count));
        viewHolder.tvShareNum.setText(String.valueOf(listModel.getForwards_count()));
        if (listModel.getReads_count() >= 10000) {
            viewHolder.tvLookNum.setText(String.valueOf(listModel.getReads_count() / 10000) + "万次观看");
        } else {
            viewHolder.tvLookNum.setText(String.valueOf(listModel.getReads_count()) + "次观看");
        }
        viewHolder.tvTime.setText(DateUtil.getCountdownMore("yyyy-MM-dd HH:mm:ss", listModel.getPost_date()));
        // isCollect = listModel.isIs_collected();
        // isLike = listModel.isIs_liked();
        if (listModel.isIs_collected() == true) {
            viewHolder.tvCollect.setBackgroundResource(R.drawable.shape_bg_gray_pane_pane);
            viewHolder.tvCollect.setText(mContext.getString(R.string.text_collected));
            viewHolder.tvCollect.setTextColor(mContext.getResources().getColor(R.color.gray_first));
        } else {
            viewHolder.tvCollect.setBackgroundResource(R.drawable.shape_bg_yellow_pane_first);
            viewHolder.tvCollect.setText(mContext.getString(R.string.text_collect));
            viewHolder.tvCollect.setTextColor(mContext.getResources().getColor(R.color.yellow_custom));
        }
        if (listModel.isIs_liked() == true) {
            viewHolder.imgClick.setImageResource(R.mipmap.icon_like_yes);
        } else {
            viewHolder.imgClick.setImageResource(R.mipmap.icon_like_no);
        }
        viewHolder.tvCollect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recyclerListener != null) {
                    recyclerListener.OnSimpleClick(viewHolder.tvCollect, position, new SetResultCallBack() {

                        @Override
                        public void onLike(boolean isLike) {

                        }

                        @Override
                        public void onCollect(boolean isCollect) {
                            if (isCollect) {
                                listModel.setIs_collected(true);
                                viewHolder.tvCollect.setBackgroundResource(R.drawable.shape_bg_gray_pane_pane);
                                viewHolder.tvCollect.setText(mContext.getString(R.string.text_collected));
                                viewHolder.tvCollect.setTextColor(mContext.getResources().getColor(R.color.gray_first));
                            } else {
                                listModel.setIs_collected(false);
                                viewHolder.tvCollect.setBackgroundResource(R.drawable.shape_bg_yellow_pane_first);
                                viewHolder.tvCollect.setText(mContext.getString(R.string.text_collect));
                                viewHolder.tvCollect.setTextColor(mContext.getResources().getColor(R.color.yellow_custom));
                            }
                        }

                        @Override
                        public void onShare(boolean isShare) {

                        }

                    });
                }

            }
        });
        final int finalLikes_count = likes_count;
        viewHolder.llLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recyclerListener != null) {
                    recyclerListener.OnSimpleClick(viewHolder.llLike, position, new SetResultCallBack() {

                        @Override
                        public void onLike(boolean isLike) {
                            if (isLike == true) {
                                listModel.setIs_liked(true);
                                viewHolder.imgClick.setImageResource(R.mipmap.icon_like_yes);

                                viewHolder.tvLikeNum.setText(String.valueOf((Integer.valueOf(finalLikes_count) + 1)));
                                listModel.setLikes_count(finalLikes_count + 1);
                            } else {
                                listModel.setIs_liked(false);
                                viewHolder.imgClick.setImageResource(R.mipmap.icon_like_no);
                                if (Integer.valueOf(viewHolder.tvLikeNum.getText().toString()) - 1 < 0) {
                                    viewHolder.tvLikeNum.setText(0);
                                    listModel.setLikes_count(0);
                                } else {
                                    viewHolder.tvLikeNum.setText(String.valueOf((Integer.valueOf(viewHolder.tvLikeNum.getText().toString()) - 1)));
                                    listModel.setLikes_count((Integer.valueOf(viewHolder.tvLikeNum.getText().toString()) - 1));
                                }

                            }
                        }

                        @Override
                        public void onCollect(boolean isCollect) {

                        }

                        @Override
                        public void onShare(boolean isShare) {

                        }
                    });
                }
            }
        });
        viewHolder.llShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showInviteDialog(listModel, viewHolder, position);
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llLike;
        public CircleImageView imgUserPhoto;
        public TextView tvUserName;
        public TextView tvTime;
        public TextView tvCollect;
        public TextView tvShow;
        public ExpandableTextView llExpand;
        public LinearLayout llContent;
        public NineGridView gridView;
        public TextView tvLookNum;
        public ImageView imgClick;
        public TextView tvLikeNum;
        public LinearLayout llShare;
        public TextView tvShareNum;
        public int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgUserPhoto = ((CircleImageView) itemView.findViewById(R.id.img_user_photo));
            tvUserName = ((TextView) itemView.findViewById(R.id.tv_user_name));
            tvTime = ((TextView) itemView.findViewById(R.id.tv_article_time));
            tvCollect = ((TextView) itemView.findViewById(R.id.tv_collect));
            llExpand = ((ExpandableTextView) itemView.findViewById(R.id.ll_expand));
            tvShow = ((TextView) itemView.findViewById(R.id.tv_show));
            llContent = ((LinearLayout) itemView.findViewById(R.id.ll_content));
            gridView = ((NineGridView) itemView.findViewById(R.id.img_grid_view));
            tvLookNum = ((TextView) itemView.findViewById(R.id.tv_look_num));
            llLike = ((LinearLayout) itemView.findViewById(R.id.ll_like));
            imgClick = ((ImageView) itemView.findViewById(R.id.img_like_click));
            tvLikeNum = ((TextView) itemView.findViewById(R.id.tv_like_num));
            llShare = ((LinearLayout) itemView.findViewById(R.id.ll_share));
            tvShareNum = ((TextView) itemView.findViewById(R.id.tv_like_share_num));
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (recyclerListener != null) {
                        recyclerListener.OnItemClick(itemView, position);
                    }
                }
            });
        }
    }

    private void showInviteDialog(FindArticleListModel.ListBean listModel, ViewHolder viewHolder, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.invite_friends_dialog, null);
        TextView weixinTV = (TextView) view.findViewById(R.id.tv_weixin);
        TextView wechatmomentsTV = (TextView) view.findViewById(R.id.tv_wechatmoments);
        TextView weiboTV = (TextView) view.findViewById(R.id.tv_weibo);
        TextView qqTV = (TextView) view.findViewById(R.id.tv_qq);
        TextView qonzeTV = (TextView) view.findViewById(R.id.tv_qonze);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        mCustomDialogManager = new CustomDialogManager(mContext, view);
        viewClick(weixinTV, listModel, viewHolder, position);
        viewClick(wechatmomentsTV, listModel, viewHolder, position);
        viewClick(weiboTV, listModel, viewHolder, position);
        viewClick(qqTV, listModel, viewHolder, position);
        viewClick(qonzeTV, listModel, viewHolder, position);
        //        weixinTV.setOnClickListener(this);
        //        wechatmomentsTV.setOnClickListener(this);
        //        weiboTV.setOnClickListener(this);
        //        qqTV.setOnClickListener(this);
        //        qonzeTV.setOnClickListener(this);
        cancelTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private void viewClick(TextView view, final FindArticleListModel.ListBean listModel, final ViewHolder holder, final int position) {
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                if (recyclerListener != null) {
                    recyclerListener.OnSimpleClick(view, position, new SetResultCallBack() {

                        @Override
                        public void onLike(boolean isLike) {

                        }

                        @Override
                        public void onCollect(boolean isCollect) {

                        }

                        @Override
                        public void onShare(boolean isShare) {
                            switch (view.getId()) {
                                case R.id.tv_weixin:
                                    share(Wechat.NAME, listModel, HelpTools.getUrl(listModel.getForward_url()));
                                    break;
                                case R.id.tv_wechatmoments:
                                    share(WechatMoments.NAME, listModel, HelpTools.getUrl(listModel.getForward_url()));
                                    break;
                                case R.id.tv_weibo:
                                    share(SinaWeibo.NAME, listModel, HelpTools.getUrl(listModel.getForward_url()));
                                    break;
                                case R.id.tv_qq:
                                    share(QQ.NAME, listModel, HelpTools.getUrl(listModel.getForward_url()));
                                    break;
                                case R.id.tv_qonze:
                                    share(QZone.NAME, listModel, HelpTools.getUrl(listModel.getForward_url()));
                                    break;
                            }
                            if (isShare) {
                                holder.tvShareNum.setText(String.valueOf(listModel.getForwards_count() + 1));
                                listModel.setForwards_count(listModel.getForwards_count() + 1);
                            }
                        }

                    });
                }
            }
        });
    }

    private String title;
    private String titleAdd;
    private boolean isFirst = false;

    private void share(String platName, FindArticleListModel.ListBean listModel, String url) {
        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }
        if (listModel != null) {
            title = "#robin8#" + listModel.getTitle();
        } else {
            title = "#robin8#";
        }
        titleAdd = "\n------  Robin8 个人影响力管理平台  ------";
        CustomToast.showShort(mContext, "正在前往分享...");
        //ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(title + url + titleAdd);
        } else {
            oks.setText(title);
        }
        oks.setTitle(title);
        oks.setTitleUrl(url);
        oks.setImageUrl(IMAGE_URL);
        if (Wechat.NAME.equals(platName) || WechatMoments.NAME.equals(platName)) {
            oks.setUrl(url);
        }
        oks.setSite(mContext.getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(mContext);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(mContext, "分享成功");

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(mContext, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(mContext, "分享取消");
        }
    }

}
