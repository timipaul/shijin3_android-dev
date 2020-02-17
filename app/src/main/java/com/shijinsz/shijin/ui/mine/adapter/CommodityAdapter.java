package com.shijinsz.shijin.ui.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Copyright (C)
 * FileName: CommodityAdapter
 * Author: m1342
 * Date: 2019/5/14 17:41
 * Description: 会员页面 兑换专区适配器
 */
public class CommodityAdapter extends BaseAdapter {

    private Context mContent;
    public List<GoodsBean> mList;
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;

    public CommodityAdapter(Context context, List<GoodsBean> datas, int itemLayoutId){
        mContent = context;
        mList = datas;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() { return mList.size(); }

    @Override
    public Object getItem(int position) { return mList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view==null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.img = view.findViewById(R.id.goods_img);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.gold_num = view.findViewById(R.id.gold_num);
            viewHolder.person_num = view.findViewById(R.id.person_num);
            viewHolder.tv_one = view.findViewById(R.id.lable_1);
            viewHolder.tv_two = view.findViewById(R.id.lable_2);
            viewHolder.tv_three = view.findViewById(R.id.lable_3);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContent).load(mList.get(i).getGoods_imgurl()).into(viewHolder.img);
        viewHolder.name.setText(mList.get(i).getGoods_title());


        String price = String.format(mContent.getString(R.string.commodity_prices),mList.get(i).getExchange_point());
        String num = String.format(mContent.getString(R.string.number_people),mList.get(i).getExchange_number());
        SpannableString goldString = new SpannableString(price);
        SpannableString numString = new SpannableString(num);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#e01f22"));
        goldString.setSpan(colorSpan,2,mList.get(i).getExchange_point().length()+2,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        numString.setSpan(colorSpan,0,mList.get(i).getExchange_number().length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.gold_num.setText(goldString);
        viewHolder.person_num.setText(numString);
        viewHolder.tv_one.setText(mList.get(i).getGoods_describe_one());


        try {
            if(!mList.get(i).getGoods_describe_two().equals("")){
                viewHolder.tv_two.setText(mList.get(i).getGoods_describe_two());
                viewHolder.tv_two.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
        }

        try {
            if(!mList.get(i).getGoods_describe_three().equals("")){
                viewHolder.tv_three.setText(mList.get(i).getGoods_describe_three());
                viewHolder.tv_three.setVisibility(View.VISIBLE);
            }
        }catch (Exception e) {
        }



        return view;
    }
    class ViewHolder{
        private ImageView img;
        private TextView name;
        private TextView gold_num;
        private TextView person_num;
        private TextView tv_one;
        private TextView tv_two;
        private TextView tv_three;
    }


}
