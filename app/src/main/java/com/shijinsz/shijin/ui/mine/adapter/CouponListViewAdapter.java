package com.shijinsz.shijin.ui.mine.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.AdAllianceBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Copyright (C)
 * FileName: CouponListViewAdapter
 * Author: m1342
 * Date: 2019/7/10 15:27
 * Description: 优惠劵显示适配器
 */
public class CouponListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<AdAllianceBean> mList;
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public CouponListViewAdapter(Context context, List<AdAllianceBean> datas, int itemLayoutId){
        mContext = context;
        mList = datas;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.imageView = view.findViewById(R.id.item_img);
            viewHolder.title = view.findViewById(R.id.item_title);
            viewHolder.hint = view.findViewById(R.id.item_hint);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.old_price = view.findViewById(R.id.old_price);
            viewHolder.coupon = view.findViewById(R.id.get_coupon);
            viewHolder.buy = view.findViewById(R.id.buy_commodity);
            viewHolder.discounts_money = view.findViewById(R.id.discounts_money);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext).load(mList.get(i).getImg_url()).into(viewHolder.imageView);
        viewHolder.title.setText(mList.get(i).getName());
        viewHolder.hint.setText(mList.get(i).getDetails());
        viewHolder.price.setText("现价：￥"+mList.get(i).getCoupon_price());
        viewHolder.old_price.setText("劵后：￥" + mList.get(i).getPrice());

        SpannableString dis_money = new SpannableString("￥" + (Integer.valueOf(mList.get(i).getCoupon_price()) - Integer.valueOf(mList.get(i).getPrice())));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.7f);
        dis_money.setSpan(sizeSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.discounts_money.setText(dis_money);
        viewHolder.coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.buttonOnclick(mList.get(i).getCoupon_url());
            }
        });

        viewHolder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.buttonOnclick(mList.get(i).getBuy_url());
            }
        });

        return view;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView title;
        private TextView hint;
        private TextView price;
        private TextView old_price;
        private Button coupon;
        private Button buy;
        private TextView discounts_money;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public interface OnItemClickListener{
        void buttonOnclick(String url);

    }
}
