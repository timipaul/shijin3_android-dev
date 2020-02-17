package com.shijinsz.shijin.ui.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StroeCommentBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.store.adapter.CardOrderAdapter;
import com.shijinsz.shijin.ui.store.adapter.OrderRecordAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.HeaderView;
import com.shijinsz.shijin.utils.LoginUtil;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * 订单记录
 */
public class ShopOrderRecord extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.seek_but)
    EditText mSeekBut;
    @BindView(R.id.rg_order)
    RadioGroup mRgOrder;


    private boolean isRefresh = true;
    private OrderRecordAdapter adapter;
    private List<ShoppingShopBean> list = new ArrayList<>();

    private CardOrderAdapter cardAdapter;
    //private List<ShoppingShopBean> carData = new ArrayList<>();

    private int index = 1;
    //查询类型 card 卡片  goods商城订单
    private String query_type;
    //查询订单状态
    private String query_state;


    @Override
    public int bindLayout() {
        return R.layout.order_record_activity;
    }

    @Override
    public void initView(View view) {

        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("订单记录");
        showTitleBackButton();
        topView.setBackgroundColor(Color.WHITE);

        query_type = getIntent().getStringExtra("type");
        query_state = getIntent().getStringExtra("state");
        if (query_type.equals("") || query_state.equals("")) {
            finish();
            return;
        }


        refresh.setRefreshHeader(new HeaderView(mContext));
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        if(query_type.equals("CARD")){
            //卡片
            //query_state = "";
            getCartData();
            //mRgOrder.setVisibility(View.GONE);
            mSeekBut.setVisibility(View.GONE);
        }else{
            //商城
            getShoppingData();
        }
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                isRefresh = true;
                index = 1;
                initData();
            }
        });

        //System.out.println("获取评论列表");
        //getCommentData();
        //System.out.println("发布评论列表");
        //putCommentData();

        mSeekBut.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    if (!mSeekBut.getText().toString().equals("")) {
                        initData();
                    }
                }
                return false;
            }
        });

        mRgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                query_state = "";
                switch (i) {
                    case R.id.bt_all:
                        //全部 空就查全部
                        query_state = "";
                        break;
                    case R.id.bt_payment:
                        //待付款
                        query_state = "WAITING-PAYMENT";
                        break;
                    case R.id.bt_deliver:
                        //待发货
                        query_state = "WAITING-DELIVER-GOODS";
                        break;
                    case R.id.bt_take:
                        //待收货
                        query_state = "WAITING-RECEIVEING-GOODS";
                        break;
                    case R.id.bt_finish:
                        //已完成
                        query_state = "END";
                        break;
                }

                initData();
            }
        });


        switch (query_state){
            case "":
                mRgOrder.check(R.id.bt_all);
                break;
            case "WAITING-PAYMENT":
                mRgOrder.check(R.id.bt_payment);
                break;
            case "WAITING-DELIVER-GOODS":
                mRgOrder.check(R.id.bt_deliver);
                break;
            case "WAITING-RECEIVEING-GOODS":
                mRgOrder.check(R.id.bt_take);
                break;
            case "END":
                mRgOrder.check(R.id.bt_finish);
                break;
        }


        mStateView.showLoading();
        //initData();
    }

    //卡片记录
    private void getCartData() {

        cardAdapter = new CardOrderAdapter(R.layout.cart_order_record_item,list);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnList(new CardOrderAdapter.onListen() {
            @Override
            public void delCallback(String id, int position) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext);
                dialog.setTitle("操作提示");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        deleteShop(id);
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.setMessage("删除此订单信息");
                dialog.show();

            }

            @Override
            public void commentCall(String id) {
                putCommentData();
            }

            @Override
            public void showLogistice(ShoppingShopBean data) {
                //显示物流
                Intent intent = new Intent(mContext,LogisticsData.class);
                intent.putExtra("type",data.getGoods().getExpCode());
                intent.putExtra("order",data.getGoods().getExpNum());
                intent.putExtra("time",data.getGoods().getCreatedAt());

                startActivity(intent);

            }
        });

    }

    //商城
    private void getShoppingData() {
        adapter = new OrderRecordAdapter(R.layout.shop_order_record_item, list);
        recyclerView.setAdapter(adapter);
        adapter.setType(query_state);

        adapter.setOnList(new OrderRecordAdapter.onListen() {
            @Override
            public void delCallback(String id, int position) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext);
                dialog.setTitle("操作提示");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        deleteShop(id);
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.setMessage("删除此订单信息");
                dialog.show();

            }

            @Override
            public void commentCall(String id) {
                putCommentData();
            }

            @Override
            public void showLogistice(ShoppingShopBean data) {
                //显示物流
                Intent intent = new Intent(mContext,LogisticsData.class);
                intent.putExtra("type",data.getGoods().getExpCode());
                intent.putExtra("order",data.getGoods().getExpNum());
                intent.putExtra("time",data.getGoods().getCreatedAt());
                startActivity(intent);

            }
        });

    }

    private void initData() {

        Map map = new HashMap();
        map.put("pageIndex", index);
        map.put("pageSize", "10");
        map.put("name", mSeekBut.getText().toString());
        map.put("type", query_type);
        //['WAITING-PAYMENT', 'WAITING-DELIVER-GOODS', 'WAITING-RECEIVEING-GOODS', 'WAITING-EVALUATE', 'END', 'DELETE']
        //等待支付、等待发货、等待收货，待评价、订单结束、订单删除
        map.put("state", query_state);
        YSBSdk.getService(OAuthService.class).getOrderRecord(map, new YRequestCallback<BaseBean<ShoppingShopBean>>() {
            @Override
            public void onSuccess(BaseBean<ShoppingShopBean> var1) {
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    if (var1.getResult().size() == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                    }else{
                        rlEmpty.setVisibility(View.GONE);
                    }
                    list.clear();
                }

                list.addAll(var1.getResult());
                if (var1.getResult().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }

                if(query_type.equals("CARD")){
                    cardAdapter.notifyDataSetChanged();
                }else{
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showContent();
                    refresh.finishLoadMore();
                    refresh.finishRefresh();
                    mStateView.showRetry();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void deleteShop(String id) {
        YSBSdk.getService(OAuthService.class).delOrderRecord(id, new YRequestCallback<ShoppingShopBean>() {
            @Override
            public void onSuccess(ShoppingShopBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        index++;
        initData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        index = 1;
        isRefresh = true;
        initData();
    }


    public void putCommentData() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderDetailsId", "5dad86564c353f355c0e75c1");
        map.put("content", "你笑起来真好看，像春天的花一样");
        map.put("star", "5");
        YSBSdk.getService(OAuthService.class).putStoreComment(map, new YRequestCallback<StroeCommentBean>() {
            @Override
            public void onSuccess(StroeCommentBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    public void getCommentData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageIndex", "1");
        YSBSdk.getService(OAuthService.class).getStoreComment("5d8de20ad599a070c9b00a1e", map, new YRequestCallback<BaseBean<StroeCommentBean>>() {
            @Override
            public void onSuccess(BaseBean<StroeCommentBean> var1) {

            }

            @Override
            public void onFailed(String var1, String message) {

            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

}
