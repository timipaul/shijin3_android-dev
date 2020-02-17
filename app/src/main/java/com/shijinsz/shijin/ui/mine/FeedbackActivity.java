package com.shijinsz.shijin.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.utils.Validator;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/21.
 */

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.img_suggest)
    ImageView imgSuggest;
    @BindView(R.id.ln_take_suggest)
    LinearLayout lnTakeSuggest;
    @BindView(R.id.img_have_error)
    ImageView imgHaveError;
    @BindView(R.id.ln_have_error)
    LinearLayout lnHaveError;
    @BindView(R.id.img_no_understand)
    ImageView imgNoUnderstand;
    @BindView(R.id.ln_no_understand)
    LinearLayout lnNoUnderstand;
    @BindView(R.id.img_bad_use)
    ImageView imgBadUse;
    @BindView(R.id.ln_bad_use)
    LinearLayout lnBadUse;
    @BindView(R.id.img_other)
    ImageView imgOther;
    @BindView(R.id.ln_other)
    LinearLayout lnOther;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.ed_detail_question)
    EditText edDetailQuestion;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.tv_qq_group)
    TextView tvQqGroup;
    @BindView(R.id.tv_wechat_num)
    TextView tvWechatNum;
    private String qq="785508895",wechat="shijinsz1";
    @Override
    public int bindLayout() {
        return R.layout.feedback_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.feedback));
        type="suggestion";
        tvQqGroup.setText(String.format(getString(R.string.qq_group),qq));
        tvWechatNum.setText(String.format(getString(R.string.wechat_num),wechat));
        edDetailQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvTotal.setText(edDetailQuestion.getText().toString().length()+"/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edDetailQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((view.getId() == R.id.ed_detail_question && canVerticalScroll(edDetailQuestion))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }
    /**
     * EditText竖直方向能否够滚动
     * @param editText  须要推断的EditText
     * @return  true：能够滚动   false：不能够滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
    private String type="";
    @OnClick({R.id.ln_take_suggest, R.id.ln_have_error, R.id.ln_no_understand, R.id.ln_bad_use, R.id.ln_other, R.id.commit, R.id.tv_qq_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ln_take_suggest:
                type="suggestion";
                imgSuggest.setImageResource(R.mipmap.radio_button_on);
                imgHaveError.setImageResource(R.mipmap.radio_button_off);
                imgNoUnderstand.setImageResource(R.mipmap.radio_button_off);
                imgBadUse.setImageResource(R.mipmap.radio_button_off);
                imgOther.setImageResource(R.mipmap.radio_button_off);
                break;
            case R.id.ln_have_error:
                type="error";
                imgSuggest.setImageResource(R.mipmap.radio_button_off);
                imgHaveError.setImageResource(R.mipmap.radio_button_on);
                imgNoUnderstand.setImageResource(R.mipmap.radio_button_off);
                imgBadUse.setImageResource(R.mipmap.radio_button_off);
                imgOther.setImageResource(R.mipmap.radio_button_off);
                break;
            case R.id.ln_no_understand:
                type="not know operation";
                imgSuggest.setImageResource(R.mipmap.radio_button_off);
                imgHaveError.setImageResource(R.mipmap.radio_button_off);
                imgNoUnderstand.setImageResource(R.mipmap.radio_button_on);
                imgBadUse.setImageResource(R.mipmap.radio_button_off);
                imgOther.setImageResource(R.mipmap.radio_button_off);
                break;
            case R.id.ln_bad_use:
                type="not good use";
                imgSuggest.setImageResource(R.mipmap.radio_button_off);
                imgHaveError.setImageResource(R.mipmap.radio_button_off);
                imgNoUnderstand.setImageResource(R.mipmap.radio_button_off);
                imgBadUse.setImageResource(R.mipmap.radio_button_on);
                imgOther.setImageResource(R.mipmap.radio_button_off);
                break;
            case R.id.ln_other:
                type="other";
                imgSuggest.setImageResource(R.mipmap.radio_button_off);
                imgHaveError.setImageResource(R.mipmap.radio_button_off);
                imgNoUnderstand.setImageResource(R.mipmap.radio_button_off);
                imgBadUse.setImageResource(R.mipmap.radio_button_off);
                imgOther.setImageResource(R.mipmap.radio_button_on);
                break;
            case R.id.commit:
                if (edDetailQuestion.getText().toString().isEmpty()){
                    ToastUtil.showToast(getString(R.string.plz_edit_suggest));
                    return;
                }
                if (edPhone.getText().toString().isEmpty()){
                    ToastUtil.showToast(getString(R.string.plz_edit_phone));
                    return;
                }
                if (!Validator.isEmail(edPhone.getText().toString())) {
                    if (!Validator.isInteger(edPhone.getText().toString())){
                        ToastUtil.showToast("请输入正确的联系方式");
                        return;
                    }
                }
                feedback();
                break;
            case R.id.tv_qq_group:
                joinQQGroup(getString(R.string.QQ_GROUP_KEY));
                break;
        }
    }
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private void feedback() {
        Map map=new HashMap();
        map.put("feedback_type",type);
        map.put("feedback_content",edDetailQuestion.getText().toString());
        map.put("contact_way",edPhone.getText().toString());
        map.put("mode","person");
        YSBSdk.getService(OAuthService.class).feedbacks(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                ToastUtil.showToast(getString(R.string.commit_success));
                finish();
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
}
