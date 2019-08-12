package com.shijinsz.shijin.ui.ad;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.githang.statusbar.StatusBarCompat;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.RichEditor;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.OssCallback;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
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
 * Created by yrdan on 2018/8/16.
 */

public class RichEditorActivity extends BaseActivity {
    @BindView(R.id.rich_editor)
    RichEditor richEditor;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_undo)
    ImageView ivUndo;
    @BindView(R.id.img_pic)
    ImageView imgPic;
    @BindView(R.id.img_bold)
    ImageView imgBold;
    @BindView(R.id.img_middle)
    ImageView imgMiddle;
    @BindView(R.id.img_color)
    ImageView imgColor;
    private File file;
    private String url = "";
    private PopupWindow window;
    private CropOptions.Builder cropOptions;
    private Uri imageUri;
    private PopupWindow boldPopup,colorPopup,sidePopup;
    private ImageView bold,tilt,underline,black,gray,red,orange,green,blue,purple,left,middle;
    private View boldPopupView,colorPopupView,sidePopupView;

    @Override
    public int bindLayout() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setSteepStatusBar(false);
        return R.layout.richeditor_activity;
    }
    InputMethodManager imm;
    @Override
    public void initView(View view) {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        StatusBarCompat.setTranslucent(getWindow(), false);
//        StatusBarUtil.setStatusTextColor(false, mActivity);
        String html = getIntent().getStringExtra("html");
//        richEditor.loadCSS("file:///android_asset/img.css");
        richEditor.focusEditor();
        richEditor.setEditorFontColor(getResources().getColor(R.color.text_33));
        richEditor.setPadding(15, 0, 15, 10);
        richEditor.setEditorFontSize(18);
        if (html != null) {
            richEditor.setHtml(html);
        }
        richEditor.setEditorFontColor(getResources().getColor(R.color.text_33));
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    file = new File(imgs);
                    getToken();
//                    GlideApp.with(mContext).load(imgs).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgId);
                    break;
                case 2:
                    richEditor.insertImage(url, "img");
                    break;

            }
        }
    };
    private String imgs;
    private int type = 1;

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        imgs = result.getImage().getCompressPath();
        Message message = new Message();
        message.arg1 = type;
        handler.sendMessage(message);
        File f = new File(imgs);
        Log.e(TAG, "takeSuccess: " + f.length());
        Log.e(TAG, "takeSuccess: " + file.length());
    }
    public boolean isBold=false,isTilt=false,isUnderline=false;
    public void showBoldPop() {
        if (boldPopupView==null)
        boldPopupView = mInflater.inflate(R.layout.bold_pop, null);
        if (boldPopup == null)
            boldPopup = new PopupWindow(boldPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        boldPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width=boldPopupView.getMeasuredWidth();
        int height = boldPopupView.getMeasuredHeight();
        bold = boldPopupView.findViewById(R.id.bt_bold);
        tilt = boldPopupView.findViewById(R.id.bt_tilt);
        underline = boldPopupView.findViewById(R.id.bt_underline);
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setBold();
                if (isBold){
                    isBold=false;
                    bold.setImageResource(R.mipmap.icon_bold);
                    bold.setBackgroundColor(getResources().getColor(R.color.transparent));
                }else {
                    isBold=true;
                    bold.setImageResource(R.mipmap.icon_bold_2);
                    bold.setBackground(getResources().getDrawable(R.drawable.red_cycle));
                }
            }
        });
        tilt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setItalic();
                if (isTilt){
                    isTilt=false;
                    tilt.setImageResource(R.mipmap.icon_tilt);
                    tilt.setBackgroundColor(getResources().getColor(R.color.transparent));
                }else {
                    isTilt=true;
                    tilt.setImageResource(R.mipmap.icon_tilt_2);
                    tilt.setBackground(getResources().getDrawable(R.drawable.red_cycle));
                }
            }
        });
        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setUnderline();
                if (isUnderline){
                    isUnderline=false;
                    underline.setImageResource(R.mipmap.icon_underline);
                    underline.setBackgroundColor(getResources().getColor(R.color.transparent));
                }else {
                    isUnderline=true;
                    underline.setImageResource(R.mipmap.icon_underline_2);
                    underline.setBackground(getResources().getDrawable(R.drawable.red_cycle));
                }
            }
        });
        //设置背景颜色
        boldPopup.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        boldPopup.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        boldPopup.setOutsideTouchable(true);
        int[] location=new int[2];
        imgBold.getLocationOnScreen(location);
        int x =(location[0]+imgBold.getWidth()/2-width/2);
        int y =location[1]-imgBold.getHeight();
        boldPopup.showAtLocation(imgBold, Gravity.NO_GRAVITY, x, y);
        //   boldPopup.update();
    }

    public void showSidePop() {
        if (sidePopupView==null)
            sidePopupView = mInflater.inflate(R.layout.side_pop, null);
        if (sidePopup == null)
            sidePopup = new PopupWindow(sidePopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        sidePopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width=sidePopupView.getMeasuredWidth();
        int height = sidePopupView.getMeasuredHeight();
        left = sidePopupView.findViewById(R.id.bt_left);
        middle = sidePopupView.findViewById(R.id.bt_middle);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setAlignLeft();
                middle.setImageResource(R.mipmap.icon_middle);
                middle.setBackgroundColor(getResources().getColor(R.color.transparent));
                left.setImageResource(R.mipmap.icon_left_2);
                left.setBackground(getResources().getDrawable(R.drawable.red_cycle));
            }
        });
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setAlignCenter();
                left.setImageResource(R.mipmap.icon_left);
                left.setBackgroundColor(getResources().getColor(R.color.transparent));
                middle.setImageResource(R.mipmap.icon_middle_2);
                middle.setBackground(getResources().getDrawable(R.drawable.red_cycle));

            }
        });
        // 设置背景颜色
        sidePopup.setBackgroundDrawable(new BitmapDrawable());
        sidePopup.setFocusable(true);
        sidePopup.setOutsideTouchable(true);
        int[] location=new int[2];
        imgMiddle.getLocationOnScreen(location);
        int x =(location[0]+imgMiddle.getWidth()/2-width/2);
        int y =location[1]-imgMiddle.getHeight();
        sidePopup.showAtLocation(imgMiddle, Gravity.NO_GRAVITY, x, y);
        //   boldPopup.update();
    }
    public void showColorPop() {
        if (colorPopupView==null)
            colorPopupView = mInflater.inflate(R.layout.color_pop, null);
        if (colorPopup == null)
            colorPopup = new PopupWindow(colorPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        black=colorPopupView.findViewById(R.id.bt_black);
        gray=colorPopupView.findViewById(R.id.bt_gray);
        red=colorPopupView.findViewById(R.id.bt_red);
        orange=colorPopupView.findViewById(R.id.bt_orange);
        green=colorPopupView.findViewById(R.id.bt_green);
        blue=colorPopupView.findViewById(R.id.bt_blue);
        purple=colorPopupView.findViewById(R.id.bt_purple);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.text_33));
                black.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.gray));
                gray.setBackground(getResources().getDrawable(R.drawable.color_black));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.red));
                red.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.orange));
                orange.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.green));
                green.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.blue));
                blue.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
                purple.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(getResources().getColor(R.color.purple));
                purple.setBackground(getResources().getDrawable(R.drawable.color_black));
                gray.setBackgroundColor(getResources().getColor(R.color.transparent));
                red.setBackgroundColor(getResources().getColor(R.color.transparent));
                orange.setBackgroundColor(getResources().getColor(R.color.transparent));
                green.setBackgroundColor(getResources().getColor(R.color.transparent));
                blue.setBackgroundColor(getResources().getColor(R.color.transparent));
                black.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        int[] location=new int[2];
        imgColor.getLocationOnScreen(location);
        colorPopup.setBackgroundDrawable(new BitmapDrawable());
        colorPopup.setFocusable(true);
        colorPopup.setOutsideTouchable(true);
        colorPopup.showAtLocation(imgColor, Gravity.NO_GRAVITY, (location[0]+imgColor.getWidth()/2)-colorPopup.getWidth()/2, location[1]-imgColor.getHeight());
    }

    public void showEarlyDialog() {
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
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();

        cropOptions = new CropOptions.Builder();
        file = new File(Environment.getExternalStorageDirectory(), "/temp/id_yyzz.jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        cropOptions.setWithOwnCrop(false).setAspectX(436).setAspectY(328);
        LubanOptions option = new LubanOptions.Builder().setMaxWidth(480).setMaxSize(400000).create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(false);
        getTakePhoto().onEnableCompress(config, true);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromCapture(imageUri);
                window.dismiss();
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTakePhoto().onPickFromGallery();
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

    public void getToken() {
        mStateView.showLoading();
        String time = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_Expiration) + "";
        if (!time.isEmpty()) {
            time = time.replace("T", " ");
            time = time.replace("Z", "");
            Long expiration = TimeUtil.parseMillsTime(time, "yyyy-MM-dd HH:mm:ss");
            if (expiration + (3600 * 8 - 60) * 1000 > System.currentTimeMillis()) {
                uploadImg();
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
                uploadImg();
            }

            @Override
            public void onFailed(String var1, String message) {
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
        mStateView.showContent();
        File f;
        imgurl = "content/img_" + System.currentTimeMillis() + "_" + ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME) + ".jpg";
        f = file;
        YSBSdk.getService(OAuthService.class).upload_oss(getApplicationContext(), imgurl, f.getAbsolutePath(), new OssCallback() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                mStateView.showContent();
                url = "http://prod-static.shijinsz.net/" + imgurl;
                richEditor.post(new Runnable() {
                    @Override
                    public void run() {
                        richEditor.insertImage(url, "img");
                    }
                });
//                Message message=new Message();
//                message.arg1=2;
//                handler.sendMessage(message);

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ToastUtil.showToast("图片上传失败，请重新上传");
                mStateView.showContent();
                commit.setEnabled(true);
            }
        });
    }


    @OnClick({R.id.tv_right, R.id.iv_back, R.id.iv_undo, R.id.img_pic, R.id.img_bold, R.id.img_middle, R.id.img_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent();
                intent.putExtra("html", richEditor.getHtml());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.iv_back:
                richEditor.undo();
                break;
            case R.id.iv_undo:
                richEditor.redo();
                break;
            case R.id.img_pic:
                showEarlyDialog();
                break;
            case R.id.img_bold:
                showBoldPop();
                break;
            case R.id.img_middle:
                showSidePop();
                break;
            case R.id.img_color:
                showColorPop();
                break;
        }
    }
}
