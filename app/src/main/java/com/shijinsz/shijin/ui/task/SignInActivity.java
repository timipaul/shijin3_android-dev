package com.shijinsz.shijin.ui.task;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.BaseBean;
import com.hongchuang.ysblibrary.model.model.bean.LotteryListBean;
import com.hongchuang.ysblibrary.model.model.bean.ShareBean;
import com.hongchuang.ysblibrary.model.model.bean.SignBean;
import com.hongchuang.ysblibrary.model.model.bean.SignInsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.LotteryView;
import com.shijinsz.shijin.utils.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/31.
 */

public class SignInActivity extends BaseActivity {
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img2)
    LotteryView img2;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.one_day)
    TextView oneDay;
    @BindView(R.id.two_day)
    TextView twoDay;
    @BindView(R.id.three_day)
    TextView threeDay;
    @BindView(R.id.four_day)
    TextView fourDay;
    @BindView(R.id.five_day)
    TextView fiveDay;
    @BindView(R.id.six_day)
    TextView sixDay;
    @BindView(R.id.seven_day)
    TextView sevenDay;
    @BindView(R.id.one_view)
    View oneView;
    @BindView(R.id.two_view)
    View twoView;
    @BindView(R.id.three_view)
    View threeView;
    @BindView(R.id.four_view)
    View fourView;
    @BindView(R.id.five_view)
    View fiveView;
    @BindView(R.id.six_view)
    View sixView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.sign)
    LinearLayout sign;
    private List<Bitmap> list = new ArrayList<>();
    private boolean isStar = true;
    private int y;
    private DialogUtils dialogUtils;

    public int bindLayout() {
        return R.layout.sign_in_activity;
    }

    private int free = 0;
    private int point = 0;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        y = sign.getBottom();
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(R.string.signin);
        String poin = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_points);
        if (!poin.isEmpty()) {
            point = Integer.parseInt(poin);
        }
        String str = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draw_lottery_number);
        if (!str.isEmpty()) {
            free = Integer.parseInt(str);
        }
        str = String.format(getString(R.string.free_lottery), free + "");
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new AbsoluteSizeSpan(21, true), 6, 6+(free+"").length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        tvFree.setText(style);
        dialogUtils = new DialogUtils(mContext);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        img2.setOnClickListen(new LotteryView.onClickListen() {
            @Override
            public void onClick() {
                if (!isStar) {
                    isStar = true;
                    if (free<=0&&point<50){
                        ToastUtil.showToast("金币不足");
                        isStar = false;
                        return;
                    }
                    getLotter();
                }
            }

            @Override
            public void onEnd(int i) {
                isStar = false;
                int i1 = i;
                if (i == 7) i1 = 3;
                if (i == 3) i1 = 4;
                if (i == 6) i1 = 5;
                if (i == 4) i1 = 7;
                if (i == 5) i1 = 6;

                if (bean.getLottery_mode().equals("change")) {
                    dialogUtils.showLotteryDialog(bean.getLottery_big_imgurl(), bean.getLottery_name(), 2);
                }else if (bean.getLottery_mode().equals("point")) {
                    try{
                        point=point+Integer.parseInt(bean.getLottery_value());
                    }catch (Exception e){

                    }

                    dialogUtils.showLotteryDialog(bean.getLottery_big_imgurl(), bean.getLottery_name(), 3);
                }else {
                    if (bean.getLottery_code().equals("patch_card")){
                        signBean.setAble_patch_number(Integer.parseInt(signBean.getAble_patch_number())+1+"");
                    }
                    dialogUtils.showLotteryDialog(bean.getLottery_big_imgurl(), bean.getLottery_name(), 1);
                }
            }
        });
        mStateView.showLoading();
        getSign();
        getLotterList();
    }

    //签到
    public void sign() {
        Map map = new HashMap();
        map.put("sign_in_id", signBean.getSign_in().getId());
        YSBSdk.getService(OAuthService.class).sign(map, new YRequestCallback<SignInsBean>() {
            @Override
            public void onSuccess(SignInsBean var1) {
                mStateView.showContent();
                if (!var1.getCycle_continuous_number().equals("7")) {
                    dialogUtils.showSignDialog(var1.getReward_points());
                } else {
                    dialogUtils.showSevenDialog(signBean.getSmall_lottery().getLottery_imgurl(), signBean.getSmall_lottery().getLottery_name(), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogUtils.dismissSevenDialog();
                            getShare();
                        }
                    });
                }
                String str=String.format(getString(R.string.day), var1.getContinuous_number());
                SpannableStringBuilder style=new SpannableStringBuilder(str);
                style.setSpan(new AbsoluteSizeSpan(30, true), 0,  var1.getContinuous_number().length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                tvDay.setText(style);
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draw_lottery_number,var1.getDraw_lottery_number());
                str = String.format(getString(R.string.free_lottery), var1.getDraw_lottery_number() + "");
                SpannableStringBuilder style1 = new SpannableStringBuilder(str);
                style1.setSpan(new AbsoluteSizeSpan(21, true), 6, 6+var1.getDraw_lottery_number().length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                tvFree.setText(style1);
                String str2 = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draw_lottery_number);
                if (!str2.isEmpty()) {
                    free = Integer.parseInt(var1.getDraw_lottery_number());
                }
                getSign();
//                for (int i1 = 0; i1 < signBean.getSign_in().getMarks().size(); i1++) {
//                    if (signBean.getSign_in().getMarks().get(i1).getStatus().equals("off")) {
//                        signBean.getSign_in().getMarks().get(i1).setStatus("on");
//                        changeView(i1, signBean);
//                        return;
//                    }
//                }

            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }

    //补签
    public void addsign(int id) {
        Map map = new HashMap();
        map.put("sign_in_id", signBean.getSign_in().getId());
        map.put("mode", "patch");
        map.put("mark_id", id);
        YSBSdk.getService(OAuthService.class).addsign(map, new YRequestCallback<SignInsBean>() {
            @Override
            public void onSuccess(SignInsBean var1) {
                num--;
                if (!var1.getCycle_continuous_number().equals("7")) {
                    dialogUtils.showSignDialog(var1.getReward_points());
                } else {
                    dialogUtils.showSevenDialog(signBean.getSmall_lottery().getLottery_imgurl(), signBean.getSmall_lottery().getLottery_name(), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogUtils.dismissSevenDialog();
                            getShare();
                        }
                    });
                }
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draw_lottery_number,var1.getDraw_lottery_number());

                String str=String.format(getString(R.string.day), var1.getContinuous_number());
                SpannableStringBuilder style=new SpannableStringBuilder(str);
                style.setSpan(new AbsoluteSizeSpan(30, true), 0,  var1.getContinuous_number().length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                tvDay.setText(style);
                str = String.format(getString(R.string.free_lottery), var1.getDraw_lottery_number() + "");
                SpannableStringBuilder style1 = new SpannableStringBuilder(str);
                style1.setSpan(new AbsoluteSizeSpan(21, true), 6, 6+var1.getDraw_lottery_number().length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                tvFree.setText(style1);
                String str2 = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draw_lottery_number);
                if (!str2.isEmpty()) {
                    free = Integer.parseInt(str2);
                }
                tvDay.setText(String.format(getString(R.string.day), var1.getContinuous_number()));
//                signBean.getSign_in().getMarks().get(id).setStatus("on");
                getSign();
//                changeView(id, signBean);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }
    //获取分享信息
    public void getShare(){
        mStateView.showLoading();
        String mode ="sign_in_small_lottery";
        YSBSdk.getService(OAuthService.class).share_infos(mode, new YRequestCallback<ShareBean>() {
            @Override
            public void onSuccess(ShareBean var1) {
                mStateView.showContent();
                new ShareDialog(mActivity).showWithdrapDialog(mActivity,5,var1.getShare_title(),var1.getShare_info(),var1.getShare_pic(), "https://prod.shijinsz.net/app_h5/task");
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    private void changeView(int i1, SignBean var1) {

        System.out.println("签到进度: " + i1);
        System.out.println("签到状态:" + var1.getSign_in().getMarks().get(i1).getStatus());

        switch (i1) {
            case 0:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    oneDay.setEnabled(false);
                    oneDay.setText("");
                    oneDay.setBackgroundResource(R.mipmap.issign);
                    oneView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    oneDay.setEnabled(true);
                    oneDay.setText(getString(R.string.add));
                    oneDay.setBackgroundResource(R.mipmap.dianzan_1);
                    oneView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    oneDay.setEnabled(false);
                    oneDay.setText("");
                    oneDay.setBackgroundResource(R.mipmap.nosign);
                    oneView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 1:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    twoDay.setEnabled(false);
                    twoDay.setText("");
                    twoDay.setBackgroundResource(R.mipmap.issign);
                    twoView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    twoDay.setEnabled(true);
                    twoDay.setText(getString(R.string.add));
                    twoDay.setBackgroundResource(R.mipmap.dianzan_1);
                    twoView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    twoDay.setEnabled(false);
                    twoDay.setText("");
                    twoDay.setBackgroundResource(R.mipmap.nosign);
                    twoView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 2:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    threeDay.setText("");
                    threeDay.setEnabled(false);
                    threeDay.setBackgroundResource(R.mipmap.issign);
                    threeView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    threeDay.setText(getString(R.string.add));
                    threeDay.setEnabled(true);
                    threeDay.setBackgroundResource(R.mipmap.dianzan_1);
                    threeView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    threeDay.setEnabled(false);
                    threeDay.setText("");
                    threeDay.setBackgroundResource(R.mipmap.nosign);
                    threeView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 3:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    fourDay.setText("");
                    fourDay.setEnabled(false);
                    fourDay.setBackgroundResource(R.mipmap.issign);
                    fourView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    fourDay.setText(getString(R.string.add));
                    fourDay.setEnabled(true);
                    fourDay.setBackgroundResource(R.mipmap.dianzan_1);
                    fourView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    fourDay.setEnabled(false);
                    fourDay.setText("");
                    fourDay.setBackgroundResource(R.mipmap.nosign);
                    fourView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 4:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    fiveDay.setText("");
                    fiveDay.setEnabled(false);
                    fiveDay.setBackgroundResource(R.mipmap.issign);
                    fiveView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    fiveDay.setText(getString(R.string.add));
                    fiveDay.setEnabled(true);
                    fiveDay.setBackgroundResource(R.mipmap.dianzan_1);
                    fiveView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    fiveDay.setEnabled(false);
                    fiveDay.setText("");
                    fiveDay.setBackgroundResource(R.mipmap.nosign);
                    fiveView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 5:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    sixDay.setText("");
                    sixDay.setEnabled(false);
                    sixDay.setBackgroundResource(R.mipmap.issign);
                    sixView.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    sixDay.setText(getString(R.string.add));
                    sixDay.setEnabled(true);
                    sixDay.setBackgroundResource(R.mipmap.dianzan_1);
                    sixView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                } else {
                    sixDay.setEnabled(false);
                    sixDay.setText("");
                    sixDay.setBackgroundResource(R.mipmap.nosign);
                    sixView.setBackgroundColor(getResources().getColor(R.color.text_hint));
                }
                break;
            case 6:
                if (var1.getSign_in().getMarks().get(i1).getStatus().equals("on")) {
                    sevenDay.setText("");
                    sevenDay.setEnabled(false);
                    sevenDay.setBackgroundResource(R.mipmap.issign);
                } else if (var1.getSign_in().getMarks().get(i1).getStatus().equals("patch")) {
                    sevenDay.setText(getString(R.string.add));
                    sevenDay.setEnabled(true);
                    sevenDay.setBackgroundResource(R.mipmap.dianzan_1);
                } else {
                    sevenDay.setEnabled(false);
                    sevenDay.setText("");
                    sevenDay.setBackgroundResource(R.mipmap.nosign);
                }
                break;
        }
    }

    private SignBean signBean;

    public void getSign() {
        Map map = new HashMap();
        YSBSdk.getService(OAuthService.class).sign_ins(map, new YRequestCallback<SignBean>() {
            @Override
            public void onSuccess(SignBean var1) {
                signBean = var1;
                String str=String.format(getString(R.string.day), var1.getSign_in().getContinuous_number());
                SpannableStringBuilder style=new SpannableStringBuilder(str);
                style.setSpan(new AbsoluteSizeSpan(30, true), 0,  var1.getSign_in().getContinuous_number().length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                tvDay.setText(style);
//                tvDay.setText(String.format(getString(R.string.day), var1.getSign_in().getContinuous_number()));
                System.out.println("当前签到信息: " + var1.getSign_in().getMarks().size());
                for (int i1 = 0; i1 < var1.getSign_in().getMarks().size(); i1++) {
                    changeView(i1, signBean);
                }
                if (!var1.getSign_in().getLast_sign_in_time().isEmpty()) {
                    String time = TimeUtil.format(Long.valueOf(var1.getSign_in().getLast_sign_in_time()) * 1000, "yyyy-MM-dd HH:mm:ss");
                    if (!TimeUtil.isToday(time)) {
                        sign();
                    }else {
                        mStateView.showContent();
                    }
                }else {
                    mStateView.showContent();
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
            }
        });
    }
    private LotteryListBean bean;
    private void getLotter() {
        mStateView.showLoading();
        Map map = new HashMap();
        map.put("mode", "draw");
        YSBSdk.getService(OAuthService.class).lottery(map, new YRequestCallback<BaseBean<LotteryListBean>>() {
            @Override
            public void onSuccess(BaseBean<LotteryListBean> var1) {
                mStateView.showContent();
                bean=var1.getReward_lottery();
                for (int i1 = 0; i1 < lotteryList.size(); i1++) {
                    if (var1.getReward_lottery().getLotteryId().equals(lotteryList.get(i1).getLotteryId())) {
                        int j = i1;
                        if (i1 == 3) j = 7;
                        if (i1 == 4) j = 3;
                        if (i1 == 5) j = 6;
                        if (i1 == 7) j = 4;
                        if (i1 == 6) j = 5;
                        if (free > 0) {
                            free--;
                            String str = String.format(getString(R.string.free_lottery), free + "");
                            SpannableStringBuilder style = new SpannableStringBuilder(str);
                            style.setSpan(new AbsoluteSizeSpan(21, true), 6, 6+(free+"").length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                            tvFree.setText(style);
                            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draw_lottery_number, free + "");
                        } else {
                            if (point >= 50) {
                                point = point - 50;
                                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_points, point + "");
                            }
                        }
                        if (free > 0) {
                            img2.start(j + 36, 1);
                        } else {
                            img2.start(j + 36, 2);
                        }

                        return;
                    }
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                isStar = false;
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                isStar = false;
            }
        });
    }

    private List<LotteryListBean> lotteryList = new ArrayList<>();

    private void getLotterList() {
        Map map = new HashMap();
        map.put("mode", "person");
        YSBSdk.getService(OAuthService.class).lotterylist(map, new YRequestCallback<BaseBean<LotteryListBean>>() {
            @Override
            public void onSuccess(BaseBean<LotteryListBean> var1) {
                lotteryList.clear();
                tvTime.setText(String.format(getString(R.string.activity_time), TimeUtil.format(Long.valueOf(var1.getLottery_activity_time()) * 1000, "yyyy-MM-dd")));
                lotteryList.addAll(var1.getDraw_lottery_list());
                initImg(var1.getDraw_lottery_list());
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private int i = 0;

    public void initImg(List<LotteryListBean> bean) {
        Glide.with(mContext).load(bean.get(i).getLottery_imgurl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = null;
                BitmapDrawable bd = (BitmapDrawable) resource;
                bitmap = bd.getBitmap();
                list.add(i, bitmap);
                if (i == 7) {
                    isStar = false;
                    img2.setBitmap(list);
                } else {
                    i++;
                    initImg(bean);
                }
            }
        });

    }

    int num;

    public void showAddSign(int i) {
        if (signBean.getAble_patch_number() != null) {
            num = Integer.parseInt(signBean.getAble_patch_number());
            dialogUtils.showAddSignDialog(num, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (num > 0) {
                        addsign(i);
                        dialogUtils.dismissAddSignDialog();
                    } else {
                        scrollView.smoothScrollTo(0, y);
                        dialogUtils.dismissAddSignDialog();
                    }
                }
            });
        }
    }

    @OnClick({R.id.one_day, R.id.two_day, R.id.three_day, R.id.four_day, R.id.five_day, R.id.six_day, R.id.seven_day})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_day:
                showAddSign(0);
                break;
            case R.id.two_day:
                showAddSign(1);
                break;
            case R.id.three_day:
                showAddSign(2);
                break;
            case R.id.four_day:
                showAddSign(3);
                break;
            case R.id.five_day:
                showAddSign(4);
                break;
            case R.id.six_day:
                showAddSign(5);
                break;
            case R.id.seven_day:
                showAddSign(6);
                break;
        }
    }
}
