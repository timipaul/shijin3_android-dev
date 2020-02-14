package com.shijinsz.shijin.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nukc.stateview.StateView;
import com.hongchuang.ysblibrary.widget.NoticeDialog;

import butterknife.ButterKnife;


/**
 * @description: Fragment的基类
 */

public abstract class BaseFragment extends LazyLoadFragment {

    protected StateView mStateView;//用于显示加载中、网络异常，空布局、内容布局
    protected Activity mActivity;
    protected LayoutInflater mInflater;
    //    protected T mPresenter;
    private View rootView;
    public NoticeDialog mailBoxDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mPresenter = createPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            ButterKnife.bind(this, rootView);

            mStateView = StateView.inject(getStateViewRoot());

            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }

    public void SetStateViewRoot(View view) {
        mStateView = StateView.inject(view);
        if (mStateView != null) {
//            mStateView.setLoadingResource(R.layout.page_loading);
//            mStateView.setRetryResource(R.layout.page_net_error);
//            mStateView.setEmptyResource(R.layout.page_detail_error);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 初始化一些view
     *
     * @param rootView
     */
    public void initView(View rootView) {
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置listener的操作
     */
    public void initListener() {

    }


    @Override
    protected void onFragmentFirstVisible() {
        //当第一次可见的时候，加载数据
        loadData();
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
//    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    //加载数据
    protected abstract void loadData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
    }

    public void showNoRealNameDiaglog(final Context context, View.OnClickListener onClickListener) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        RelativeLayout mailBoxLay = (RelativeLayout) inflater.inflate(
//                R.layout.no_realname_diglog, null);
//        Button yd_btn = (Button) mailBoxLay.findViewById(R.id.commit);
//         mailBoxDialog = new NoticeDialog(context);
//        mailBoxDialog.showDialog(mailBoxLay, 0, 0);
//        yd_btn.setOnClickListener(onClickListener);
//        yd_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }


}
