package com.robin8.rb.module.social.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.robin8.rb.R;
import com.robin8.rb.module.social.bean.SocialDetailItem;
import com.robin8.rb.module.social.view.CardHandler;

/**
 * description
 * <p>
 * Created by sunjian on 2017/6/24.
 */

public class MyCardHandler implements CardHandler<SocialDetailItem> {

    private RelativeLayout layoutShowOne;
    private RelativeLayout layoutShowTwo;
    private RelativeLayout layoutShowThree;

    @Override
    public View onBind(final Context context, final SocialDetailItem data, final int position) {
        View view = View.inflate(context, R.layout.social_detail_card_item, null);
//        TextView tvCardName = (TextView) view.findViewById(R.id.tv_card_type);
//        ImageView imgShowOne = (ImageView) view.findViewById(R.id.img_show_one);
//        ImageView imgShowTwo = (ImageView) view.findViewById(R.id.img_show_two);
//        ImageView imgShowThree = (ImageView) view.findViewById(R.id.img_show_three);
//        layoutShowOne = ((RelativeLayout) view.findViewById(R.id.show_layout_one));
//        layoutShowTwo = ((RelativeLayout) view.findViewById(R.id.show_layout_one2));
//        layoutShowThree = ((RelativeLayout) view.findViewById(R.id.show_layout_one3));
//        tvCardName.setText(data.getCardName());

//            imgShowOne.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    if (layoutShowOne.getVisibility() == View.VISIBLE) {
//                        layoutShowOne.setVisibility(View.GONE);
//                    } else {
//                        layoutShowOne.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            imgShowTwo.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    if (layoutShowTwo.getVisibility() == View.VISIBLE) {
//                        layoutShowTwo.setVisibility(View.GONE);
//                    } else {
//                        layoutShowTwo.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            imgShowThree.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    if (layoutShowThree.getVisibility() == View.VISIBLE) {
//                        layoutShowThree.setVisibility(View.GONE);
//                    } else {
//                        layoutShowThree.setVisibility(View.VISIBLE);
//                    }
//                }
//            });

        //  Glide.with(context).load(data.getUrl()).into(imageView);
        //textView.setText(data.getTitle());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "data:" + data + "position:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.img_show_one:
//                if (layoutShowOne.getVisibility()==View.VISIBLE){
//                    layoutShowOne.setVisibility(View.GONE);
//                }else {
//                    layoutShowOne.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.img_show_two:
//                if (layoutShowTwo.getVisibility()==View.VISIBLE){
//                    layoutShowTwo.setVisibility(View.GONE);
//                }else {
//                    layoutShowTwo.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.img_show_three:
//                if (layoutShowThree.getVisibility()==View.VISIBLE){
//                    layoutShowThree.setVisibility(View.GONE);
//                }else {
//                    layoutShowThree.setVisibility(View.VISIBLE);
//                }
//                break;
//        }
//    }
}
