package com.hongchuang.hclibrary.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/***
 * 功能描述:RevyclerView的基础封装adapter
 * 作者:qiujialiu
 * 时间:2017/6/3
 ***/

public abstract class BaseRecyclerViewAdapter<T, M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<M> {
    public final static int ITEM_HEAD = 0;
    public final static int ITEM_CONTENT = 1;
    public final static int TYPE_PULL_REFRESH = 100;
    public final static int TYPE_PULL_MORE = 200;
    protected final Context context;
    protected List<T> datas = new ArrayList<>();
    protected OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(M holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(v, position);
                }
                return false;
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public List<T> getDatas() {
        if (datas == null)
            datas = new ArrayList<>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            if (this.datas == null) {
                this.datas = new ArrayList<>();
            }
            this.datas.addAll(dataList);
            notifyDataSetChanged();
        }
    }
}
