package com.shijinsz.shijin.ui.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.store.StoreGoodsDetails;

import java.util.List;

/** 搜索 抢购 -- 商品适配器 */
public class SeekGoodsAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    public List<StoreGoodsBean> mDatas;
    protected final int mItemLayoutId;
    private ItemOnclick onItemClick;



    public SeekGoodsAdapter(Context context, List<StoreGoodsBean> data, int itemLayoutId) {
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
            viewHolder.img = view.findViewById(R.id.item_img);
            viewHolder.name = view.findViewById(R.id.item_name);
            viewHolder.newPrice = view.findViewById(R.id.item_new_price);
            viewHolder.oldPrice = view.findViewById(R.id.item_old_price);
            viewHolder.bt_add = view.findViewById(R.id.item_bt_add);
            viewHolder.bt_minus = view.findViewById(R.id.item_bt_minus);
            viewHolder.tv_num = view.findViewById(R.id.tv_car_num);
            viewHolder.layout = view.findViewById(R.id.num_layout);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }


        if(mDatas.get(i).getCoverImg() != null){
            Glide.with(mContext).load(mDatas.get(i).getCoverImg()[0]).into(viewHolder.img);
        }else{
            Glide.with(mContext).load(mDatas.get(i).getGoodsImg()).into(viewHolder.img);
        }

        viewHolder.name.setText(mDatas.get(i).getName());


        if(mDatas.get(i).getDiscount() == 0){
            viewHolder.oldPrice.setText("");
            viewHolder.newPrice.setText("¥"+mDatas.get(i).getPrice());
        }else{
            viewHolder.newPrice.setText("¥"+mDatas.get(i).getDiscount());
            viewHolder.oldPrice.setText(""+mDatas.get(i).getPrice());
            viewHolder.oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线（删除线）
        }
        try {
            viewHolder.tv_num.setText(String.valueOf(mDatas.get(i).getGoodsNum()));
        } catch (Exception e) {
            viewHolder.tv_num.setText("0");
        }

        viewHolder.layout.setVisibility(View.GONE);

        if(mDatas.get(i).getGoodsNum() == 0){
            viewHolder.bt_minus.setVisibility(View.GONE);
            viewHolder.tv_num.setVisibility(View.GONE);
        }else{
            viewHolder.bt_minus.setVisibility(View.VISIBLE);
            viewHolder.tv_num.setVisibility(View.VISIBLE);

        }

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //商品点击 进入详情页
                onItemClick.itemClickInto(mDatas.get(i).get_id());
            }
        });

       viewHolder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //增加
                onItemClick.addClick(mDatas.get(i).get_id(),viewHolder.tv_num,viewHolder.bt_minus);
            }
        });
        viewHolder.bt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //减少
                onItemClick.minusClick(mDatas.get(i).get_id(),viewHolder.tv_num,viewHolder.bt_minus);
            }
        });

        return view;
    }

    class ViewHolder{
        private ImageView img;
        private TextView name;
        private TextView newPrice;
        private TextView oldPrice;
        private Button bt_add;
        private Button bt_minus;
        private TextView tv_num;
        private RelativeLayout layout;
    }

    public void setOnItemClickListener(ItemOnclick onclick){
        this.onItemClick = onclick;
    }

    public interface ItemOnclick{
        void addClick(String goodsId,TextView num,Button bt);
        void minusClick(String goodsId,TextView num,Button bt);
        void itemClickInto(String goodsId);
    }


}
