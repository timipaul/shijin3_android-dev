package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hongchuang.hclibrary.utils.AndroidSystemUtil;
import com.shijinsz.shijin.R;

import java.util.List;

/** 商城详情中的多图适配器 */
public class SimpleImageAdapter extends BaseAdapter {

    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    private List<String> mList;
    private Context mContext;

    //屏幕宽度
    private int phone_width;

    public SimpleImageAdapter(Context context, int itemLayoutId, List<String> list){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        mItemLayoutId = itemLayoutId;
        phone_width = new AndroidSystemUtil(mContext).Width();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.imageView = view.findViewById(R.id.item_img);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .asBitmap()
                .load(mList.get(i))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        double ratio = (double) bitmap.getWidth() / (double) phone_width;
                        viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(phone_width, (int) (bitmap.getHeight() / ratio)));
                        viewHolder.imageView.setImageBitmap(bitmap);
                    }
                });
        return view;
    }

    class ViewHolder{
        private ImageView imageView;
    }
}
