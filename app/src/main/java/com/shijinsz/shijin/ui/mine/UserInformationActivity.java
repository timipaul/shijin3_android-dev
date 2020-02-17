package com.shijinsz.shijin.ui.mine;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TimeUtil;
import com.hongchuang.hclibrary.view.CircleImageView;
import com.hongchuang.hclibrary.view.MyGridView;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.OssCallback;
import com.hongchuang.ysblibrary.model.model.bean.ChangeUserInfoBean;
import com.hongchuang.ysblibrary.model.model.bean.InterestBean;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.StsBean;
import com.hongchuang.ysblibrary.utils.PopSetectDate;
import com.hongchuang.ysblibrary.utils.PopSetectPlace;
import com.hongchuang.ysblibrary.utils.PopSetectSingle;
import com.hongchuang.ysblibrary.utils.SetText;
import com.hongchuang.ysblibrary.utils.SetText2;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.CommonAdapter;
import com.shijinsz.shijin.base.ViewHolder;
import com.shijinsz.shijin.utils.ErrorUtils;
import com.shijinsz.shijin.utils.GlideApp;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.callback.YRequestCallback;

import static mapsdkvi.com.gdi.bgl.android.java.EnvDrawText.bmp;

/**
 * Created by yrdan on 2018/8/3.
 */

public class UserInformationActivity extends BaseActivity {
    @BindView(R.id.bt_left)
    ImageView btLeft;
    @BindView(R.id.bt_right)
    ImageView btRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.ln_head)
    LinearLayout lnHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ln_nickname)
    LinearLayout lnNickname;
    @BindView(R.id.tv_birth)
    TextView tvBirth;
    @BindView(R.id.ln_birth)
    LinearLayout lnBirth;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ln_sex)
    LinearLayout lnSex;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ln_area)
    LinearLayout lnArea;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ln_getmoney)
    LinearLayout lnGetmoney;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.ln_job)
    LinearLayout lnJob;
    @BindView(R.id.ln_habit)
    LinearLayout lnHabit;
    @BindView(R.id.tv_habit)
    TextView tvHabit;
    @BindView(R.id.jiantou)
    ImageView jiantou;
    @BindView(R.id.ln_interest)
    LinearLayout lnInterest;
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.img_nickname)
    ImageView imgNickname;
    private File f;
    private Bitmap bitmap;
    private Map map = new HashMap();
    private List<String> interest = new ArrayList<>();
    private boolean isBt1 = false, isBt2 = false, isBt3 = false, isBt4 = false, isBt5 = false, isBt6 = false, isBt7 = false, isBt8 = false, isBt9 = false, isBt10 = false, isBt11 = false, isBt12 = false;
    private String tvInterest = "";
    private boolean isShow = false;
    private PopSetectDate popSetectDate;
    private PopSetectPlace popSetectPlace;
    private PopSetectSingle popSetectSex, popSetectMoney, popSetectJob;
    private List<String> sexlist = new ArrayList<>();
    private List<String> moneylist = new ArrayList<>();
    private List<String> joblist = new ArrayList<>();
    private String imgurl;
    private List<InterestBean> list = new ArrayList<>();
    private CommonAdapter<InterestBean> adapter;

    @Override
    public int bindLayout() {
        return R.layout.user_information_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle("个人信息");
        showTitleBackButton();
        sexlist.add("男");
        sexlist.add("女");
        moneylist.add("其他");
        moneylist.add("2K以下");
        moneylist.add("2~5K");
        moneylist.add("5~10K");
        moneylist.add("10~20K");
        joblist.add("宝妈");
        joblist.add("医生");
        joblist.add("白领");
        joblist.add("IT");
        joblist.add("自由职业");
        joblist.add("教师");
        joblist.add("其他");
        list.add(new InterestBean("skincare", "护肤", false));
        list.add(new InterestBean("emotion", "情感", false));
        list.add(new InterestBean("beauty", "美妆", false));
        list.add(new InterestBean("house", "家居", false));
        list.add(new InterestBean("special_sale", "特卖", false));
        list.add(new InterestBean("childcare", "母婴", false));
        list.add(new InterestBean("video", "视频", false));
        list.add(new InterestBean("life", "生活", false));
        list.add(new InterestBean("food", "美食", false));
        list.add(new InterestBean("APP", "APP", false));
        list.add(new InterestBean("tourism", "旅游", false));
        list.add(new InterestBean("other", "其他", false));
        initData();
    }
    private boolean isChange=false;

    public void initData() {
        String date = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_birth);
        String birth;
//        if (date.isEmpty()){
//            birth = "";
//        }else {
//            birth = TimeUtil.formatName(Long.parseLong(date)*1000);
//        }
        interest.addAll(ShareDataManager.getInstance().getParaList(SharedPreferencesKey.KEY_interest));
        if (interest != null && interest.size() > 0) {
            for (String s : interest) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getInterest().equals(s)) {
                        list.get(i).setSelect(true);
                    }
                }
            }
            String inter = "";
            for (InterestBean interestBean : list) {
                if (interestBean.isSelect) {
                    inter = inter + "," + interestBean.getName();
                }
            }
            if (inter.length() > 0)
                inter = inter.substring(1, inter.length());
            tvHabit.setText(inter);
        }
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname_update_number).equals("0")) {
            imgNickname.setVisibility(View.VISIBLE);
        } else {
            imgNickname.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvNickname.getLayoutParams());
            lp.setMargins(0, 0, 20, 0);
            tvNickname.setLayoutParams(lp);
        }
        adapter = new CommonAdapter<InterestBean>(mContext, list, R.layout.interest_item) {
            @Override
            public void convert(ViewHolder helper, InterestBean item, int position) {
                isChange=true;
                if (item.isSelect) {
                    helper.setTextColor(R.id.bt1, R.color.white);
                    helper.setBackgroundResource(R.id.bt1, R.drawable.btn_checked);
                } else {
                    helper.setTextColor(R.id.bt1, R.color.text_33);
                    helper.setBackgroundResource(R.id.bt1, R.drawable.btn_unchecked);
                }
                helper.setText(R.id.bt1, item.getName());
            }
        };
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.get(i).setSelect(!list.get(i).isSelect());
                adapter.notifyDataSetChanged();
                String inter = "";
                for (InterestBean interestBean : list) {
                    if (interestBean.isSelect) {
                        inter = inter + "," + interestBean.getName();
                    }
                }
                if (inter.length() > 0)
                    inter = inter.substring(1, inter.length());
                tvHabit.setText(inter);
            }
        });
        GlideApp.with(mContext).load(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)).into(imgAvatar);
        tvNickname.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname));
        tvArea.setText(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_address));
        tvBirth.setText(date);
        if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_gender).equals("male"))
            tvSex.setText("男");
        else
            tvSex.setText("女");
        switch (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_job)) {
            case "doctor":
                tvJob.setText("医生");
                break;
            case "IT":
                tvJob.setText("IT");
                break;
            case "teacher":
                tvJob.setText("教师");
                break;
            case "mother":
                tvJob.setText("宝妈");
                break;
            case "white_collar":
                tvJob.setText("白领");
                break;
            case "freelance":
                tvJob.setText("自由职业");
                break;
            default:
                tvJob.setText("其他");
                break;
        }
        switch (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_income)) {
            case "2000":
                tvMoney.setText("2K以下");
                break;
            case "5000":
                tvMoney.setText("2K~5K");
                break;
            case "10000":
                tvMoney.setText("5K~10K");
                break;
            case "20000":
                tvMoney.setText("10K~20K");
                break;
            default:
                tvMoney.setText("其他");
                break;
        }
//        String habit = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_interest);

    }

    @OnClick({R.id.ln_head, R.id.ln_nickname, R.id.ln_birth, R.id.ln_sex, R.id.ln_area, R.id.ln_getmoney, R.id.ln_job, R.id.ln_habit})
    public void onViewClicked(View view) {
        View viewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        switch (view.getId()) {
            case R.id.ln_head:
                showEarlyDialog(mActivity, 10001, 10000);
                break;
            case R.id.ln_nickname:
                if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname_update_number).equals("0")) {
                    startActivityForResult(new Intent(mContext, ChangeNicknameActivity.class), 100);
                } else {
                    ToastUtil.showToast("昵称只能修改一次");
                }
                break;
            case R.id.ln_birth:
                if (popSetectDate == null)
                    popSetectDate = new PopSetectDate(mActivity);
                popSetectDate.showPopupWindow(viewGroup, new SetText() {
                    @Override
                    public void listenResult(String text) {
                        mStateView.showLoading();
                        tvBirth.setText(text);
//                        Long time =TimeUtil.parseTime(text,"yyyy.MM.dd");
                        change_info("birth", text);
                    }
                });
                break;
            case R.id.ln_sex:
                if (popSetectSex == null)
                    popSetectSex = new PopSetectSingle(mActivity, sexlist, 0);
                popSetectSex.showPopupWindow(viewGroup, new SetText() {
                    @Override
                    public void listenResult(String text) {
                        mStateView.showLoading();
                        tvSex.setText(text);
                        if (text.equals("男")) {
                            change_info("gender", "male");
                        } else {
                            change_info("gender", "female");
                        }
                    }
                });
                break;
            case R.id.ln_area:
                if (popSetectPlace == null)
                    popSetectPlace = new PopSetectPlace(mActivity);
                popSetectPlace.showPopupWindow(view, new SetText2() {
                    @Override
                    public void listenResult(String text, String province, String city, String area) {
                        mStateView.showLoading();
                        tvArea.setText(text);
                        change_info("address", text);
                    }
                });
                break;
            case R.id.ln_getmoney:
                if (popSetectMoney == null)
                    popSetectMoney = new PopSetectSingle(mActivity, moneylist);
                popSetectMoney.showPopupWindow(view, new SetText() {
                    @Override
                    public void listenResult(String text) {
                        mStateView.showLoading();
                        tvMoney.setText(text);
                        switch (text) {
                            case "2K以下":
                                change_info("income", "2000");
                                break;
                            case "2~5K":
                                change_info("income", "5000");
                                break;
                            case "5~10K":
                                change_info("income", "10000");
                                break;
                            case "10~20K":
                                change_info("income", "20000");
                                break;
                            case "其他":
                                change_info("income", "other");
                                break;
                        }
                    }
                });
                break;
            case R.id.ln_job:
                if (popSetectJob == null)
                    popSetectJob = new PopSetectSingle(mActivity, joblist);
                popSetectJob.showPopupWindow(view, new SetText() {
                    @Override
                    public void listenResult(String text) {
                        mStateView.showLoading();
                        tvJob.setText(text);
                        joblist.add("宝妈");
                        joblist.add("医生");
                        joblist.add("白领");
                        joblist.add("IT");
                        joblist.add("自由职业");
                        joblist.add("教师");
                        joblist.add("其他");
                        switch (text) {
                            case "宝妈":
                                change_info("job", "mother");
                                break;
                            case "医生":
                                change_info("job", "doctor");
                                break;
                            case "白领":
                                change_info("job", "white_collar");
                                break;
                            case "IT":
                                change_info("job", "IT");
                                break;
                            case "自由职业":
                                change_info("job", "freelance");
                                break;
                            case "教师":
                                change_info("job", "teacher");
                                break;
                            case "其他":
                                change_info("job", "other");
                                break;
                        }
                    }
                });
                break;
            case R.id.ln_habit:
                if (isShow) {
                    isShow = false;
                    gridview.setVisibility(View.GONE);
                    jiantou.setRotation(0);
                } else {
                    isShow = true;
                    gridview.setVisibility(View.VISIBLE);
                    jiantou.setRotation(90);
                }
                break;
        }
    }

    public void showEarlyDialog(Activity activity, final int CAMARA, final int PHOTO) {
        verifyStoragePermissions(activity);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mailBoxLay = (LinearLayout) inflater.inflate(
                R.layout.activity_mail_box_dialog, null);
        Button zy_btn = (Button) mailBoxLay.findViewById(R.id.zy_btn);
        Button yd_btn = (Button) mailBoxLay.findViewById(R.id.yd_btn);
        Button details = (Button) mailBoxLay.findViewById(R.id.details);
        zy_btn.setText("拍照");
        yd_btn.setText("相册");
        details.setVisibility(View.VISIBLE);
        final NoticeDialog mailBoxDialog = new NoticeDialog(this);
        mailBoxDialog.showDialog(mailBoxLay, 0, 0);
        zy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailBoxDialog.dismiss();
                try {

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 启动相机
                    startActivityForResult(intent1, CAMARA);
                } catch (SecurityException e) {
                    Toast.makeText(mContext, "权限未开启", Toast.LENGTH_SHORT).show();
                }


            }
        });

        yd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailBoxDialog.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "选择图片"), PHOTO);
                } catch (SecurityException e) {
                    Toast.makeText(mContext, "权限未开启", Toast.LENGTH_SHORT).show();
                }

            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailBoxDialog.dismiss();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
        if (resultCode == Activity.RESULT_OK) {
            //选择图片
            if (requestCode == 10000) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    if (bmp != null)//如果不释放的话，不断取图片，将会内存不够
                        bmp.recycle();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    bmp = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                imgHead.setImageBitmap(bmp);
                compressScale(bmp);
//                savebitmap(bmp);

            } else if (requestCode == 10001) {
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡

                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
//                    imgHead.setImageBitmap(bitmap);
                    compressScale(bitmap);


                } else {
                    Toast.makeText(this, "没有SD卡", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 100) {
                if (data != null) {
                    if (ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname_update_number).equals("0")) {
                        imgNickname.setVisibility(View.VISIBLE);
                    } else {
                        imgNickname.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvNickname.getLayoutParams());
                        lp.setMargins(0, 0, 20, 0);
                        tvNickname.setLayoutParams(lp);
                    }
                    tvNickname.setText(data.getStringExtra("nickname"));
                }
            }
        }

    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public void compressScale(Bitmap image) {
        mStateView.showLoading();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

        //return bitmap;
    }

    public void change_info(final String key, final Object value) {
        ChangeUserInfoBean map = new ChangeUserInfoBean();
        map.setMode("profile");
        map.setKey(key);
        map.setValue(value);
        YSBSdk.getService(OAuthService.class).change_info(ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_ID), map, new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {
                mStateView.showContent();
                ToastUtil.showToast("修改成功");
                switch (key) {
                    case "birth":
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_birth, (String) value);
                        break;
                    case "address":
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_address, (String) value);
                        break;
                    case "gender":
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_gender, (String) value);
                        break;
                    case "income":
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_income, (String) value);
                        break;
                    case "job":
                        ShareDataManager.getInstance().save(mContext, SharedPreferencesKey.KEY_job, (String) value);
                        break;
                }
            }

            @Override
            public void onFailed(String var1, String message) {
                mStateView.showContent();
                ErrorUtils.error(mContext,var1,message);
            }

            @Override
            public void onException(Throwable var1) {
                mStateView.showContent();
                mStateView.showRetry();
            }
        });

    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public void compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        savebitmap(bitmap);
    }

    public void savebitmap(Bitmap bmp) {
        f = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlideApp.with(mContext).load(f).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_launcher).circleCrop()
                .priority(Priority.LOW)
                .into(imgAvatar);
        getToken();
//        Glide.with(mContext).load(f).into(imgHead);

    }


    public void getToken() {
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
            }

            @Override
            public void onException(Throwable var1) {

            }
        });
    }

    private void uploadImg() {
        imgurl = "imgurl/img_" +  System.currentTimeMillis() +"_"+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME) +".jpg";
        YSBSdk.getService(OAuthService.class).upload_oss(getApplicationContext(), imgurl, f.getAbsolutePath(), new OssCallback() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                imgurl = "http://prod-static.shijinsz.net/" + imgurl;
                Log.e(TAG, "onSuccess: " + imgurl);
                change_info("imgurl", imgurl);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ToastUtil.showToast("图片上传失败，请重新上传");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (isChange) {
            interest.clear();
            for (InterestBean interestBean : list) {
                if (interestBean.isSelect()) {
                    interest.add(interestBean.getInterest());
                }
            }
            change_info("interests", interest);
        }
        super.onDestroy();
    }

}
