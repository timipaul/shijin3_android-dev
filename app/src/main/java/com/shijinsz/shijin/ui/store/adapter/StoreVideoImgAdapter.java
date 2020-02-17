package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.StoreBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * 商城主页面页面 视频列表适配器
 */
public class StoreVideoImgAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreBean> mList;
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;
    private OnItemClick onItemClick;

    public StoreVideoImgAdapter(Context context,int layout, List<StoreBean> data){
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
            viewHolder.title = view.findViewById(R.id.title_item);
            viewHolder.play_num = view.findViewById(R.id.look_item);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(mList.get(i).getHomeImg() != null && !mList.get(i).getHomeImg().equals("")){
            Glide.with(mContext).load(mList.get(i).getHomeImg()).into(viewHolder.img);
        }else{
            Glide.with(mContext).load(mList.get(i).getUrl()).into(viewHolder.img);
        }
        viewHolder.title.setText(mList.get(i).getTitle());
        viewHolder.play_num.setText(mList.get(i).getPlay());

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onItemClick.buttonOnclick(mList.get(i),i);
            }
        });

        return view;
    }

    class ViewHolder{
        private ImageView img;
        private TextView title;
        private TextView play_num;
    }

    public void setOnItemClick(OnItemClick listener){
        this.onItemClick=listener;
    }

    public interface OnItemClick{
        void buttonOnclick(StoreBean data,int index);

    }
}
