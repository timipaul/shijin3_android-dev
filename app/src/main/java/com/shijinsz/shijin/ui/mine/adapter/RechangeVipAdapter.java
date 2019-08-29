package com.shijinsz.shijin.ui.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.model.model.bean.VipPriceBean;
import com.shijinsz.shijin.R;

import java.util.List;

/**
 * Copyright (C)
 * FileName: RechangeVipAdapter
 * Author: paul
 * Date: 2019/5/12 11:45
 * Description: 充值选择适配器
 */
public class RechangeVipAdapter extends BaseAdapter {

    private Context mContent;
    public List<VipPriceBean> mList;
    protected LayoutInflater mInflater;
    protected final int mItemLayoutId;

    int[] price = {
            R.mipmap.price_99,
            R.mipmap.price_199,
            R.mipmap.price_399,
            R.mipmap.price_569,
            R.mipmap.price_999,
            R.mipmap.price_1159};

    String[] number = {"99","199","399","569","999","1159"};

    public RechangeVipAdapter(Context context, List<VipPriceBean> datas, int itemLayoutId){
        mContent = context;
        mList = datas;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() { return mList.size(); }

    @Override
    public Object getItem(int position) { return mList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mItemLayoutId,null);
            viewHolder.first_ic = view.findViewById(R.id.first_vip);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.old_price = view.findViewById(R.id.old_price);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(i == 0){
            view.setBackground(mContent.getResources().getDrawable(R.mipmap.rechang_view_on_bg));
        }

        viewHolder.old_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        if(i == 0 && ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_VIP_first_open).equals("on")){//临时显示
            viewHolder.first_ic.setVisibility(View.VISIBLE);
        }else{
            viewHolder.first_ic.setVisibility(View.GONE);
        }

        for(int k = 0;k < number.length; k++){
            if(mList.get(i).getSum().equals(number[k])){
                viewHolder.price.setImageDrawable(mContent.getResources().getDrawable(price[k]));
                break;
            }
        }

        viewHolder.title.setText(mList.get(i).getTooltip());
        viewHolder.old_price.setText("原价￥" + mList.get(i).getOriginal_price());
        return view;
    }
    class ViewHolder{
        private ImageView first_ic;
        private TextView title;
        private ImageView price;
        private TextView old_price;
    }
}
