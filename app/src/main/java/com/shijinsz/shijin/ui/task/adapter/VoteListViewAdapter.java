package com.shijinsz.shijin.ui.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VoteListViewAdapter
 * Author: m1342
 * Date: 2019/6/24 18:31
 * Description: 投票显示适配器
 */
public class VoteListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<VoteBean> mList;
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public VoteListViewAdapter(Context context, List<VoteBean> list, int itemLayoutId){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
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
            viewHolder.name = view.findViewById(R.id.item_name);
            viewHolder.date = view.findViewById(R.id.item_end_date);
            viewHolder.num = view.findViewById(R.id.item_num);
            viewHolder.state = view.findViewById(R.id.item_button);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //String time= TimeUtil.format(Long.valueOf(mList.get(i).getEnd_time())*1000,"yyyy-MM-dd HH:mm");

        viewHolder.name.setText(mList.get(i).getContent().getTitle());
        viewHolder.date.setText(mContext.getString(R.string.vote_end_date,mList.get(i).getEnd_time()));
        viewHolder.num.setText(mContext.getString(R.string.vote_join_num,mList.get(i).getCount()));
        if(mList.get(i).getVoted().equals("true")){
            viewHolder.state.setText("已投票");
            viewHolder.state.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.state.setBackgroundResource(R.drawable.bt_red_15);

        }else{
            viewHolder.state.setText("参与投票");
            viewHolder.state.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.state.setBackgroundResource(R.drawable.bt_whi_15);
        }

        viewHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.buttonOnclick();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.layoutOnclick(i,mList.get(i));
            }
        });
        return view;
    }

    class ViewHolder{
        private TextView name;
        private TextView date;
        private TextView num;
        private Button state;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public interface OnItemClickListener{
        void buttonOnclick();
        void layoutOnclick(int index,VoteBean voteBean);
    }
}
