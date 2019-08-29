package com.shijinsz.shijin.ui.mine.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hongchuang.hclibrary.recyclerview.PowerfulRecyclerView;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.GoodsBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseFragment;
import com.shijinsz.shijin.ui.mine.MySwopActivity;
import com.shijinsz.shijin.ui.mine.UserSiteMessageActivity;
import com.shijinsz.shijin.ui.wallet.adapter.PointAdapter;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit.callback.YRequestCallback;

/**
 * 普通兑换
 */
public class CommonFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    PowerfulRecyclerView recyclerView;

    private TextView tv_money;

    private PointAdapter adapter;
    private DialogUtils dialogUtils;
    private List<GoodsBean> list = new ArrayList<>();
    private int point = 0;
    private int exchangePoint = 0;

    @Override
    protected int provideContentViewId() {
        return R.layout.new_list_fragment;
    }

    @Override
    protected void loadData() {
        dialogUtils = new DialogUtils(getContext());
        tv_money = getActivity().findViewById(R.id.money);
        if (!tv_money.getText().toString().isEmpty())
            point = Integer.parseInt(tv_money.getText().toString());

        adapter = new PointAdapter(R.layout.point_good_item, list);
        recyclerView.setAdapter(adapter);

        adapter.setOnListen(new PointAdapter.OnListen() {
            @Override
            public void call(int pos) {
                String text_hint = getString(R.string.exchange_dialog);
                int goods_price = Integer.parseInt(list.get(pos).getExchange_point());
                if(goods_price == 0){
                    text_hint = "领取商品!";
                }

                dialogUtils.showCommentDialog(text_hint, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogUtils.dismissCommentDialog();
                        //商品大于余额
                        if (point < goods_price) {
                            dialogUtils.showCommentDialog(getString(R.string.exchange_fail), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogUtils.dismissCommentDialog();
                                }
                            });
                        } else if(goods_price == 0){
                            //商品价格0  给用户出邮费领取的
                            Intent intent = new Intent(getActivity(),UserSiteMessageActivity.class);
                            intent.putExtra("goods_id",list.get(pos).getId());
                            startActivity(intent);
                        }else {
                            //金币不足
                            exchangePoint = goods_price;
                            exchange(list.get(pos).getId());
                        }
                    }
                });
            }
        });
        getGoods();
    }

    //获取兑换
    private void exchange(String id) {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("goods_id", id);
        YSBSdk.getService(OAuthService.class).exchange(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                point = point - exchangePoint;
                //money.setText(point + "");
                ShareDataManager.getInstance().save(getContext(), SharedPreferencesKey.KEY_points, point + "");
                dialogUtils.showCommentDialog(getString(R.string.exchange_success_2), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getGoods();
                        startActivity(new Intent(getContext(),MySwopActivity.class));
                        dialogUtils.dismissCommentDialog();
                    }
                });
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(getContext(), var1, message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    private void getGoods() {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "person");
        map.put("membership", "off");
        YSBSdk.getService(OAuthService.class).goods(map, new YRequestCallback<BaseBean<GoodsBean>>() {
            @Override
            public void onSuccess(BaseBean<GoodsBean> var1) {

                mStateView.showContent();
                list.clear();
                list.addAll(var1.getGoods());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(getContext(), var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
}
