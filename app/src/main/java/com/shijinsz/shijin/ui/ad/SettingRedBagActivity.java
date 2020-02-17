package com.shijinsz.shijin.ui.ad;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.CashierInputFilter;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/20.
 */

public class SettingRedBagActivity extends BaseActivity {
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.tv_unput)
    TextView tvUnput;
    @BindView(R.id.et_red_num)
    EditText etRedNum;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.ed_question)
    EditText edQuestion;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.ed_answer1)
    EditText edAnswer1;
    @BindView(R.id.img_answer1)
    ImageView imgAnswer1;
    @BindView(R.id.ln_answer1)
    LinearLayout lnAnswer1;
    @BindView(R.id.ed_answer2)
    EditText edAnswer2;
    @BindView(R.id.img_answer2)
    ImageView imgAnswer2;
    @BindView(R.id.ln_answer2)
    LinearLayout lnAnswer2;
    @BindView(R.id.ed_answer3)
    EditText edAnswer3;
    @BindView(R.id.img_answer3)
    ImageView imgAnswer3;
    @BindView(R.id.ln_answer3)
    LinearLayout lnAnswer3;
    @BindView(R.id.ed_answer4)
    EditText edAnswer4;
    @BindView(R.id.img_answer4)
    ImageView imgAnswer4;
    @BindView(R.id.ln_answer4)
    LinearLayout lnAnswer4;
    @BindView(R.id.ed_url)
    EditText edUrl;
    @BindView(R.id.tv_preview)
    TextView tvPreview;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.shui)
    TextView shui;
    private int right = 0;
    private String id;
    private boolean isClick = false;
    private int shuinum=1;

    @Override
    public int bindLayout() {
        setSteepStatusBar(false);
        StatusBarUtil.setStatusBar(mActivity, true, false);
        return R.layout.setting_redbag_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton();
        setTitle(getString(R.string.setting_redbag));
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent).equals("")){
            shui.setVisibility(View.GONE);
        }else {
            if (Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent))>0){
                shui.setVisibility(View.VISIBLE);
                shuinum=Integer.parseInt(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_service_charge_percent));
                shui.setText(String.format(getString(R.string.shui),0+"",shuinum+"%",0+""));
            }else {
                shui.setVisibility(View.GONE);
            }
        }
        id = getIntent().getStringExtra("id");
        InputFilter[] filters = {new CashierInputFilter()};
        edMoney.setFilters(filters);
        edMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edMoney.getText().toString().isEmpty()){
                    shui.setText(String.format(getString(R.string.shui),0+"",shuinum+"%",0+""));
                }else {
                    float num=Float.valueOf(edMoney.getText().toString())*(100-shuinum)/100;
                    DecimalFormat fnum  =   new  DecimalFormat("##0.00");
                    String   dd=fnum.format(num);
                    shui.setText(String.format(getString(R.string.shui),edMoney.getText().toString(),shuinum+"%",dd+""));
                }
                if (isClick) {
                    topView.setVisibility(View.VISIBLE);
                    tvUnput.setVisibility(View.GONE);
                    if (isEnoughtM()) {
                        tvPreview.setEnabled(true);
                        publish.setEnabled(true);
                    } else {
                        tvPreview.setEnabled(false);
                        publish.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etRedNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isClick) {
                    topView.setVisibility(View.VISIBLE);
                    tvUnput.setVisibility(View.GONE);
                    if (isEnoughtM()) {
                        tvPreview.setEnabled(true);
                        publish.setEnabled(true);
                    } else {
                        tvPreview.setEnabled(false);
                        publish.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvQuestion.setText(edQuestion.getText().toString().length() + "/35");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isop1 = false, isop2 = false, isop3 = false, isop4 = false;

    @OnClick({R.id.ln_answer1, R.id.ln_answer2, R.id.ln_answer3, R.id.ln_answer4, R.id.tv_preview, R.id.publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_answer1:
                if (isop1) {
                    isop1 = false;
                    imgAnswer1.setImageResource(R.mipmap.icon_selected_3);
                } else {
                    isop1 = true;
                    imgAnswer1.setImageResource(R.mipmap.icon_selected_4);
                    isop2 = false;
                    imgAnswer2.setImageResource(R.mipmap.icon_selected_3);
                    isop3 = false;
                    imgAnswer3.setImageResource(R.mipmap.icon_selected_3);
                    isop4 = false;
                    imgAnswer4.setImageResource(R.mipmap.icon_selected_3);
                }
                break;
            case R.id.ln_answer2:
                if (isop2) {
                    isop2 = false;
                    imgAnswer2.setImageResource(R.mipmap.icon_selected_3);
                } else {
                    isop2 = true;
                    imgAnswer2.setImageResource(R.mipmap.icon_selected_4);
                    isop1 = false;
                    imgAnswer1.setImageResource(R.mipmap.icon_selected_3);
                    isop3 = false;
                    imgAnswer3.setImageResource(R.mipmap.icon_selected_3);
                    isop4 = false;
                    imgAnswer4.setImageResource(R.mipmap.icon_selected_3);
                }
                break;
            case R.id.ln_answer3:
                if (isop3) {
                    isop3 = false;
                    imgAnswer3.setImageResource(R.mipmap.icon_selected_3);
                } else {
                    isop3 = true;
                    imgAnswer3.setImageResource(R.mipmap.icon_selected_4);
                    isop2 = false;
                    imgAnswer2.setImageResource(R.mipmap.icon_selected_3);
                    isop1 = false;
                    imgAnswer1.setImageResource(R.mipmap.icon_selected_3);
                    isop4 = false;
                    imgAnswer4.setImageResource(R.mipmap.icon_selected_3);
                }
                break;
            case R.id.ln_answer4:
                if (isop4) {
                    isop4 = false;
                    imgAnswer4.setImageResource(R.mipmap.icon_selected_3);
                } else {
                    isop4 = true;
                    imgAnswer4.setImageResource(R.mipmap.icon_selected_4);
                    isop2 = false;
                    imgAnswer2.setImageResource(R.mipmap.icon_selected_3);
                    isop3 = false;
                    imgAnswer3.setImageResource(R.mipmap.icon_selected_3);
                    isop1 = false;
                    imgAnswer1.setImageResource(R.mipmap.icon_selected_3);
                }
                break;
            case R.id.tv_preview:
                if (isEmpty()) {
                    return;
                }
                if (edAnswer1.getText().toString().equals(edAnswer2.getText().toString())||
                        edAnswer1.getText().toString().equals(edAnswer3.getText().toString())||
                        edAnswer1.getText().toString().equals(edAnswer4.getText().toString())||
                        edAnswer2.getText().toString().equals(edAnswer3.getText().toString())||
                        edAnswer2.getText().toString().equals(edAnswer4.getText().toString())||
                        edAnswer3.getText().toString().equals(edAnswer4.getText().toString())){
                    ToastUtil.showToast("不能有重复的选项");
                    return;
                }
                updata(1);

                break;
            case R.id.publish:
                if (isEmpty()) {
                    return;
                }
                if (edAnswer1.getText().toString().equals(edAnswer2.getText().toString())||
                        edAnswer1.getText().toString().equals(edAnswer3.getText().toString())||
                        edAnswer1.getText().toString().equals(edAnswer4.getText().toString())||
                        edAnswer2.getText().toString().equals(edAnswer3.getText().toString())||
                        edAnswer2.getText().toString().equals(edAnswer4.getText().toString())||
                        edAnswer3.getText().toString().equals(edAnswer4.getText().toString())){
                    ToastUtil.showToast("不能有重复的选项");
                    return;
                }
                updata(2);

                break;
        }
    }

    private void updata(int i) {
        mStateView.showLoading();
        Map map = new HashMap();
        Map optionsMap = new HashMap();
        Map answerMap = new HashMap();
        optionsMap.put("option1", edAnswer1.getText().toString());
        optionsMap.put("option2", edAnswer2.getText().toString());
        optionsMap.put("option3", edAnswer3.getText().toString());
        optionsMap.put("option4", edAnswer4.getText().toString());
        if (isop1) {
            answerMap.put("option1", edAnswer1.getText().toString());
        }
        if (isop2) {
            answerMap.put("option2", edAnswer2.getText().toString());
        }
        if (isop3) {
            answerMap.put("option3", edAnswer3.getText().toString());
        }
        if (isop4) {
            answerMap.put("option4", edAnswer4.getText().toString());
        }
        map.put("mode", "answer");
        map.put("question", edQuestion.getText().toString());
        map.put("options", optionsMap);
        map.put("answer", answerMap);
        map.put("url", edUrl.getText().toString());
        map.put("total_number", edMoney.getText().toString());
        map.put("people_number", etRedNum.getText().toString());
        map.put("reward_mode", "change");
        YSBSdk.getService(OAuthService.class).updata_ads(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                if (i == 1) {
                    mStateView.showContent();
                    Intent intent = new Intent(mContext, PreviewActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("money", edMoney.getText().toString());
                    intent.putExtra("num", etRedNum.getText().toString());
                    startActivity(intent);
                } else {
                    releases();
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast("上传失败，请重新上传");
                mStateView.showContent();
            }
        });
    }

    private void releases() {
        Map map = new HashMap<>();
        YSBSdk.getService(OAuthService.class).releases(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                Intent intent2 = new Intent(mContext, PayActivity.class);
                intent2.putExtra("id", var1.getNew_ad_id());
                intent2.putExtra("money", edMoney.getText().toString());
                intent2.putExtra("num", etRedNum.getText().toString());
                startActivity(intent2);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext, var1, message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast("发布失败，请重新上传");
                mStateView.showContent();
            }
        });
    }

    private boolean isEmpty() {
        if (etRedNum.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setnum));
            return true;
        }
        if (edMoney.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setmoney));
            return true;
        }
        if (edQuestion.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setquestion));
            return true;
        }
        if (edAnswer1.getText().toString().isEmpty() || edAnswer2.getText().toString().isEmpty()
                || edAnswer3.getText().toString().isEmpty() || edAnswer4.getText().toString().isEmpty()) {
            ToastUtil.showToast(getString(R.string.plz_setanswer));
            return true;
        }
        if (!isop1 && !isop2 && !isop3 && !isop4) {
            ToastUtil.showToast(getString(R.string.plz_choice_answer));
            return true;
        }
        int num = Integer.parseInt(etRedNum.getText().toString());
        float money = Float.valueOf(edMoney.getText().toString());
        if (money < 1) {
            tvUnput.setText(getString(R.string.more_than_1yuan));
            tvUnput.setVisibility(View.VISIBLE);
            topView.setVisibility(View.GONE);
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            scrollView.smoothScrollTo(0, 0);
            isClick = true;
            return true;
        }
        if (money / num < 0.1) {
            tvUnput.setText(getString(R.string.more_than_01));
            tvUnput.setVisibility(View.VISIBLE);
            topView.setVisibility(View.GONE);
            scrollView.smoothScrollTo(0, 0);
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClick = true;
            return true;
        }
        return false;
    }

    private boolean isEnoughtM() {
        if (edMoney.getText().toString().isEmpty() || etRedNum.getText().toString().isEmpty()) {
            return false;
        }
        int num = Integer.parseInt(etRedNum.getText().toString());
        float money = Float.valueOf(edMoney.getText().toString());
        if (money < 1) {
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            return false;
        }
        if (money / num < 0.1) {
            tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
            edMoney.setTextColor(getResources().getColor(R.color.colorPrimary));
            etRedNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            return false;
        }
        tv1.setTextColor(getResources().getColor(R.color.text_33));
        tv2.setTextColor(getResources().getColor(R.color.text_33));
        tv3.setTextColor(getResources().getColor(R.color.text_33));
        tv4.setTextColor(getResources().getColor(R.color.text_33));
        edMoney.setTextColor(getResources().getColor(R.color.text_33));
        etRedNum.setTextColor(getResources().getColor(R.color.text_33));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
