package com.shijinsz.shijin.ui.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.ysblibrary.model.model.bean.VipRechangeBean;
import com.shijinsz.shijin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C)
 * FileName: RedPacketAdapter
 * Author: paul
 * Date: 2019/5/13 11:23
 * Description: 红包点击适配器
 */
public class RedPacketAdapter extends BaseAdapter {

    private Context mContent;
    public List<VipRechangeBean> mList = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;
    private OnItemClickListener onItemClickListener;
    private String day_time;
    private boolean day_cilck = true;
    //当前数据条数
    public int list_sum;

    public RedPacketAdapter(Context context, List<VipRechangeBean> datas, int itemLayoutId){
        mContent = context;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;

        if(datas.size() > 3){
            for(int i = 0;i < 3; i++){
                mList.add(datas.get(i));
            }
        }else{
            mList = datas;
        }
        list_sum = mList.size();
        //数据反转
        Collections.reverse(mList);
        //判断红包显示数量小于六条就加入数据
        if(list_sum < 6){
            for(int i = 0;i < 6-list_sum; i++){
                VipRechangeBean price = new VipRechangeBean();
                price.setRed_package_number(0);
                price.setGet_time("0");
                mList.add(price);
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        day_time = df.format(new Date());
        if(list_sum > 0){
            if(mList.get(list_sum-1).getGet_time().equals(day_time)){
                day_cilck = false;
            }
        }



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
            viewHolder.img = view.findViewById(R.id.img);
            viewHolder.num = view.findViewById(R.id.tv_vip_num);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(list_sum == 0 && i == 0 || i == list_sum && day_cilck){
            day_cilck = false;
            viewHolder.img.setImageDrawable(mContent.getResources().getDrawable(R.mipmap.red_packet_click));
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(i,viewHolder.img);
                }
            });
        }else if(mList.get(i).getGet_time().equals("0")){
            viewHolder.img.setImageDrawable(mContent.getResources().getDrawable(R.mipmap.red_packet_off));

        }else if(mList.get(i).getGet_time().equals(day_time)){
            viewHolder.num.setText("￥" + mList.get(i).getRed_package_number());
            viewHolder.img.setImageDrawable(mContent.getResources().getDrawable(R.mipmap.red_packet_on));

        }else{
            viewHolder.num.setText("￥" + mList.get(i).getRed_package_number());
            viewHolder.img.setImageDrawable(mContent.getResources().getDrawable(R.mipmap.red_packet_on));
        }

        return view;
    }
    class ViewHolder{
        private ImageView img;
        private TextView num;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;

    }
    public interface OnItemClickListener{
        void onItemClick(int index,View view);
    }
}
