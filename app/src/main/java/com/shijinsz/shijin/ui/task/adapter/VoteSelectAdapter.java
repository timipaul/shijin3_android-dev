package com.shijinsz.shijin.ui.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hongchuang.ysblibrary.model.model.bean.VoteBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.MyListView;

import java.util.List;

/**
 * Copyright (C)
 * FileName: VoteListViewAdapter
 * Author: m1342
 * Date: 2019/6/24 18:31
 * Description: 投票选择适配器
 */
public class VoteSelectAdapter extends BaseAdapter {

    private Context mContext;
    private VoteBean mList;
    protected final int mItemLayoutId;
    private int mListItemLayoutId;
    protected LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public VoteSelectAdapter(Context context, VoteBean list, int itemLayoutId,int mListItemLayoutId){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        mItemLayoutId = itemLayoutId;
        this.mListItemLayoutId = mListItemLayoutId;
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
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.listView = view.findViewById(R.id.listView);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(mList.getContent().getInfo().get(i).getTitle());
        List<VoteBean.Option> vto = mList.getContent().getInfo().get(i).getOption();
        VoteTextOptionAdapter itemAdapter = new VoteTextOptionAdapter(mContext,vto,mList.getContent().getCount() ,mListItemLayoutId);
        viewHolder.listView.setAdapter(itemAdapter);

        itemAdapter.setOnItemClickListener(new VoteTextOptionAdapter.OnItemClickListener() {
            @Override
            public void redionButtonOnclick(int position,int index) {
                for(int k = 0;k < mList.getContent().getInfo().get(i).getOption().size(); k++){
                    RadioButton rb = viewHolder.listView.getChildAt(k).findViewById(R.id.item_name);
                    if(k == position){
                        onItemClickListener.buttonDataMore(i,index);
                    }else{
                        rb.setChecked(false);
                    }
                }
            }
        });
        return view;

    }

    class ViewHolder{
        private TextView title;
        private MyListView listView;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;

    }

    public interface OnItemClickListener{
        void buttonDataMore(int item,int index);
    }
}
