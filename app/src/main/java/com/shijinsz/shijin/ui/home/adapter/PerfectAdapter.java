package com.shijinsz.shijin.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.ysblibrary.model.model.bean.PerfectBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class PerfectAdapter extends BaseAdapter{
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<PerfectBean> mDatas;
    protected final int mItemLayoutId;
    public PerfectAdapter(Context context, List<PerfectBean> data, int itemLayoutId) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDatas = data;
        mItemLayoutId = itemLayoutId;

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public PerfectBean getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.img = view.findViewById(R.id.img);
            viewHolder.textView = view.findViewById(R.id.tv_name);
            viewHolder.check = view.findViewById(R.id.checkbox);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.img.setImageResource(mDatas.get(i).getRes());
        viewHolder.textView.setText(mDatas.get(i).getName());
        if (mDatas.get(i).isCheck()){
            viewHolder.check.setImageResource(R.mipmap.bt_sex_checked);
        }else {
            viewHolder.check.setImageResource(R.mipmap.bt_uncheck);
        }
        return view;
    }
    class ViewHolder{
        private ImageView img;
        private TextView textView;
        private ImageView check;
    }
}
