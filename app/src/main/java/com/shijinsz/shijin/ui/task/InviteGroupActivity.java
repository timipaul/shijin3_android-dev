package com.shijinsz.shijin.ui.task;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongchuang.hclibrary.permission.MPermission;
import com.hongchuang.hclibrary.storage.ShareDataManager;
import com.hongchuang.hclibrary.storage.SharedPreferencesKey;
import com.hongchuang.hclibrary.utils.TextUtil;
import com.hongchuang.ysblibrary.common.toast.ToastUtil;
import com.hongchuang.ysblibrary.utils.QRCodeUtil;
import com.shijinsz.shijin.R;
import com.shijinsz.shijin.base.BaseActivity;
import com.shijinsz.shijin.base.Comment;
import com.shijinsz.shijin.utils.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yrdan on 2018/9/4.
 */

public class InviteGroupActivity extends BaseActivity {
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.fm)
    FrameLayout fm;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.rl)
    LinearLayout rl;

    @Override
    public int bindLayout() {
        return R.layout.invite_group_activity;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setStatusTextColor(true, mActivity);
        showTitleBackButton();
        setTitle(getString(R.string.invite_group));
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(Comment.url+"group_invitation?nickname="+ ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_nickname)+"&username="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_USER_NAME)+"&imageurl="+ShareDataManager.getInstance().getPara(SharedPreferencesKey.KEY_imageurl)+"&channel=group", 390);
        code.setImageBitmap(bitmap);
    }


    @OnClick({R.id.tv_save, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                generateBitmap(rl,mActivity);
                break;
            case R.id.tv_next:
                startActivity(InviteGroupActivity2.class);
                break;
        }
    }
    public static final int WRITE_REQUEST_CODE = 100;
    // 权限控制  读取权限
    public static final String[] WRITE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    public void generateBitmap(LinearLayout mContentRootView, Activity activity) {
        //    mStateView.showContent();
        if (MPermission.havePermission(activity, MPermission.Type.PERMISSION_STORAGE)) {
            //    LoadingDialog.Build(activity).setContent("正在保存").show();
            Bitmap bitmap = Bitmap.createBitmap(mContentRootView.getWidth(),
                    mContentRootView.getHeight()
                    , Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            mContentRootView.draw(canvas);
            //     final String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera" + "/IMG_" + System.currentTimeMillis() + ".png";
            //  final String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".png";
            if (TextUtil.isNotEmpty(saveBitmapToLocal(bitmap, activity))) {
                ToastUtil.showToast("保存成功");
            } else {
                ToastUtil.showToast("保存失败");
            }


        } else {
            MPermission.with(activity).addRequestCode(WRITE_REQUEST_CODE).permissions(WRITE_PERMISSIONS).request();
        }
    }
    /**
     * 保存图像到本地
     */
    public String saveBitmapToLocal(Bitmap bm, String filepath, boolean isBroadcast) {
        String path;
        // final String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".png";
        try {
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
            if (isBroadcast) {
                MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        bm, filepath, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                mContext.sendBroadcast(intent);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } finally {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();  //回收图片所占的内存
                System.gc();  //提醒系统及时回收
            }
        }

        return path;
    }

    /**
     * 保存图像到本地
     */
    public String saveBitmapToLocal(Bitmap bm, Activity activity) {

        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + System.currentTimeMillis() + ".png";
        String path = saveBitmapToLocal(bm, filepath, true);
        //      Context context = null;
//        String path = BitmapUtil.saveBitmapToFile(bm,System.currentTimeMillis()+"",100,false,mContext);
//        Observable.timer(1500, TimeUnit.MILLISECONDS, Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
        // LoadingDialog.dismissDialog();

//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
        return path;
    }
}
