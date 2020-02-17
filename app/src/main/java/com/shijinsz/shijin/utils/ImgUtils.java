package com.shijinsz.shijin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yrdan on 2018/9/17.
 */

public class ImgUtils {
    /**
     * 保存文件到指定路径
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp){
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "dearxy.jpg";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        try {
            FileOutputStream fos = new FileOutputStream(appDir);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
//            Uri uri = Uri.fromFile(file);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存图像到本地
     */
    public static String saveBitmapToLocal(Bitmap bm, String filepath) {
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
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } finally {
//            if (bm != null && !bm.isRecycled()) {
//                bm.recycle();  //回收图片所占的内存
//                System.gc();  //提醒系统及时回收
//            }
        }

        return path;
    }

    /**
     * 保存图像到本地
     */
    public static String saveBitmapToLocal(Context context,Bitmap bm) {

        String filepath = context.getCacheDir().getAbsolutePath() + "/ad.png";
        String path = saveBitmapToLocal(bm, filepath);
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