package com.shijinsz.shijin.ui.user;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.view.VerificationCodeInput;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.ToKenUtil;
import retrofit.callback.YRequestCallback;

/**
 * Created by Administrator on 2018/7/26.
 */

public class SendMsgActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ed_code)
    VerificationCodeInput edCode;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.bt_get_code)
    Button btGetCode;
    private CountDownTimer timer;
    private String phone;
    private String type="1";

    @Override
    public int bindLayout() {
        return R.layout.sendmsg_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        phone = getIntent().getStringExtra("phone");
        type = getIntent().getStringExtra("type");
        tvPhone.setText(phone);
        timer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long l) {
                tvTimer.setText(l/1000+"s");
            }

            @Override
            public void onFinish() {
                btGetCode.setVisibility(View.VISIBLE);
                tvTimer.setVisibility(View.GONE);
//                edCode.setEnabled(false);
            }
        };
        edCode.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String s) {
                cellphone_code_comparison(s);
            }
        });
        btGetCode.setVisibility(View.GONE);
        tvTimer.setVisibility(View.VISIBLE);
        timer.start();
//        sendCellPhone();

    }

    private void cellphone_code_comparison(String s) {
        Map map = new HashMap();
        map.put("cellphone_code",s);
        YSBSdk.getService(OAuthService.class).cellphone_code_comparison(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                Intent intent;
                if (!type.equals("2")){
                    intent = new Intent(mContext, SetPasswordActivity.class);
                    intent.putExtra("type",type);
                }else {
                    intent = new Intent(mContext, SetNewPasswordActivity.class);
                }
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                edCode.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                ToastUtil.showToast(var1.getMessage());
                edCode.setEnabled(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @OnClick({R.id.back, R.id.bt_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                showDialog();
                break;
            case R.id.bt_get_code:
                sendCellPhone();
                break;
        }
    }

    private void sendCellPhone() {
        Map map = new HashMap();
        map.put("cellphone",phone);
        YSBSdk.getService(OAuthService.class).cellphone_code(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

                btGetCode.setVisibility(View.GONE);
                tvTimer.setVisibility(View.VISIBLE);
                timer.start();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                btGetCode.setVisibility(View.VISIBLE);
                tvTimer.setVisibility(View.GONE);
            }

            @Override
            public void onException(Throwable var1) {
                try{
                    ToastUtil.showToast(var1.getMessage());
                    btGetCode.setVisibility(View.VISIBLE);
                    tvTimer.setVisibility(View.GONE);
                }catch (Exception e){

                }

            }
        });
    }

    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.normal_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToKenUtil.deleteToken();
                finish();
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            showDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
