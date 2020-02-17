package com.shijinsz.shijin.ui.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.ShoppingShopBean;
import com.hongchuang.ysblibrary.model.model.bean.StoreGoodsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.store.ShopOrderActivity;
import com.shijinsz.shijin.ui.store.adapter.ShopcartAdapter;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

public class CartFragment extends BaseFragment implements ShopcartAdapter.CheckInterface,
        ShopcartAdapter.ModifyCountInterface, ShopcartAdapter.GroupEdtorListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;
    @BindView(R.id.exListView)
    ExpandableListView exListView;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.all_chekbox)
    CheckBox allChekbox;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_go_to_pay)
    TextView tvGoToPay;

    @BindView(R.id.ll_shar)
    RelativeLayout llShar;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;

    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.layout_cart_empty)
    LinearLayout cart_empty;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private ShopcartAdapter selva;
    private int flag = 0;
    private List<ShoppingShopBean> shopList = new ArrayList<>();
    private boolean isRefresh = true;
    private String isVIP;

    @Override
    protected int provideContentViewId() {
        return R.layout.store_car_view;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);

        isVIP = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_store_vip);
        initEvents();

    }

    @Override
    protected void loadData() {
        initDatas();
    }

    private void initEvents() {

        StatusBarUtil.setStatusTextColor(true, mActivity);

        selva = new ShopcartAdapter(shopList, mActivity);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setmListener(this);
        exListView.setAdapter(selva);
    }

    //修改购物车数量
    public void updateCart(String goodsId,int num){

        Map<String,Object> map = new HashMap<>();
        map.put("num",num);
        YSBSdk.getService(OAuthService.class).updateCarGoods(goodsId,map,new YRequestCallback<StoreGoodsBean>() {
            @Override
            public void onSuccess(StoreGoodsBean var1) {

                //Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();

                /*AlertDialog alert = new AlertDialog.Builder(mActivity).create();
                alert.setTitle("操作提示");
                alert.setMessage("修改数据成功");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                alert.show();*/
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mActivity,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    /**
     * 设置购物车产品数量.
     *
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < shopList.size(); i++) {
            shopList.get(i).setChoosed(allChekbox.isChecked());
            List<StoreGoodsBean> childs = shopList.get(i).getGoodsList();
            for (StoreGoodsBean goodsInfo : childs) {
                count += 1;
            }
        }


        //购物车已清空
        if(count==0){
            clearCart();
        } else{
            title.setText("购物车" + "(" + count + ")");
            cart_empty.setVisibility(View.GONE);
            llCart.setVisibility(View.VISIBLE);
        }
    }

    private void clearCart() {
        title.setText("购物车" + "(" + 0 + ")");
        subtitle.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        cart_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {

        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",20);
        map.put("pageIndex",1);
        YSBSdk.getService(OAuthService.class).getCarGoods(map,new YRequestCallback<BaseBean<ShoppingShopBean>>() {
            @Override
            public void onSuccess(BaseBean<ShoppingShopBean> var1) {

                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (isRefresh) {
                    shopList.clear();
                }

                shopList.addAll(var1.getResult());
                setCartNum();
                selva.notifyDataSetChanged();
                for (int i = 0; i < selva.getGroupCount(); i++) {
                    exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                }

                if (var1.getResult().size() < 10) {
                    refresh.setNoMoreData(true);
                } else {
                    refresh.setNoMoreData(false);
                }

            }

            @Override
            public void onFailed(String var1, String message) {
                //ErrorUtils.error(mActivity,var1,message);

            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();

            }
        });
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<ShoppingShopBean> toBeDeleteGroups = new ArrayList<ShoppingShopBean>();// 待删除的组元素列表
        List<String> listId = new ArrayList<>();
        for (int i = 0; i < shopList.size(); i++) {
            ShoppingShopBean group = shopList.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<StoreGoodsBean> toBeDeleteProducts = new ArrayList<StoreGoodsBean>();// 待删除的子元素列表
            List<StoreGoodsBean> childs = group.getGoodsList();
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                    listId.add(childs.get(j).get_id());
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("_idList",listId);
        YSBSdk.getService(OAuthService.class).deleteCarGoods(map,new YRequestCallback<StoreGoodsBean>() {
            @Override
            public void onSuccess(StoreGoodsBean var1) {

                AlertDialog alert = new AlertDialog.Builder(mActivity).create();
                alert.setTitle("操作提示");
                alert.setMessage("删除数据成功");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initDatas();
                            }
                        });
                alert.show();

                shopList.removeAll(toBeDeleteGroups);
                //记得重新设置购物车
                setCartNum();
                selva.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mActivity,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }

    private void doSubmit(){
        List<ShoppingShopBean> toBeDeleteGroups = new ArrayList<ShoppingShopBean>();// 待提交的组元素列表
        for (int i = 0; i < shopList.size(); i++) {
            ShoppingShopBean group = shopList.get(i);
            List<StoreGoodsBean> toBeDeleteProducts = new ArrayList<StoreGoodsBean>();// 待提交的子元素列表
            List<StoreGoodsBean> childs = group.getGoodsList();
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            if(toBeDeleteProducts.size() > 0){
                group.setGoodsList(toBeDeleteProducts);
                toBeDeleteGroups.add(group);
            }
        }


        Intent intent = new Intent(mActivity, ShopOrderActivity.class);
        Bundle data = new Bundle();
        data.putString("listBean",new Gson().toJson(toBeDeleteGroups));
        intent.putExtra("data",data);
        startActivity(intent);



    }


    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        ShoppingShopBean group = shopList.get(groupPosition);
        List<StoreGoodsBean> childs = group.getGoodsList();
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        ShoppingShopBean group = shopList.get(groupPosition);
        List<StoreGoodsBean> childs = group.getGoodsList();
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        StoreGoodsBean product = (StoreGoodsBean) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getNum();
        currentCount++;
        product.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");

        //修改购物车数据
        updateCart(product.get_id(),product.getNum());
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        StoreGoodsBean product = (StoreGoodsBean) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getNum();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        shopList.get(groupPosition).getGoodsList().remove(childPosition);
        //children.get(groups.get(groupPosition).getId()).remove(childPosition);
        ShoppingShopBean group = shopList.get(groupPosition);
        List<StoreGoodsBean> childs = group.getGoodsList();
        if (childs.size() == 0) {
            shopList.remove(groupPosition);
        }
        selva.notifyDataSetChanged();
        //     handler.sendEmptyMessage(0);
        calculate();
    }

    @Override
    public void groupEdit(int groupPosition) {
        shopList.get(groupPosition).setIsEdtor(true);
        selva.notifyDataSetChanged();
    }

    private boolean isAllCheck() {

        for (ShoppingShopBean group : shopList) {
            if (!group.isChoosed())
                return false;

        }

        return true;
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shopList.size(); i++) {
            ShoppingShopBean group = shopList.get(i);
            List<StoreGoodsBean> childs = group.getGoodsList();
            for (int j = 0; j < childs.size(); j++) {
                StoreGoodsBean product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    if(isVIP.equals("true") && product.getDiscount() != 0){
                        totalPrice += product.getDiscount() * product.getNum();
                    }else{
                        totalPrice += product.getPrice() * product.getNum();
                    }
                }
            }
        }

        tvTotalPrice.setText("¥" + totalPrice);
        tvGoToPay.setText("结算(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if(totalCount==0){
            setCartNum();
        } else{
            title.setText("购物车" + "(" + totalCount + ")");
        }
    }
    //
    @OnClick({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay, R.id.subtitle})
    public void onClick(View view) {
        AlertDialog alert;
        switch (view.getId()) {
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(mActivity, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(mActivity).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doDelete();
                            }
                        });
                alert.show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(mActivity, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                doSubmit();
                break;
            case R.id.subtitle:
                if (flag == 0) {
                    llInfo.setVisibility(View.GONE);
                    llShar.setVisibility(View.VISIBLE);
                    tvGoToPay.setVisibility(View.GONE);
                    subtitle.setText("完成");
                } else if (flag == 1) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvGoToPay.setVisibility(View.VISIBLE);
                    llShar.setVisibility(View.GONE);
                    subtitle.setText("编辑");
                }
                flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
        }
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < shopList.size(); i++) {
            shopList.get(i).setChoosed(allChekbox.isChecked());
            ShoppingShopBean group = shopList.get(i);
            List<StoreGoodsBean> childs = group.getGoodsList();
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            initDatas();
        }
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        initDatas();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
