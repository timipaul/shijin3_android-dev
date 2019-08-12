package com.shijinsz.shijin.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.github.nukc.stateview.StateView;
import com.hongchuang.hclibrary.rxbus.RxBus;
import com.hongchuang.hclibrary.rxbus.SubscriptionBean;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.ysblibrary.YSBSdk;
import com.hongchuang.ysblibrary.common.GrobalListener;
import com.hongchuang.ysblibrary.common.RxEventType;
import com.hongchuang.ysblibrary.model.model.OAuthServiceImp;
import com.hongchuang.ysblibrary.widget.NoticeDialog;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.utils.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/7/14.
 * 应用程序Activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity implements GrobalListener,TakePhoto.TakeResultListener, InvokeListener {
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    public static final int REQUEST_LOGIN = 2222;
    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();
    protected LinearLayout layout_content;
    protected LayoutInflater mInflater;
    protected String mAddress, mTime;
    protected Unbinder mBinder;

    protected Context mContext = this;
    protected Activity mActivity = this;
    protected Bundle savedInstanceState;
    protected StateView mStateView;
    protected String openId="";
    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = true;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;
    /**
     * 当前Activity渲染的视图View
     **/
    private View mContextView = null;
    private RelativeLayout rl_pb;

    private boolean isinit ;

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private static boolean HideKeyboard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] location = {0, 0};
            view.getLocationInWindow(location);
            //获取现在拥有焦点的控件view的位置，即EditText
            int left = location[0], top = location[1], bottom = top + view.getHeight(), right = left + view.getWidth();
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            boolean isInEt = (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom);
            return !isInEt;
        }
        return false;
    }

    private OAuthServiceImp oAuthServiceImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        $Log("onCrete+++++");
        mInflater = LayoutInflater.from(mContext);
        setContentView(bindLayout());
        if (isSetStatusBar) {
            StatusBarCompat.setTranslucent(getWindow(), true);
            StatusBarUtil.setStatusTextColor(true, mActivity);
        }
//        StatusBarUtil.setStatusTextColor(true, mActivity);
//        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white), false);
        ButterKnife.bind(this);
        mBinder = ButterKnife.bind(this);
        isinit = true;
        openId = ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_OPENID);
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        View viewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        mStateView = StateView.inject(viewGroup);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.page_detail_error);

        }
        mStateView.setOnInflateListener(new StateView.OnInflateListener() {
            @Override
            public void onInflate(int viewType, View view) {
                if (viewType == StateView.RETRY){
                    ViewGroup empty = (ViewGroup) view;
                    ImageView error_back = empty.findViewById(R.id.error_back);
                    error_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                }
            }
        });



    }

    public void setmStateView(View view) {
        mStateView = StateView.inject(view);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.page_detail_error);
        }
    }

    @Override
    public void requestFial(int type, int code) {

    }
    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
    @Override
    public void networkLost() {
        Log.e(TAG, "networkLost: " );

    }

    @Override
    public void networkAvailable() {
        Log.e(TAG, "networkAvailable: ");

    }

    @Override
    public void connectError(int code, String message) {
        Log.e(TAG, "connectError: ");
        showErrorDialog(""+code,message);
    }

    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int bindLayout();

    /**
     * [初始化控件]
     *
     * @param view
     */
    public abstract void initView(final View view);

    /**
     * [绑定控件]
     *
     * @param resId
     * @return
     */
    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isHideBoa()) {
                View v = getCurrentFocus();
                //如果不是落在EditText区域，则需要关闭输入法
                if (HideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (v instanceof EditText) {
                        v.clearFocus();
                    }
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(RxEventType.TYPE_HIDE_KEYBOARD, null));
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isHideBoa() {
        return true;
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        $Log("onRestart+++++");
    }

    @Override
    protected void onStart() {
        super.onStart();
        $Log("onStart+++++");

    }

    @Override
    protected void onResume() {
        super.onResume();
        $Log("onResume+++++");
        MobclickAgent.onResume(this);
        if (isinit) {
            initView(mContextView);
            isinit = false;
            YSBSdk.addGrobalListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        $Log("onPause+++++");
        MobclickAgent.onPause(this);
//        YSBSdk.removeGrobalListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        $Log("onStop+++++");

    }

    @Override
    protected void onDestroy() {
        mBinder.unbind();
        super.onDestroy();
        $Log("onDestroy+++++");

    }

    public void showProgress() {
        rl_pb.setVisibility(View.VISIBLE);
    }

    public void showContent() {
        rl_pb.setVisibility(View.GONE);
    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRoate = isAllowScreenRoate;
    }


    /**
     * [日志输出]
     *
     * @param msg
     */
    protected void $Log(String msg) {
        Log.d("CustomerAppLogMassage", msg);
    }


    /**
     * 显示返回按钮
     */
    public void showTitleBackButton() {
        View vBack = findViewById(R.id.iv_back);
        if (vBack != null) {
            vBack.setVisibility(View.VISIBLE);
            vBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    TextView vText;
    /**
     * 显示左边文字
     * @param listener
     */
    public void showTitleLeftText(String text,View.OnClickListener listener){
        if (vText==null)
        vText=findViewById(R.id.tv_left);
        if (vText != null){
            vText.setText(text);
            vText.setVisibility(View.VISIBLE);
            vText.setOnClickListener(listener);
        }
    }
    /**
     * 显示/隐藏左边文字
     */
    public void showTitleLeftTextVisibility(boolean visible){
        if (vText==null)
        vText=findViewById(R.id.tv_left);
        View vBack = findViewById(R.id.iv_back);
        if (vText != null){
            if (visible) {
                vText.setVisibility(View.VISIBLE);
                vBack.setVisibility(View.GONE);
            }else {
                vText.setVisibility(View.GONE);
                vBack.setVisibility(View.VISIBLE);
            }
        }
    }
    public void showTitleBackButton(View.OnClickListener listener) {
        View vBack = findViewById(R.id.iv_back);
        if (vBack != null) {
            vBack.setVisibility(View.VISIBLE);
            vBack.setOnClickListener(listener);
        }
    }

    public void showTitleLeftBackButton(View.OnClickListener listener) {
//        View vBack = findViewById(R.id.bt_left);
//        if (vBack != null) {
//            vBack.setVisibility(View.VISIBLE);
//            vBack.setOnClickListener(listener);
//        }
    }

    public void showTitleRightBackButton(View.OnClickListener listener) {
//        View vBack = findViewById(R.id.bt_right);
//        if (vBack != null) {
//            vBack.setVisibility(View.VISIBLE);
//            vBack.setOnClickListener(listener);
//        }
    }

    public void showTitleRightBackButton(int drawable, View.OnClickListener listener) {
        ImageView vBack = findViewById(R.id.bt_right);
        if (vBack != null) {
            vBack.setImageResource(drawable);
            vBack.setVisibility(View.VISIBLE);
            vBack.setOnClickListener(listener);
        }
    }

    public void showTitleRightText(String text, View.OnClickListener listener) {
        TextView vText = findViewById(R.id.tv_right);
        if (vText != null) {
            vText.setText(text);
            vText.setVisibility(View.VISIBLE);
            vText.setOnClickListener(listener);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        TextView titleTxt = (TextView) findViewById(R.id.tv_author);
        if (titleTxt != null) {
            titleTxt.setText(title == null ? "" : title);
        }
        titleTxt.setVisibility(View.VISIBLE);
    }

    public void setTitleGone() {
//        TextView titleTxt = (TextView) findViewById(R.id.tv_author);
//        titleTxt.setVisibility(View.GONE);
    }
    public NoticeDialog mailDialog;
    public TextView commit;
    private int i =5;
    public void showErrorDialog(String code,String msg) {
//
//        final RelativeLayout mailBoxLay = (RelativeLayout) mInflater.inflate(R.layout.interface_error_dialog, null);
//        commit = mailBoxLay.findViewById(R.id.minute);
//        switch (code){
//            case "4401":
//                ((ImageView)mailBoxLay.findViewById(R.id.iv_top)).setImageResource(R.mipmap.information_4401);
//                break;
//            case "4402":
//                ((ImageView)mailBoxLay.findViewById(R.id.iv_top)).setImageResource(R.mipmap.information_4402);
//                break;
//            case "4403":
//                ((ImageView)mailBoxLay.findViewById(R.id.iv_top)).setImageResource(R.mipmap.information_4403);
//                break;
//            case "500":
//                ((ImageView)mailBoxLay.findViewById(R.id.iv_top)).setImageResource(R.mipmap.information_500);
//                break;
//        }
//        ((TextView)mailBoxLay.findViewById(R.id.tv_error)).setText("温馨提示("+code+")");
//        ((TextView)mailBoxLay.findViewById(R.id.tv_content)).setText(msg);
////        mailBoxLay.findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v){
////                handler.sendEmptyMessage(1);
////                mailDialog.dismiss();
//////                mStateView.showContent();
//////                MActivityManager.getInstance().delAllACTWithout(MainActivity.class);
////            }
////        });
//        mailDialog = new NoticeDialog(mContext);
//        mailDialog.showDialog(mailBoxLay, 0, 0);
//        i=5;
//        handler.sendEmptyMessage(0);
//        mailBoxDialog.setCancelable(false);
    }
//    //定时器
//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 0:
//                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
//                    handler.removeMessages(0);
//                    // app的功能逻辑处理
//                    i--;
//                    commit.setText(i+"秒");
//                    if (i>0) {
//                        // 再次发出msg，循环更新
//                        handler.sendEmptyMessageDelayed(0, 1000);
//                    }else {
//                        mailDialog.dismiss();
//                    }
//                    break;
//
//                case 1:
//                    // 直接移除，定时器停止
//                    handler.removeMessages(0);
//                    break;
//
//                default:
//                    break;
//            }
//        };
//    };
//
//    public void setSubmit(View.OnClickListener listener){
//        TextView submit = (TextView) findViewById(R.id.submit);
//        if (submit != null){
//            submit.setVisibility(View.VISIBLE);
//            submit.setOnClickListener(listener);
//        }
//    }

//    public void showEarlyDialog(Activity activity, final int CAMARA, final int PHOTO) {
//        verifyStoragePermissions(activity);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        LinearLayout mailBoxLay = (LinearLayout) inflater.inflate(
//                R.layout.activity_mail_box_dialog, null);
//        Button zy_btn = (Button) mailBoxLay.findViewById(R.id.zy_btn);
//        Button yd_btn = (Button) mailBoxLay.findViewById(R.id.yd_btn);
//        Button details = (Button) mailBoxLay.findViewById(R.id.details);
//        zy_btn.setText("拍照");
//        yd_btn.setText("相册");
//        details.setVisibility(View.VISIBLE);
//        final NoticeDialog mailBoxDialog = new NoticeDialog(this);
//        mailBoxDialog.showDialog(mailBoxLay, 0, 0);
//        zy_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mailBoxDialog.dismiss();
//                try{
//
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    // 启动相机
//                    startActivityForResult(intent1, CAMARA);
//                }catch (SecurityException e){
//                    Toast.makeText(mContext, "权限未开启", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//            }
//        });
//
//        yd_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mailBoxDialog.dismiss();
//                try{
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "选择图片"), PHOTO);
//                }catch (SecurityException e){
//                    Toast.makeText(mContext, "权限未开启", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mailBoxDialog.dismiss();
//            }
//        });
//
//    }


    public void verifyStoragePermissions(Activity activity) {
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 111);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }
    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(org.devio.takephoto.R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
}
