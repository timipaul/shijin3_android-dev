package com.shijinsz.shijin.ui.mine;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.model.model.OAuthService;
import com.hongchuang.ysblibrary.model.model.bean.PicCodeBean;
import com.hongchuang.ysblibrary.model.model.bean.ShenmiBean;
import com.hongchuang.ysblibrary.utils.NetworkUtil;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.meituan.android.walle.WalleChannelReader;
import com.shijinsz.shijin.BuildConfig;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.ui.home.NewGuideActivity;
import com.shijinsz.shijin.ui.store.UserLocationActivity;
import com.shijinsz.shijin.ui.user.LoginActivity;
import com.shijinsz.shijin.utils.APKVersionCodeUtils;
import com.shijinsz.shijin.utils.KdniaoTrackQueryAPI;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit.ToKenUtil;
import retrofit.callback.YRequestCallback;

import static com.shijinsz.shijin.ui.mine.MyVipActivity.KEY_privacy_code;
import static com.shijinsz.shijin.ui.mine.MyVipActivity.KEY_user_code;

/**
 * 设置
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.now_version)
    TextView nowVersion;
//    @BindView(R.id.tv_guide_info)
//    TextView tvGuide;

    @Override
    public int bindLayout() {
        return R.layout.setting_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        setTitle(getString(R.string.setting));
        nowVersion.setText(String.format(getString(R.string.now_version),APKVersionCodeUtils.getVerName(mContext)));
        showTitleBackButton();

        initData();

    }

    private void initData() {
        //华为设置隐藏开屏广告
        if(Build.MANUFACTURER.toLowerCase().contains("huawei")){
            YSBSdk.getService(OAuthService.class).getGameStatue(new YRequestCallback<ShenmiBean>() {
                @Override
                public void onSuccess(ShenmiBean var1) {
                    //tvGuide.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed(String var1, String message) {
                    //tvGuide.setVisibility(View.GONE);
                }

                @Override
                public void onException(Throwable var1) {
                    //tvGuide.setVisibility(View.GONE);
                }
            });
        }

    }


    @OnClick({R.id.tv_personal_info, R.id.tv_black_list, R.id.tv_user_safe, R.id.tv_data_cache,
            R.id.tv_logout,R.id.now_version,R.id.tv_discounts_info,
            R.id.tv_site_info,R.id.tv_user_agreement,R.id.tv_privacy_agreement})
    public void onViewClicked(View view) {

        Bundle bundle = null;

        switch (view.getId()) {
            case R.id.tv_personal_info:
                startActivity(UserInformationActivity.class);
                break;
            case R.id.tv_black_list:
                startActivity(BlackListActivity.class);
                break;
            case R.id.tv_user_safe:
                startActivity(UserSafeActivity.class);
                break;
            case R.id.tv_data_cache:
                startActivity(DataCacheActivity.class);
                break;
            case R.id.tv_logout:
                showDialog();
                break;
            case R.id.now_version:
                new Thread(runnable).start();
                break;
            case R.id.tv_discounts_info:
                //提货卡
                startActivity(CommodityDiscountsActivity.class);
                break;
//            case R.id.tv_guide_info:
//                //新手指南
//                startActivity(new Intent(mActivity, NewGuideActivity.class));
//                break;
            case R.id.tv_site_info:
                Intent intent = new Intent(mActivity, UserLocationActivity.class);
                intent.putExtra("select","false");
                startActivity(intent);
                break;
            case R.id.tv_user_agreement:
                //用户协议
                bundle = new Bundle();
                bundle.putString("code",KEY_user_code);
                startActivity(AgreementActivity.class,bundle);
                break;
            case R.id.tv_privacy_agreement:
                //隐私协议
                bundle = new Bundle();
                bundle.putString("code",KEY_privacy_code);
                startActivity(AgreementActivity.class,bundle);
                break;
        }
    }

    public void showDialog() {

        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.logout_dialog, null);
        final NoticeDialog mailDialog = new NoticeDialog(mContext);
        mailDialog.showDialog(mailBoxLay, 0, 0);
        ((TextView) mailBoxLay.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoff();
                ToKenUtil.deleteToken();
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_nickname);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_OPENID);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_qqid);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_invitation_code);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_is_advertiser);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_new_one_status);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_gender);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_age);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_birth);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_income);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_job);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_nickname_update_number);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_imageurl);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_points);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_address);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_change);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_interest);
                ShareDataManager.getInstance().delete(mContext, SharedPreferencesKey.KEY_is_login);
                startActivity(LoginActivity.class);
                finish();
                mailDialog.dismiss();
            }
        });
        ((TextView) mailBoxLay.findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailDialog.dismiss();
            }
        });
    }

    private void logoff() {
        YSBSdk.getService(OAuthService.class).logoff(new YRequestCallback<PicCodeBean>() {
            @Override
            public void onSuccess(PicCodeBean var1) {

            }

            @Override
            public void onFailed(String var1, String message) {
            }

            @Override
            public void onException(Throwable var1) {
                var1.printStackTrace();
            }
        });
    }
    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 111;
    private int REQUEST_SCAN_RESULT = 100;
    private NoticeDialog mailBoxDialog;
    private NoticeDialog mailBoxDialog2;
    //fir更新
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
//                Log.d(TAG, "handleMessage: " + result);
//                Gson gson = new Gson();
//                FirResult bean = gson.fromJson(result, FirResult.class);
                int build = 0;
                if (map != null) {
                    if (map.get("x-oss-meta-code") != null && !map.get("x-oss-meta-code").get(0).isEmpty()) {
                        build = Integer.parseInt(map.get("x-oss-meta-code").get(0));
                    }
                    if (build > APKVersionCodeUtils.getVersionCode(mContext)) {
                        showDownLoad();
                    }else {
                        ToastUtil.showToast("当前已是最新版");
                    }
                }

            }
        }
    };

    public void showDownLoad() {
        RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.new_version_diglog, null);
        mailBoxLay.findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(mActivity);
                String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
                ToastUtil.showToast("1 当前渠道："+channel);
                switch (channel+""){
                    case "360":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-360.apk");
                        break;
                    case "ali":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-ali.apk");
                        break;
                    case "baidu":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-baidu.apk");
                        break;
                    case "huawei":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-huawei.apk");
                        break;
                    case "meizu":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-meizu.apk");
                        break;
                    case "oppo":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-oppo.apk");
                        break;
                    case "other":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-other.apk");
                        break;
                    case "sougou":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-sougou.apk");
                        break;
                    case "vivo":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-vivo.apk");
                        break;
                    case "xiaomi":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-xiaomi.apk");
                        break;
                    case "yingyongbao":
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-yingyongbao.apk");
                        break;
                    default:
                        showDownloadProgressDialog(SettingActivity.this, "http://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release.apk");
                        break;
                }
            }
        });
        String str2 = new String(Base64.decode(map.get("x-oss-meta-content").get(0).getBytes(), Base64.DEFAULT));
        TextView tv_version = mailBoxLay.findViewById(R.id.tv_version);
        tv_version.setText("发现最新版本" + map.get("x-oss-meta-version").get(0));
        TextView tv_content = mailBoxLay.findViewById(R.id.tv_content);
        tv_content.setText(str2);
        mailBoxDialog2 = new NoticeDialog(mContext);
        mailBoxDialog2.showDialog(mailBoxLay, 0, 0);
        mailBoxDialog2.setCancelable(true);
    }

    String result = "";
    private Map<String, List<String>> map=new HashMap<>();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            result = NetworkUtil.doGet("http://api.fir.im/apps/latest/" + getResources().getString(R.string.FIR_ID) + "?api_token=" + getResources().getString(R.string.FIR_TOKEN));
            verifyStoragePermissions(mActivity);
            String channel = WalleChannelReader.getChannel(mContext.getApplicationContext());
           // ToastUtil.showToast("2 当前渠道："+channel);

            switch (channel+""){
                case "360":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-360.apk");
                    break;
                case "ali":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-ali.apk");
                    break;
                case "baidu":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-baidu.apk");
                    break;
                case "huawei":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-huawei.apk");
                    break;
                case "meizu":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-meizu.apk");
                    break;
                case "oppo":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-oppo.apk");
                    break;
                case "other":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-other.apk");
                    break;
                case "sougou":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-sougou.apk");
                    break;
                case "vivo":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-vivo.apk");
                    break;
                case "xiaomi":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-xiaomi.apk");
                    break;
                case "yingyongbao":
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release-yingyongbao.apk");
                    break;
                default:
                    map = NetworkUtil.doGetHeader("https://shijin-ad-pic.oss-cn-shenzhen.aliyuncs.com/android_apk/app-release.apk");
                    break;
            }
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }
    };

    private void showDownloadProgressDialog(Context context, String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        String downloadUrl = url;
        new SettingActivity.DownloadAPK(progressDialog).execute(downloadUrl);
    }
    /**
     * 下载APK的异步任务
     */

    private class DownloadAPK extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        File file;

        public DownloadAPK(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;

            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                bis = new BufferedInputStream(conn.getInputStream());
                int fileLength = conn.getContentLength();
                String fileName = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .getAbsolutePath() + "/magkare/action.apk";
                file = new File(fileName);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte data[] = new byte[4 * 1024];
                long total = 0;
                int count;
                while ((count = bis.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(data, 0, count);
                    fos.flush();
                }
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            openFile(file);
            progressDialog.dismiss();
        }

        private void openFile(File file) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(activity,"包名.fileprovider", takeImageFile));
//            } else {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takeImageFile));
//            }
            if (file != null) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    // 声明需要的零时权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // 第二个参数，即第一步中配置的authorities
                    Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                }
                startActivity(intent);
            }

        }
    }

}
