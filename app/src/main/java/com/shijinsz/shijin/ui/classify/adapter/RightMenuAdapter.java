package com.shijinsz.shijin.ui.classify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.GoodsClassifyBean;
import com.shijinsz.shijin.R;

import java.util.List;

/* 分类二级菜单适配器 */
public class RightMenuAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<GoodsClassifyBean.Child.ThreeChild> mDatas;
    protected final int mItemLayoutId;


    public RightMenuAdapter(Context context, List<GoodsClassifyBean.Child.ThreeChild> data, int itemLayoutId) {
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
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.img = view.findViewById(R.id.image);
            viewHolder.name = view.findViewById(R.id.title);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(mContext).load(mDatas.get(i).getCoverImg()).into(viewHolder.img);
        viewHolder.name.setText(mDatas.get(i).getChName());

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.callback(mDatas.get(i));
            }
        });

        return view;
    }

    class ViewHolder {
        private ImageView img;
        private TextView name;
    }

    public interface onListen {
        void callback(GoodsClassifyBean.Child.ThreeChild pos);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;


}
