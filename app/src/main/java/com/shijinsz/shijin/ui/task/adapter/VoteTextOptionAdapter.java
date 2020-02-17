package com.shijinsz.shijin.ui.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.MyProgressBar;

import java.util.List;


/**
 * Copyright (C)
 * FileName: VoteTextOptionAdapter
 * Author: m1342
 * Date: 2019/6/25 18:13
 * Description: 投票基本选项适配器
 */
public class VoteTextOptionAdapter extends BaseAdapter {
    private Context mContext;
    private List<VoteBean.Option> mList;
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private String mCount;

    public VoteTextOptionAdapter(Context context, List<VoteBean.Option> list,String count, int itemLayoutId){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        mCount = count;
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
            viewHolder.title = view.findViewById(R.id.item_name);
            viewHolder.bar = view.findViewById(R.id.myProgressBar);
            viewHolder.number = view.findViewById(R.id.number);

            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(mList.get(i).getTitle());
        String num = mList.get(i).getCount();
        //计算出比例
        int ratio = 0;
        try {
            ratio = (int) (Double.valueOf(num) / Double.valueOf(mCount) * 100);
        }catch (Exception e) {
        }
        viewHolder.bar.setProgress(ratio);
        viewHolder.number.setText(num + "人 ("+ ratio +"%)");
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.redionButtonOnclick(i,Integer.valueOf(mList.get(i).getIndex()));
            }
        });

        return view;

    }

    class ViewHolder{
        private RadioButton title;
        private MyProgressBar bar;
        private TextView number;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;

    }

    public interface OnItemClickListener{
        //position 视图下标位置  index 给后台选项位置
        void redionButtonOnclick(int position,int index);

    }
}
