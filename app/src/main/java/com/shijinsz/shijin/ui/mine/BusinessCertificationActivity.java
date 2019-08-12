package com.shijinsz.shijin.ui.mine;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.DataCleanManager;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.OssCallback;
import com.hongchuang.ysblibrary.model.model.bean.CertificationBean;
import com.hongchuang.ysblibrary.model.model.bean.MaterialBussiness;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.StatusBarUtil;

import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/10.
 */

public class BusinessCertificationActivity extends BaseActivity {
    @BindView(R.id.img_id)
    ImageView imgId;
    @BindView(R.id.rl_id)
    RelativeLayout rlId;
    @BindView(R.id.tv_nosend)
    TextView tvNosend;
    @BindView(R.id.ed_contact)
    EditText edContact;
    @BindView(R.id.img_contact_miss)
    ImageView imgContactMiss;
    @BindView(R.id.ed_tel)
    EditText edTel;
    @BindView(R.id.img_tel_miss)
    ImageView imgTelMiss;
    @BindView(R.id.ln_unsend)
    LinearLayout lnUnsend;
    @BindView(R.id.commit)
    Button commit;
    private File file;
    private String url="";
    private PopupWindow window;
    private CropOptions.Builder cropOptions;
    private Uri imageUri;
    private String id;

    @Override
    public int bindLayout() {
        return R.layout.business_certification_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        setTitle(getString(R.string.business_certification));
        id =ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID);
    }
    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        TextView yes = mailBoxLay.findViewById(R.id.yes);
        TextView no = mailBoxLay.findViewById(R.id.no);
        TextView content = mailBoxLay.findViewById(R.id.content);
        no.setText(getString(R.string.define));
        yes.setText(getString(R.string.go_on));
        content.setText(getString(R.string.certification_dialog));
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView)mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
        ((TextView)mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @OnClick({R.id.rl_id, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_id:
                showEarlyDialog();
                break;
            case R.id.commit:
                if (file==null){
                    lnUnsend.setVisibility(View.VISIBLE);
                    tvNosend.setVisibility(View.INVISIBLE);
                    imgContactMiss.setVisibility(View.GONE);
                    imgTelMiss.setVisibility(View.GONE);
                    return;
                }
                if (edContact.getText().toString().isEmpty()){
                    lnUnsend.setVisibility(View.INVISIBLE);
                    tvNosend.setVisibility(View.VISIBLE);
                    imgContactMiss.setVisibility(View.VISIBLE);
                    imgTelMiss.setVisibility(View.GONE);
                    return;
                }
                if (edTel.getText().toString().isEmpty()){
                    lnUnsend.setVisibility(View.INVISIBLE);
                    tvNosend.setVisibility(View.VISIBLE);
                    imgContactMiss.setVisibility(View.GONE);
                    imgTelMiss.setVisibility(View.VISIBLE);
                    return;
                }else {
                    lnUnsend.setVisibility(View.INVISIBLE);
                    tvNosend.setVisibility(View.INVISIBLE);
                    imgContactMiss.setVisibility(View.GONE);
                    imgTelMiss.setVisibility(View.GONE);
                }
                commit.setEnabled(false);
                getToken();
                break;
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 1:
                    file=new File(img);
                    GlideApp.with(mContext).load(img).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgId);
                    break;

            }
        }
    };
    private String img;
    private int type = 1;
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        img = result.getImage().getCompressPath();
        Message message =new Message();
        message.arg1=type;
        handler.sendMessage(message);
        File f =new File(img);
        Log.e(TAG, "takeSuccess: "+f.length());
        Log.e(TAG, "takeSuccess: "+file.length());
    }
    public void showEarlyDialog() {
        View popupView = mInflater.inflate(R.layout.choice_pic_pop, null);
        if (window==null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT , true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView camera=popupView.findViewById(R.id.camera);
        TextView choice=popupView.findViewById(R.id.choice);
        TextView cancel=popupView.findViewById(R.id.cancel);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();

        cropOptions=new CropOptions.Builder();
        file = new File(Environment.getExternalStorageDirectory(), "/temp/id_yyzz.jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri= Uri.fromFile(file);
        cropOptions.setWithOwnCrop(false).setAspectX(436).setAspectY(328);
        LubanOptions option = new LubanOptions.Builder().setMaxSize(400000).create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);
        getTakePhoto().onEnableCompress(config, true);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromCaptureWithCrop(imageUri,cropOptions.create());
                window.dismiss();
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromGalleryWithCrop(imageUri,cropOptions.create());
                window.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);

            }
        });
    }

    public void getToken(){
        mStateView.showLoading();
        String time= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_Expiration)+"";
        if (!time.isEmpty()){
            time = time.replace("T"," ");
            time = time.replace("Z","");
            Long expiration= TimeUtil.parseMillsTime(time,"yyyy-MM-dd HH:mm:ss");
            if (expiration+(3600*8-60)*1000>System.currentTimeMillis()){
                uploadImg();
                return;
            }
        }
        Map map = new HashMap();
        map.put("mode","mobile");
        YSBSdk.getService(OAuthService.class).stst(map, new YRequestCallback<StsBean>() {
            @Override
            public void onSuccess(StsBean var1) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_Expiration,var1.getExpiration());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_AccessKeyId,var1.getAccessKeyId());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_AccessKeySecret,var1.getAccessKeySecret());
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_SecurityToken,var1.getSecurityToken());
                uploadImg();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                commit.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                commit.setEnabled(true);
            }
        });
    }
    String imgurl;
    private void uploadImg() {

        File f;
        imgurl = "auth/img_"+System.currentTimeMillis()+"_"+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+".jpg";
        f=file;
        YSBSdk.getService(OAuthService.class).upload_oss(getApplicationContext(), imgurl, f.getAbsolutePath(), new OssCallback() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    url = "http://prod-static.shijinsz.net/" + imgurl;
                    upload();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ToastUtil.showToast("图片上传失败，请重新上传");
                mStateView.showContent();
                commit.setEnabled(true);
            }
        });
    }

    public void upload(){
        MaterialBussiness bean = new MaterialBussiness(url,edContact.getText().toString(),edTel.getText().toString());
        CertificationBean map =new CertificationBean("business",bean);
        YSBSdk.getService(OAuthService.class).promotion(id,map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("提交成功");
                DataCleanManager.deleteFile(file);
                commit.setEnabled(true);
                finish();
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                commit.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                commit.setEnabled(true);
            }
        });

    }

}
