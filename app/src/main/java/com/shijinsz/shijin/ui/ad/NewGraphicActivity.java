package com.shijinsz.shijin.ui.ad;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.ProgressWebView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.OssCallback;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;

import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

/**
 * Created by yrdan on 2018/8/15.
 */

public class NewGraphicActivity extends BaseActivity {
    @BindView(R.id.img_small)
    ImageView imgSmall;
    @BindView(R.id.tv_small)
    TextView tvSmall;
    @BindView(R.id.ln_small)
    LinearLayout lnSmall;
    @BindView(R.id.img_big)
    ImageView imgBig;
    @BindView(R.id.tv_big)
    TextView tvBig;
    @BindView(R.id.ln_big)
    LinearLayout lnBig;
    @BindView(R.id.img_three)
    ImageView imgThree;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.ln_three)
    LinearLayout lnThree;
    @BindView(R.id.ln1)
    LinearLayout ln1;
    @BindView(R.id.img_big_pic)
    ImageView imgBigPic;
    @BindView(R.id.rl_big_pic)
    RelativeLayout rlBigPic;
    @BindView(R.id.img_pic1)
    ImageView imgPic1;
    @BindView(R.id.rl_pic1)
    RelativeLayout rlPic1;
    @BindView(R.id.img_pic2)
    ImageView imgPic2;
    @BindView(R.id.rl_pic2)
    RelativeLayout rlPic2;
    @BindView(R.id.img_pic3)
    ImageView imgPic3;
    @BindView(R.id.rl_pic3)
    RelativeLayout rlPic3;
    @BindView(R.id.ln_three_pic)
    LinearLayout lnThreePic;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.tv_title_num)
    TextView tvTitleNum;
    @BindView(R.id.rl_input)
    RelativeLayout rlInput;
    @BindView(R.id.webview)
    ProgressWebView webview;
    @BindView(R.id.ln_web)
    LinearLayout lnWeb;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private CropOptions.Builder cropOptions;
    private File file;
    private String id="";
    private Ads ads;

    @Override
    public int bindLayout() {
        return R.layout.new_graphic_activity;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                needPop();
            }
        });
        setTitle(getString(R.string.new_graphic));
        edTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvTitleNum.setText(edTitle.getText().toString().length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        id=getIntent().getStringExtra("id");
        if (id==null||id.isEmpty()){
            if (!ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_admode).isEmpty()){
                ad_mode=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_admode);
                switch (ad_mode){
                    case "one_small_pic":
                        imgSmall.setImageResource(R.mipmap.radio_button_on);
                        tvSmall.setTextColor(getResources().getColor(R.color.text_33));
                        imgBig.setImageResource(R.mipmap.radio_button_off);
                        tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                        imgThree.setImageResource(R.mipmap.radio_button_off);
                        tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                        lnThreePic.setVisibility(View.VISIBLE);
                        rlBigPic.setVisibility(View.GONE);
                        rlPic2.setVisibility(View.INVISIBLE);
                        rlPic3.setVisibility(View.INVISIBLE);
                        break;
                    case "one_big_pic":
                        imgBig.setImageResource(R.mipmap.radio_button_on);
                        tvBig.setTextColor(getResources().getColor(R.color.text_33));
                        imgSmall.setImageResource(R.mipmap.radio_button_off);
                        tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                        imgThree.setImageResource(R.mipmap.radio_button_off);
                        tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                        lnThreePic.setVisibility(View.GONE);
                        rlBigPic.setVisibility(View.VISIBLE);
                        rlPic2.setVisibility(View.INVISIBLE);
                        rlPic3.setVisibility(View.INVISIBLE);
                        break;
                    case "three_pics":
                        imgThree.setImageResource(R.mipmap.radio_button_on);
                        tvThree.setTextColor(getResources().getColor(R.color.text_33));
                        imgBig.setImageResource(R.mipmap.radio_button_off);
                        tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                        imgSmall.setImageResource(R.mipmap.radio_button_off);
                        tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                        lnThreePic.setVisibility(View.VISIBLE);
                        rlBigPic.setVisibility(View.GONE);
                        rlPic2.setVisibility(View.VISIBLE);
                        rlPic3.setVisibility(View.VISIBLE);
                        break;
                }
            }
            edTitle.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_title));
            html= ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_content);
            if (!html.isEmpty()) {
                lnWeb.setVisibility(View.VISIBLE);
                rlInput.setVisibility(View.GONE);
                webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            }
            smallurl1=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_small1);
            smallurl2=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_small2);
            smallurl3=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_small3);
            bigurl=ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_big);
            if (!smallurl1.isEmpty()){
                GlideApp.with(mContext).load(smallurl1).into(imgPic1);
            }
            if (!smallurl2.isEmpty()){
                GlideApp.with(mContext).load(smallurl2).into(imgPic2);
            }
            if (!smallurl3.isEmpty()){
                GlideApp.with(mContext).load(smallurl3).into(imgPic3);
            }
            if (!bigurl.isEmpty()){
                GlideApp.with(mContext).load(bigurl).into(imgBigPic);
            }
        }else {
            ads=(Ads)getIntent().getSerializableExtra("ads");
            ad_mode=ads.getAd_mode();
            switch (ad_mode){
                case "one_small_pic":
                    imgSmall.setImageResource(R.mipmap.radio_button_on);
                    tvSmall.setTextColor(getResources().getColor(R.color.text_33));
                    imgBig.setImageResource(R.mipmap.radio_button_off);
                    tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                    imgThree.setImageResource(R.mipmap.radio_button_off);
                    tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                    lnThreePic.setVisibility(View.VISIBLE);
                    rlBigPic.setVisibility(View.GONE);
                    rlPic2.setVisibility(View.INVISIBLE);
                    rlPic3.setVisibility(View.INVISIBLE);
                    smallurl1=ads.getAd_title_pics().get(0);
                    GlideApp.with(mContext).load(smallurl1).into(imgPic1);
                    break;
                case "one_big_pic":
                    imgBig.setImageResource(R.mipmap.radio_button_on);
                    tvBig.setTextColor(getResources().getColor(R.color.text_33));
                    imgSmall.setImageResource(R.mipmap.radio_button_off);
                    tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                    imgThree.setImageResource(R.mipmap.radio_button_off);
                    tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                    lnThreePic.setVisibility(View.GONE);
                    rlBigPic.setVisibility(View.VISIBLE);
                    rlPic2.setVisibility(View.INVISIBLE);
                    rlPic3.setVisibility(View.INVISIBLE);
                    bigurl=ads.getAd_title_pics().get(0);
                    GlideApp.with(mContext).load(bigurl).into(imgBigPic);
                    break;
                case "three_pics":
                    imgThree.setImageResource(R.mipmap.radio_button_on);
                    tvThree.setTextColor(getResources().getColor(R.color.text_33));
                    imgBig.setImageResource(R.mipmap.radio_button_off);
                    tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                    imgSmall.setImageResource(R.mipmap.radio_button_off);
                    tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                    lnThreePic.setVisibility(View.VISIBLE);
                    rlBigPic.setVisibility(View.GONE);
                    rlPic2.setVisibility(View.VISIBLE);
                    rlPic3.setVisibility(View.VISIBLE);
                    smallurl1=ads.getAd_title_pics().get(0);
                    GlideApp.with(mContext).load(smallurl1).into(imgPic1);
                    smallurl2=ads.getAd_title_pics().get(1);
                    GlideApp.with(mContext).load(smallurl2).into(imgPic2);
                    smallurl3=ads.getAd_title_pics().get(2);
                    GlideApp.with(mContext).load(smallurl3).into(imgPic3);
                    break;
            }
            edTitle.setText(ads.getAd_title());
            html=ads.getAd_content();
            if (!html.isEmpty()) {
                lnWeb.setVisibility(View.VISIBLE);
                rlInput.setVisibility(View.GONE);
                webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            }
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_admode,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_title,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_content,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_big,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small1,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small2,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small3,"");
        }
        showTitleRightText(getString(R.string.next), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (ad_mode) {
                    case "one_small_pic":
                        if (smallurl1.isEmpty()) {
                            ToastUtil.showToast(getString(R.string.plz_put_img));
                            return;
                        }
                        break;
                    case "one_big_pic":
                        if (bigurl.isEmpty()) {
                            ToastUtil.showToast(getString(R.string.plz_put_img));
                            return;
                        }
                        break;
                    case "three_pics":
                        if (smallurl1.isEmpty() ||
                                smallurl2.isEmpty() ||
                                smallurl3.isEmpty()) {
                            ToastUtil.showToast(getString(R.string.plz_put_img));
                            return;
                        }
                        break;
                }
                if (edTitle.getText().toString().isEmpty()) {
                    ToastUtil.showToast(getString(R.string.plz_put_title));
                    return;
                }
                if (html.isEmpty()) {
                    ToastUtil.showToast(getString(R.string.plz_put_content));
                    return;
                }
                if (edTitle.getText().toString().length() < 10) {
                    ToastUtil.showToast(getString(R.string.less_than_title));
                    return;
                }
                if (id!=null&&!id.isEmpty()){
                    updata();
                }else {
                    upload();
                }
            }
        });

    }
    public void getToken(String imgtype) {
        mStateView.showLoading();
        String time = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_Expiration) + "";
        if (!time.isEmpty()) {
            time = time.replace("T", " ");
            time = time.replace("Z", "");
            Long expiration = TimeUtil.parseMillsTime(time, "yyyy-MM-dd HH:mm:ss");
            if (expiration + (3600 * 8 - 60) * 1000 > System.currentTimeMillis()) {
                uploadImg(imgtype);
                return;
            }
        }
        Map map = new HashMap();
        map.put("mode", "mobile");
        YSBSdk.getService(OAuthService.class).stst(map, new YRequestCallback<StsBean>() {
            @Override
            public void onSuccess(StsBean var1) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_Expiration, var1.getExpiration());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_AccessKeyId, var1.getAccessKeyId());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_AccessKeySecret, var1.getAccessKeySecret());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_SecurityToken, var1.getSecurityToken());
                uploadImg(imgtype);
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                tvRight.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){

                }

            }
        });
    }

    private String imgurl;
    private String smallurl1="",smallurl2="",smallurl3="",bigurl="";
    private void
    uploadImg(final String  imgtype) {
        File f=null;
        switch (imgtype){
            case "1":
                f=small1;
                break;
            case "2":
                f=big;
                break;
            case "3":
                f=small2;
                break;
            case "4":
                f=small3;
                break;
        }
        imgurl = "cover/img_" + System.currentTimeMillis()+ "_" + ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME) + ".jpg";
        YSBSdk.getService(OAuthService.class).upload_oss(getApplicationContext(), imgurl, f.getAbsolutePath(), new OssCallback() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (imgtype.equals("1")){
                    smallurl1 = "http://prod-static.shijinsz.net/" + imgurl;
                }else if (imgtype.equals("2")){
                    bigurl = "http://prod-static.shijinsz.net/" + imgurl;
                }else if (imgtype.equals("3")){
                    smallurl2 = "http://prod-static.shijinsz.net/" + imgurl;
                }else {
                    smallurl3 = "http://prod-static.shijinsz.net/" + imgurl;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStateView.showContent();
                    }
                });

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ToastUtil.showToast("图片上传失败，请重新选择图片");
                if (imgtype.equals("1")){
                    smallurl1 = "";
                    imgPic1.setImageDrawable(null);
                }else if (imgtype.equals("2")){
                    bigurl = "";
                    imgBigPic.setImageDrawable(null);
                }else if (imgtype.equals("3")){
                    smallurl2 = "";
                    imgPic2.setImageDrawable(null);
                }else {
                    smallurl3 = "";
                    imgPic3.setImageDrawable(null);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStateView.showContent();
                    }
                });
            }
        });
    }

    private List<String> arrarurl=new ArrayList<>();
    private void upload(){
        mStateView.showLoading();
        arrarurl.clear();
        Map map = new HashMap();
        map.put("ad_title",edTitle.getText().toString());
        map.put("ad_content",html);
        map.put("ad_type","picture");
        map.put("ad_mode",ad_mode);
        switch (ad_mode){
            case "one_small_pic":
                arrarurl.add(smallurl1);
                map.put("ad_title_pics",arrarurl);
                break;
            case "one_big_pic":
                arrarurl.add(bigurl);
                map.put("ad_title_pics",arrarurl);
                break;
            case "three_pics":
                arrarurl.add(smallurl1);
                arrarurl.add(smallurl2);
                arrarurl.add(smallurl3);
                map.put("ad_title_pics",arrarurl);
                break;
        }
        YSBSdk.getService(OAuthService.class).new_title(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
               mStateView.showContent();
               tvRight.setEnabled(true);
               id=var1.getAid();
               ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_title, edTitle.getText().toString());
               ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_content, html);
               ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_type, "graphic");
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("off")){
                    Intent intent = new Intent(mContext,SettingRedBagActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext,FilterActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                tvRight.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    ToastUtil.showToast("图片上传失败，请重新上传");
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
    private void updata(){
        mStateView.showLoading();
        arrarurl.clear();
        Map map = new HashMap();
        map.put("ad_title",edTitle.getText().toString());
        map.put("mode","detail");
        map.put("ad_content",html);
        map.put("ad_type","picture");
        map.put("ad_mode",ad_mode);
        switch (ad_mode){
            case "one_small_pic":
                arrarurl.add(smallurl1);
                map.put("ad_title_pics",arrarurl);
                break;
            case "one_big_pic":
                arrarurl.add(bigurl);
                map.put("ad_title_pics",arrarurl);
                break;
            case "three_pics":
                arrarurl.add(smallurl1);
                arrarurl.add(smallurl2);
                arrarurl.add(smallurl3);
                map.put("ad_title_pics",arrarurl);
                break;
        }
        YSBSdk.getService(OAuthService.class).updata_ads(id,map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                tvRight.setEnabled(true);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_title, edTitle.getText().toString());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_content, html);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_type, "graphic");
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_is_advertiser).equals("off")){
                    Intent intent = new Intent(mContext,SettingRedBagActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext,FilterActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailed(String var1, String message) {
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
                tvRight.setEnabled(true);
            }

            @Override
            public void onException(Throwable var1) {
                try {
                    ToastUtil.showToast("图片上传失败，请重新上传");
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            needPop();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String ad_mode = "one_small_pic";

    @OnClick({R.id.img_edit, R.id.ln_small, R.id.ln_big, R.id.ln_three, R.id.rl_big_pic, R.id.rl_pic1, R.id.rl_pic2, R.id.rl_pic3, R.id.rl_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_edit:
                Intent intent = new Intent(mContext, RichEditorActivity.class);
                intent.putExtra("html", html);
                startActivityForResult(intent, 100);
                break;
            case R.id.ln_small:
                ad_mode = "one_small_pic";
                imgSmall.setImageResource(R.mipmap.radio_button_on);
                tvSmall.setTextColor(getResources().getColor(R.color.text_33));
                imgBig.setImageResource(R.mipmap.radio_button_off);
                tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                imgThree.setImageResource(R.mipmap.radio_button_off);
                tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                lnThreePic.setVisibility(View.VISIBLE);
                rlBigPic.setVisibility(View.GONE);
                rlPic2.setVisibility(View.INVISIBLE);
                rlPic3.setVisibility(View.INVISIBLE);
                break;
            case R.id.ln_big:
                ad_mode = "one_big_pic";
                imgBig.setImageResource(R.mipmap.radio_button_on);
                tvBig.setTextColor(getResources().getColor(R.color.text_33));
                imgSmall.setImageResource(R.mipmap.radio_button_off);
                tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                imgThree.setImageResource(R.mipmap.radio_button_off);
                tvThree.setTextColor(getResources().getColor(R.color.text_999999));
                lnThreePic.setVisibility(View.GONE);
                rlBigPic.setVisibility(View.VISIBLE);
                rlPic2.setVisibility(View.INVISIBLE);
                rlPic3.setVisibility(View.INVISIBLE);
                break;
            case R.id.ln_three:
                ad_mode = "three_pics";
                imgThree.setImageResource(R.mipmap.radio_button_on);
                tvThree.setTextColor(getResources().getColor(R.color.text_33));
                imgBig.setImageResource(R.mipmap.radio_button_off);
                tvBig.setTextColor(getResources().getColor(R.color.text_999999));
                imgSmall.setImageResource(R.mipmap.radio_button_off);
                tvSmall.setTextColor(getResources().getColor(R.color.text_999999));
                lnThreePic.setVisibility(View.VISIBLE);
                rlBigPic.setVisibility(View.GONE);
                rlPic2.setVisibility(View.VISIBLE);
                rlPic3.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_big_pic:
                type = 1;
                showEarlyDialog();
                break;
            case R.id.rl_pic1:
                type = 2;
                showEarlyDialog();
                break;
            case R.id.rl_pic2:
                type = 3;
                showEarlyDialog();
                break;
            case R.id.rl_pic3:
                type = 4;
                showEarlyDialog();
                break;
            case R.id.rl_input:
                startActivityForResult(new Intent(mContext, RichEditorActivity.class), 100);
                break;
        }
    }

    private String html = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                html = data.getStringExtra("html");
                if (html != null) {
                    if (!html.isEmpty()) {
                        lnWeb.setVisibility(View.VISIBLE);
                        rlInput.setVisibility(View.GONE);
                        webview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                    }else {
                        lnWeb.setVisibility(View.GONE);
                        rlInput.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private File big, small1, small2, small3;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    big = new File(img);
                    getToken("2");
                    GlideApp.with(mContext).load(big).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgBigPic);
                    break;
                case 2:
                    small1 = new File(img);
                    getToken("1");
                    GlideApp.with(mContext).load(small1).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPic1);
                    break;
                case 3:
                    small2 = new File(img);
                    getToken("3");
                    GlideApp.with(mContext).load(small2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPic2);
                    break;
                case 4:
                    small3 = new File(img);
                    getToken("4");
                    GlideApp.with(mContext).load(small3).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPic3);
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
        Message message = new Message();
        message.arg1 = type;
        handler.sendMessage(message);
    }

    private PopupWindow draftwindow;
    private void showDraftDialog(){
        View popupView = mInflater.inflate(R.layout.choice_pic_pop, null);
        if (draftwindow == null)
            draftwindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView camera = popupView.findViewById(R.id.camera);
        TextView choice = popupView.findViewById(R.id.choice);
        TextView cancel = popupView.findViewById(R.id.cancel);
        camera.setText(getString(R.string.save_draft));
        choice.setText(getString(R.string.no_save));
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        draftwindow.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景颜色
        draftwindow.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        draftwindow.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        draftwindow.setOutsideTouchable(true);
        draftwindow.update();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_admode,ad_mode);
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_title,edTitle.getText().toString()+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_content,html+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_big,bigurl+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small1,smallurl1+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small2,smallurl2+"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small3,smallurl3+"");
                finish();
                draftwindow.dismiss();
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_admode,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_title,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_content,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_big,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small1,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small2,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small3,"");
                finish();
                draftwindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draftwindow.dismiss();
            }
        });
        draftwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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

    private PopupWindow window;
    private Uri imageUri;

    public void showEarlyDialog() {
        verifyStoragePermissions(mActivity);
        View popupView = mInflater.inflate(R.layout.choice_pic_pop, null);
        if (window == null)
            window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        TextView camera = popupView.findViewById(R.id.camera);
        TextView choice = popupView.findViewById(R.id.choice);
        TextView cancel = popupView.findViewById(R.id.cancel);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.showAtLocation(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        window.update();

        cropOptions = new CropOptions.Builder();
        if (type == 1) {
            file = new File(Environment.getExternalStorageDirectory(), "/temp/big.jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            imageUri = Uri.fromFile(file);
            cropOptions.setWithOwnCrop(false).setAspectX(682).setAspectY(364);
        } else if (type == 2) {
            file = new File(Environment.getExternalStorageDirectory(), "/temp/small1.jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            imageUri = Uri.fromFile(file);
            cropOptions.setWithOwnCrop(false).setAspectX(220).setAspectY(144);

        } else if (type == 3) {
            file = new File(Environment.getExternalStorageDirectory(), "/temp/small2.jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            imageUri = Uri.fromFile(file);
            cropOptions.setWithOwnCrop(false).setAspectX(220).setAspectY(144);

        } else {
            file = new File(Environment.getExternalStorageDirectory(), "/temp/small3.jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            imageUri = Uri.fromFile(file);
            cropOptions.setWithOwnCrop(false).setAspectX(220).setAspectY(144);
        }
        LubanOptions option = new LubanOptions.Builder().setMaxSize(400000).create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);
        getTakePhoto().onEnableCompress(config, true);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromCaptureWithCrop(imageUri, cropOptions.create());
                window.dismiss();
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromGalleryWithCrop(imageUri, cropOptions.create());
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

    //判断是否弹出保存草稿
    private void needPop(){
        if (edTitle.getText().toString().isEmpty()&&html.isEmpty()&&smallurl1.isEmpty()&&smallurl2.isEmpty()&&smallurl3.isEmpty()&&
                bigurl.isEmpty()){
//            ToastUtil.showToast("已保存至草稿箱");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_admode,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_title,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_content,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_big,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small1,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small2,"");
            ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small3,"");
            finish();
        }else {
            if (id==null||id.isEmpty()){
                showDraftDialog();
            }else {
                ToastUtil.showToast("已保存至草稿箱");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_admode,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_title,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_content,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_big,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small1,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small2,"");
                ShareDataManager.getInstance().save(mContext,SharedPreferencesKey.KEY_draft_small3,"");
                finish();
            }
        }
    }
}
