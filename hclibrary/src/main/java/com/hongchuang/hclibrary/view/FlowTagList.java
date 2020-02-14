package com.hongchuang.hclibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;


public class FlowTagList extends FlowLayout {


    public FlowTagList(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public FlowTagList(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        // TODO Auto-generated constructor stub
    }
    /*public FlowTagList(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        // TODO Auto-generated constructor stub
    }*/
    private List<FlowTag> mDataList = new ArrayList<FlowTag>();
    private onItemClickListener mListenr;
    //添加一个子view到listview中，并使其和tag类数据绑定
    private void inflateTag(final FlowTag tag) {
        if (tag == null)
            return;

        //这里选择插入一个textview。也可以自定义layout的view通过LayoutInflater引入布局，总之添加进来一个子view就ok。
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seek_tv_item, null);
        TextView item = (TextView) view.findViewById(R.id.text_item);
        item.setText(tag.getTitle());
        addView(view);

        /*TextView tv = new TextView(getContext());
        tv.setText(tag.getTitle());
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.seek_bj_ic);
        tv.setPadding(10, 10, 10, 10);*/
        //设置点击事件，并传递给接口。当然不只是能设置点击事件，任何事件都可以设置，如果需要只要稍微修改一下接口或者添加新的接口就OK
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListenr != null) {
                    mListenr.onClick(v, tag);
                }
            }
        });
    }
    //添加数据
    public void setData(List<FlowTag> list) {
        if (list == null || list.isEmpty())
            return;
        this.mDataList = list;
        for (FlowTag tag : mDataList)
            inflateTag(tag);
    }

    //设置子item点击事件
    public void setOnItemClickListener(onItemClickListener listener) {
        this.mListenr = listener;
    }

    //自定义一个接口，用来传递点击事件
    public interface onItemClickListener {
        public void onClick(View v, FlowTag tag);
    }
}
