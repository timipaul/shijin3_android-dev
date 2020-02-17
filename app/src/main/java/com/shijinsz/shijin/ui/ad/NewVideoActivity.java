package com.shijinsz.shijin.ui.ad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.FileUtil;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.MyJzvdStd;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.OssCallback;
import com.hongchuang.ysblibrary.model.model.bean.Ads;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.utils.DialogUtils;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import retrofit.callback.YRequestCallback;

import static com.example.videolibrary.features.trim.VideoTrimmerActivity.call;

/**
 * Created by yrdan on 2018/8/22.
 */

public class NewVideoActivity extends BaseActivity {
    @BindView(R.id.rl_big_pic)
    RelativeLayout rlBigPic;
    @BindView(R.id.videoplayer)
    MyJzvdStd videoplayer;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.tv_title_num)
    TextView tvTitleNum;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.tv_tolong)
    TextView tvTolong;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private String path = "";
    private Ads ads;

    @Override
    public int bindLayout() {
        return R.layout.new_video_activty;
    }

    @Override
    public void initView(View view) {
        showTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                needPop();
            }
        });
        setTitle(getString(R.string.new_video));
        showTitleRightText(getString(R.string.next), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTitle.getText().toString().isEmpty()) {
                    ToastUtil.showToast(getString(R.string.plz_put_title));
                    return;
                }
                if (videourl.isEmpty()) {
                    ToastUtil.showToast(getString(R.string.plz_put_video));
                    return;
                }
                if (videourl.equals("tolong")){
                    ToastUtil.showToast("视频过大，请重新上传视频");
                    return;
                }
                if (edTitle.getText().toString().length() < 10) {
                    ToastUtil.showToast(getString(R.string.less_than_title));
                    return;
                }
                if (id==null||id.isEmpty()){
                    upload();
                }else {
                    updata();
                }
//                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_title, edTitle.getText().toString());
//                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_video, videourl);
//                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_type, "video");
//                startActivity(FilterActivity.class);
            }
        });
        dialogUtils=new DialogUtils(mContext);
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
        id = getIntent().getStringExtra("id");
        if (id != null && !id.isEmpty()) {
            ads = (Ads) getIntent().getSerializableExtra("ads");
            edTitle.setText(ads.getAd_title());
            videourl = ads.getAd_content();
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video_title, "");
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video, "");
        } else {
            edTitle.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_video_title));
            videourl = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_draft_video);
        }
        if (!videourl.isEmpty()) {
            videoplayer.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            videoplayer.setUp(videourl
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            GlideApp.with(mContext).load(fengmianurl + "?x-oss-process=video/snapshot,t_0,f_jpg").into(videoplayer.thumbImageView);
        }

    }

    private List<String> arrarurl = new ArrayList<>();

    private void upload() {
        tvRight.setEnabled(false);
        mStateView.showLoading();
        arrarurl.clear();
        arrarurl.add(fengmianurl + "?x-oss-process=video/snapshot,t_0,f_jpg");
        Map map = new HashMap();
        map.put("ad_title", edTitle.getText().toString());
        map.put("ad_content", videourl);
        map.put("ad_type", "video");
        map.put("ad_mode", "one_big_pic");
        map.put("ad_title_pics", arrarurl);
        YSBSdk.getService(OAuthService.class).new_title(map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                tvRight.setEnabled(true);
                id = var1.getAid();
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_title, edTitle.getText().toString());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_content, videourl);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_type, "video");
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
                try{
                    ToastUtil.showToast("上传失败，请重新上传");
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){

                }

            }
        });
    }

    private void updata() {
        tvRight.setEnabled(false);
        mStateView.showLoading();
        arrarurl.clear();
        arrarurl.add(fengmianurl + "?x-oss-process=video/snapshot,t_0,f_jpg");
        Map map = new HashMap();
        map.put("mode", "detail");
        map.put("ad_title", edTitle.getText().toString());
        map.put("ad_content", videourl);
        map.put("ad_type", "video");
        map.put("ad_mode", "one_big_pic");
        map.put("ad_title_pics", arrarurl);
        YSBSdk.getService(OAuthService.class).updata_ads(id, map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                tvRight.setEnabled(true);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_title, edTitle.getText().toString());
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_content, videourl);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_put_type, "video");
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
                try{
                    ToastUtil.showToast("上传失败，请重新上传");
                    mStateView.showContent();
                    tvRight.setEnabled(true);
                }catch (Exception e){

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

    @OnClick({R.id.rl_big_pic, R.id.img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_big_pic:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 66);
                break;
            case R.id.img_delete:
                FileUtil.deleteFolder(path);
                imgDelete.setVisibility(View.GONE);
                videoplayer.setVisibility(View.GONE);
                tvTolong.setVisibility(View.GONE);
                videourl="";
                videoplayer.releaseAllVideos();
                break;
        }

    }
    private DialogUtils dialogUtils;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String VIDEOPATH = cursor.getString(columnIndex);
            cursor.close();
            Log.e(TAG, "onActivityResult: " + VIDEOPATH);
            dialogUtils.showCommentDialog("是否需要进行视频剪辑？\n（该功能为测试功能，可能会出现异常）", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogUtils.dismissCommentDialog();
                    //VideoTrimmerActivity.call(NewVideoActivity.this, VIDEOPATH);
                    call(NewVideoActivity.this, VIDEOPATH);
                    //call(NewVideoActivity.this, VIDEOPATH);


                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogUtils.dismissCommentDialog();
                    path=VIDEOPATH;
                    videoplayer.setVisibility(View.VISIBLE);
                    imgDelete.setVisibility(View.VISIBLE);
                    videoplayer.setUp(path
                            , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
                    long size = iknow.android.utils.FileUtil.getFileSize(path);
                    Log.e(TAG, "video_size:" + size);
                    if (size > 10485760) {
                        tvTolong.setVisibility(View.VISIBLE);
                        videourl="tolong";
                    } else {
                        MediaPlayer player = new MediaPlayer();
                        try {
                            MediaMetadataRetriever media = new MediaMetadataRetriever();
                            media.setDataSource(path);
                            videoplayer.thumbImageView.setImageBitmap(media.getFrameAtTime());
                            player.setDataSource(path);  //recordingFilePath（）为音频文件的路径
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        duration= player.getDuration();//获取音频的时间
                        Log.d("ACETEST", "### duration: " + duration);
                        player.release();//记得释放资源
                        getToken();
                    }
                }
            });
//            submit_vd_ad.setText(VIDEOPATH);
        }
        if (requestCode == 0x001 && resultCode == RESULT_OK && null != data) {
            path = data.getStringExtra("url");

            long size = iknow.android.utils.FileUtil.getFileSize(path);
            if (size<=0){
                ToastUtil.showToast("视频剪辑失败，请重新选择视频");
                return;
            }
            Log.e(TAG, "video_size:" + size);
            videoplayer.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            videoplayer.setUp(path
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            if (size > 10485760) {
                tvTolong.setVisibility(View.VISIBLE);
                videourl="tolong";
            } else {
                MediaPlayer player = new MediaPlayer();
                try {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(path);
                    videoplayer.thumbImageView.setImageBitmap(media.getFrameAtTime());
                    player.setDataSource(path);  //recordingFilePath（）为音频文件的路径
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                duration= player.getDuration();//获取音频的时间
                Log.d("ACETEST", "### duration: " + duration);
                player.release();//记得释放资源
                getToken();
            }
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }
    int duration;
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
                ErrorUtils.error(mContext,var1,message);
                mStateView.showContent();
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
                mStateView.showContent();
            }
        });
    }


    private String imgurl;
    private String videourl = "";
    private String fengmianurl = "";
    private String bigurl = "";

    private void uploadImg() {
        imgurl = "content/video_" + System.currentTimeMillis() +"_"+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME) +  ".mp4";
        YSBSdk.getService(OAuthService.class).upload_oss(getApplicationContext(), imgurl, path, new OssCallback() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                videourl = "http://prod-static.shijinsz.net/" + imgurl + "#length="+duration;
                fengmianurl ="http://prod-static.shijinsz.net/" + imgurl;
//                ?x-oss-process=video/snapshot,t_7000,f_jpg,w_800,h_600,m_fast

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStateView.showContent();
                    }
                });
                Log.e(TAG, "onSuccess: " + videourl);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ToastUtil.showToast("视频上传失败，请重新选择视频");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStateView.showContent();
                    }
                });
            }
        });
    }


    private PopupWindow draftwindow;

    private void showDraftDialog() {
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
        // 设置背景颜色
        draftwindow.setBackgroundDrawable(new BitmapDrawable());
        //设置可以获取焦点
        draftwindow.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        draftwindow.setOutsideTouchable(true);
        draftwindow.update();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video, videourl);
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video_title, edTitle.getText().toString()+"");
                draftwindow.dismiss();
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video, "");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video_title, "");
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
    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
    }

    String id = "";

    //判断是否弹出保存草稿
    private void needPop() {
        if (edTitle.getText().toString().isEmpty() && videourl.isEmpty()) {
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video_title, "");
            ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video, "");
            finish();
        } else {
            if (id == null || id.isEmpty()) {
                showDraftDialog();
            } else {
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video_title, "");
                ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_draft_video, "");
                finish();
            }
        }
    }
}
