package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商铺图爿适配器 */
public class ShopImgItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreGoodsBean> mdata;
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;


    public ShopImgItemAdapter(Context context,int layoutResId, @Nullable List<StoreGoodsBean> data){
        this.mContext = context;
        this.mdata = data;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = layoutResId;

    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.img = view.findViewById(R.id.item_img);
            viewHolder.price = view.findViewById(R.id.item_price);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext).load(mdata.get(i).getGoodsImg()).into(viewHolder.img);
        viewHolder.price.setText("¥"+mdata.get(i).getPrice());
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.Onclick(mdata.get(i).get_id());
            }
        });
        return view;
    }

    public class ViewHolder{
        private ImageView img;
        private TextView price;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public interface OnItemClickListener{
        void Onclick(String goodsId);

    }
}
