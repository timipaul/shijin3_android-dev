package com.shijinsz.shijin.ui.store.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.ui.store.StoreGoodsDetails;
import com.shijinsz.shijin.utils.HorizontalListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 搜索 -- 商铺适配器 */
public class SeekShopAdapter extends BaseQuickAdapter<ShoppingShopBean, BaseViewHolder> {

    public interface onListen{
        void callback(String pos);
    }

    public void setOnListen(onListen onListen) {
        this.onListen = onListen;
    }

    public onListen onListen;

    public SeekShopAdapter(int layoutResId, @Nullable List<ShoppingShopBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingShopBean item) {


        Glide.with(mContext).load(item.getImgUrl()).into((CircleImageView)helper.getView(R.id.item_img));
        helper.setText(R.id.item_name,item.getName());
        GridView grid_img = helper.getView(R.id.grid_img);
        HorizontalListView grid_hint = helper.getView(R.id.list_hint);

        //图片适配器
        ShopImgItemAdapter imgAdapter = new ShopImgItemAdapter(mContext,R.layout.img_price_item,item.getGoodsList());
        grid_img.setAdapter(imgAdapter);
        imgAdapter.setOnItemClickListener(new ShopImgItemAdapter.OnItemClickListener() {
            @Override
            public void Onclick(String goodsId) {
                //商品点击 进入详情页
                Intent intent = new Intent(mContext,StoreGoodsDetails.class);
                intent.putExtra("goodsId",goodsId);
                mContext.startActivity(intent);
            }
        });

        ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
        if(item.getBanner() != null){
            for (int i = 0; i < item.getBanner().length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("text", item.getBanner()[i]);
                imagelist.add(map);
            }
            SimpleAdapter tvAdapter = new SimpleAdapter(mContext,imagelist,R.layout.text_item,new String[]{"text"},new int[]{R.id.item_text});
            grid_hint.setAdapter(tvAdapter);
        }

        Button but = helper.getView(R.id.item_button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListen.callback(item.get_id());
            }
        });
    }
}
