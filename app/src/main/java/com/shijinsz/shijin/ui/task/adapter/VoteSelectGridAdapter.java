package com.shijinsz.shijin.ui.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;


import java.util.List;

/**
 * Copyright (C)
 * FileName: VoteSelectGridAdapter
 * Author: m1342
 * Date: 2019/6/27 16:57
 * Description: 图片投票适配器
 */
public class VoteSelectGridAdapter extends BaseAdapter {
    private Context mContext;
    private VoteBean mList;
    private int mItemLayoutId;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public VoteSelectGridAdapter(Context context, VoteBean list, int itemLayoutId){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mList.getContent().getInfo().size();
    }

    @Override
    public Object getItem(int position) {
        return mList.getContent().getInfo().get(position);
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
            viewHolder.img = view.findViewById(R.id.item_img);
            viewHolder.num_id = view.findViewById(R.id.num_id);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.layout = view.findViewById(R.id.button_layout);
            viewHolder.number = view.findViewById(R.id.number);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext).load(mList.getContent().getInfo().get(0).getOption().get(i).getUrl()).into(viewHolder.img);
        viewHolder.num_id.setText(String.valueOf(mList.getId()));
        viewHolder.name.setText(mList.getContent().getInfo().get(0).getOption().get(i).getTitle());
        viewHolder.number.setText(String.valueOf(mList.getContent().getInfo().get(0).getOption().get(i).getCount()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.layoutOnclick(i,mList.getContent().getInfo().get(0).getOption().get(i).getIndex());
            }
        });




        return view;
    }

    class ViewHolder{
        private TextView num_id;
        private ImageView img;
        private TextView name;
        private TextView number;
        private LinearLayout layout;

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;

    }

    public interface OnItemClickListener{
        void layoutOnclick(int position,String index);

    }
}
