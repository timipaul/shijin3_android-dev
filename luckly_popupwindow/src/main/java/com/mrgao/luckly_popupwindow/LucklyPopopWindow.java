package com.mrgao.luckly_popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mrgao.luckly_popupwindow.adapter.ListDataAdapter;
import com.mrgao.luckly_popupwindow.beans.DataBeans;
import com.mrgao.luckly_popupwindow.utils.PopouBackView;
import com.mrgao.luckly_popupwindow.utils.PopupWindowUtils;
import com.mrgao.luckly_popupwindow.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.gao on 2018/1/24.
 * Package:    com.mrgao.popupwindowviews
 * Create Date:2018/1/24
 * Project Name:PopupWindowViews
 * Description:
 */

public class LucklyPopopWindow extends PopupWindow {
    private static final String TAG = "LucklyPopopWindow";
    private Context mContext;
    //PopupWindow的contentView
    private View mContentView;
    //RecyclerView
    private RecyclerView mRecyclerView;
    //Adapter
    private ListDataAdapter mAdapter;
    //三角形的宽度
    private int mTriangleWidth = 40;
    //三角形的高度
    private int mTrianleHeight = 30;
    //圆角的半径
    private int mRadius = 10;
    //字体的颜色
    private int mTextColor = Color.BLACK;
    //背景颜色
    private int mBackgroundColor = Color.WHITE;
    //背景为灰色的程度
    private float mDarkBackgroundDegree = 0.6f;
    //自绘制三角形圆角背景
    private PopouBackView mPopouBackView;
    //分割线横向布局
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    //分割线纵向布局
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    public TextView mCancelTv;
    private String nickname;
    private List<String> interests;
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,commit;
    private int index = 0;
    private TextView num;
    private Boolean isBt1=false,isBt2=false,isBt3=false,isBt4=false,isBt5=false,isBt6=false,isBt7=false;

    private List<DataBeans> mBeansList = new ArrayList<>();

    public LucklyPopopWindow(Context context,String nickname,List<String> interests) {
        super(context);
        mContext = context;
        this.nickname = nickname;
        this.interests = interests;
        initContentView();
    }


    /**
     * 初始化contentView
     */
    private void initContentView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.popuper, null);
        bt1 = mContentView.findViewById(R.id.bt1);
        bt2 = mContentView.findViewById(R.id.bt2);
        bt3 = mContentView.findViewById(R.id.bt3);
        bt4 = mContentView.findViewById(R.id.bt4);
        bt5 = mContentView.findViewById(R.id.bt5);
        bt6 = mContentView.findViewById(R.id.bt6);
        bt7 = mContentView.findViewById(R.id.bt7);
        commit = mContentView.findViewById(R.id.commit);
        num = mContentView.findViewById(R.id.num);
        num.setText(index+"");
        bt4.setText("拉黑作者:"+nickname);
        LinearLayout ln4 =mContentView.findViewById(R.id.ln4);
        LinearLayout ln5 =mContentView.findViewById(R.id.ln5);
        switch (interests.size()){
            case 0:
                ln4.setVisibility(View.GONE);
                ln5.setVisibility(View.GONE);
                break;
            case 1:
                bt5.setText("不想看:"+interests.get(0));
                bt6.setVisibility(View.INVISIBLE);
                ln4.setVisibility(View.VISIBLE);
                ln5.setVisibility(View.GONE);
                break;
            case 2:
                bt5.setText("不想看:"+interests.get(0));
                bt6.setVisibility(View.VISIBLE);
                bt6.setText("不想看:"+interests.get(1));
                ln4.setVisibility(View.VISIBLE);
                ln5.setVisibility(View.GONE);
                break;
            case 3:
                bt5.setText("不想看:"+interests.get(0));
                bt6.setVisibility(View.VISIBLE);
                bt6.setText("不想看:"+interests.get(1));
                bt7.setText("不想看:"+interests.get(2));
                ln4.setVisibility(View.VISIBLE);
                ln5.setVisibility(View.VISIBLE);
                break;
        }
        setContentView(mContentView);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setAnimationStyle(R.style.AlphaPopupWindow);
        setOutsideTouchable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt1){
                    index--;
                    num.setText(index+"");
                    isBt1 = false;
                    bt1.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt1.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt1 = true;
                    bt1.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt1.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt2){
                    index--;
                    num.setText(index+"");
                    isBt2 = false;
                    bt2.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt2.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt2 = true;
                    bt2.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt2.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt3){
                    index--;
                    num.setText(index+"");
                    isBt3 = false;
                    bt3.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt3.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt3 = true;
                    bt3.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt3.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt4){
                    index--;
                    num.setText(index+"");
                    isBt4 = false;
                    bt4.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt4.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt4 = true;
                    bt4.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt4.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt5){
                    index--;
                    num.setText(index+"");
                    isBt5 = false;
                    bt5.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt5.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt5 = true;
                    bt5.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt5.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt6){
                    index--;
                    num.setText(index+"");
                    isBt6 = false;
                    bt6.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt6.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt6 = true;
                    bt6.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt6.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBt7){
                    index--;
                    num.setText(index+"");
                    isBt7 = false;
                    bt7.setBackground(mContext.getResources().getDrawable(R.drawable.bt_uncheck));
                    bt7.setTextColor(mContext.getResources().getColor(R.color.uncheck));
                }else {
                    index++;
                    num.setText(index+"");
                    isBt7 = true;
                    bt7.setBackground(mContext.getResources().getDrawable(R.drawable.bt_checked));
                    bt7.setTextColor(mContext.getResources().getColor(R.color.checked));
                }
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(isBt1,isBt2,isBt3,isBt4,isBt5,isBt6,isBt7);
                }
            }
        });
    }



    /**
     * 添加数据
     *
     * @param strings
     */
    public void setData(DataBeans[] strings) {
        if (mAdapter != null) {
            mAdapter.setData(strings);
        }
    }

    public void setData(String[] data, int[] images) {
        mBeansList.clear();
        if (data.length == images.length) {
            Bitmap bitmap = null;
            for (int i = 0; i < images.length; i++) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), images[i]);
                DataBeans dataBeans = new DataBeans(bitmap, data[i]);
                mBeansList.add(dataBeans);
            }
            setData(mBeansList);
        }
    }

    public void setData(String[] data, Bitmap[] images) {
        mBeansList.clear();
        if (data.length == images.length) {
            Bitmap bitmap = null;
            for (int i = 0; i < images.length; i++) {
                bitmap = images[i];
                DataBeans dataBeans = new DataBeans(bitmap, data[i]);
                mBeansList.add(dataBeans);
            }
            setData(mBeansList);
        }
    }

    public void setData(String[] data) {
        mBeansList.clear();
        for (int i = 0; i < data.length; i++) {
            DataBeans dataBeans = new DataBeans();
            dataBeans.setData(data[i]);
            mBeansList.add(dataBeans);
        }
        setData(mBeansList);
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void setData(List<DataBeans> list) {
        if (mAdapter != null) {
            mAdapter.setData(list);
        }
    }

    @Override
    public View getContentView() {
        return mContentView;
    }



    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    /**
     * 设置背景为灰色的程度
     *
     * @param darkBackgroundDegree
     */
    public void setDarkBackgroundDegree(float darkBackgroundDegree) {
        if (darkBackgroundDegree >= 0.0f && darkBackgroundDegree <= 1.0f)
            mDarkBackgroundDegree = darkBackgroundDegree;
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgcolor;
        if (bgcolor == 1) {
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener onItemClickListener;

    /**
     * 设置箭头的宽度
     *
     * @param triangleWidth
     */
    public void setTriangleWidth(int triangleWidth) {
        mTriangleWidth = triangleWidth;
        update();
    }

    /**
     * 设置剪头的高度
     *
     * @param trianleHeight
     */
    public void setTrianleHeight(int trianleHeight) {
        mTrianleHeight = trianleHeight;
        update();
    }

    /**
     * 设置圆角矩形半径
     *
     * @param radius
     */
    public void setRadius(int radius) {
        mRadius = radius;
        update();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public ListDataAdapter getAdapter() {
        return mAdapter;
    }

    public int getTriangleWidth() {
        return mTriangleWidth;
    }

    public int getTrianleHeight() {
        return mTrianleHeight;
    }

    public int getRadius() {
        return mRadius;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public float getDarkBackgroundDegree() {
        return mDarkBackgroundDegree;
    }

    public interface OnItemClickListener {
        void onItemClick(boolean see,boolean interested,boolean content_level,boolean black_user,boolean black_label1,boolean black_label2,boolean black_label3);
    }

    /**
     * 在positionView的位置显示popupWindow;
     * 显示的时候 首先获取到背景View;然后将其设置为背景图片
     */
    public void showAtLocation(View parentView, View positionView) {
        int[] contentPosition = PopupWindowUtils.calculatePopupWindowPos(mContentView, positionView, mTrianleHeight, getWidth());
        int[] centerPosition = PopupWindowUtils.getPositionViewCenterPos(positionView);

//        mRecyclerView.setBackground(null);
//        mRecyclerView.setAlpha(1.0f);
        mPopouBackView = new PopouBackView(mContext);
        mPopouBackView.setContentPosition(contentPosition);
        mPopouBackView.setPosCenterPosition(centerPosition);
        mPopouBackView.setRadius(mRadius);
        mPopouBackView.setPosViewHeight(positionView.getMeasuredHeight());
        mPopouBackView.setViewWidth(getWidth());//注意这里传入的参数为popop的宽度
        mPopouBackView.setViewHeight(mContentView.getMeasuredHeight());
        mPopouBackView.setShowDown(PopupWindowUtils.isShowDown(mContentView, positionView, mTrianleHeight));
        mPopouBackView.setTranWidth(mTriangleWidth);
        mPopouBackView.setTranHeight(mTrianleHeight);
        mPopouBackView.setBackColor(mBackgroundColor);

        Bitmap bitmap = mPopouBackView.convertViewToBitmap();
        Drawable drawable = new BitmapDrawable(null, bitmap);
        update();
        setBackgroundDrawable(drawable);
        darkenBackground(mDarkBackgroundDegree);//设置背景框为灰色
//        contentPosition[0] -= 20;
        showAtLocation(parentView, Gravity.TOP | Gravity.START, contentPosition[0], contentPosition[1]);

    }

    public ViewGroup.LayoutParams setViewMargin(boolean isDp, int left, int right, int top, int bottom) {
        if (mRecyclerView == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = ScreenUtils.dp2px(mContext, left);
            rightPx = ScreenUtils.dp2px(mContext, right);
            topPx = ScreenUtils.dp2px(mContext, top);
            bottomPx = ScreenUtils.dp2px(mContext, bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);

        mRecyclerView.setLayoutParams(marginParams);
        mCancelTv.setLayoutParams(marginParams);
        return marginParams;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        if (mRecyclerView != null) {
            mRecyclerView.setPadding(left, top, right, bottom);
        }

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }
}
