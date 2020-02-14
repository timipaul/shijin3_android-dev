package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;

/* 商城礼包适配器 */
public class GiftChangeAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreGoodsBean> mList;
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;
    private OnItemClick onItemClick;

    public GiftChangeAdapter(Context context, int layout, List<StoreGoodsBean> data){
        this.mContext = context;
        this.mList = data;
        this.mInflater = LayoutInflater.from(context);
        mItemLayoutId = layout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
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
            viewHolder.img = view.findViewById(R.id.img_item);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }



        Glide.with(mContext).load(mList.get(i).getHomeImg()).into(viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.buttonOnclick(mList.get(i));
            }
        });

        return view;
    }

    class ViewHolder{
        private ImageView img;

    }

    public void setOnItemClick(OnItemClick listener){
        this.onItemClick=listener;
    }

    public interface OnItemClick{
        void buttonOnclick(StoreGoodsBean data);

    }
}
