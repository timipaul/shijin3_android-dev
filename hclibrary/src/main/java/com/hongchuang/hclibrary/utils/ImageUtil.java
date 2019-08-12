package com.hongchuang.hclibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

//import static org.xutils.common.util.IOUtil.copy;

/***
 * 功能描述:图片处理工具
 * 作者:xiongning
 * 时间:2016/12/29
 ***/
public class ImageUtil {

//    /**
//     * 字符串生成二维码图
//     *
//     * @param mContext
//     * @param str
//     * @return
//     * @throws WriterException
//     */
//    public static Bitmap Create2DCode(Context mContext, String str) throws WriterException {
//
//        if (TextUtil.isEmpty(str)) {
//            return null;
//        }
//        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 450, 450);
//
//        int width = matrix.getWidth();
//
//        int height = matrix.getHeight();
//
//        //二维矩阵转为一维像素数组,也就是一直横着排了
//        int[] pixels = new int[width * height];
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = 0xff000000;
//                } else {
//                    pixels[y * width + x] = 0xffffffff;
//                }
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        //通过像素数组生成bitmap,具体参考api
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
//        return bitmap;
//    }

    /**
     * 获取图片URI实际目录
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 把网络资源图片转化成bitmap
     *
     * @param url 网络资源图片
     * @return Bitmap
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap;
        InputStream in;
        BufferedOutputStream out;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            //copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //象素数 / DPI = 英寸数
    //英寸数 * 25.4 = 毫米数
    public static double getMM(Context context, int px) {
        double result = 0;
        int DPI = DevicesUtil.getDPI(context);
        result = px / DPI * 25.4;
        return result;
    }

    public static double getPx(Context context, int mm) {
        return mm * 25.4 * DevicesUtil.getDPI(context);
    }

    /**
     * get the video thumbnail from server or local
     * can only invoke this method on the child thread
     *
     * @param url video path
     * @return
     */
    public static void createVideoThumbnail(String url, CallBack callBack) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            getBitmapFromUrl(url, callBack);
        } else {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MINI_KIND);
            if (callBack != null) {
                callBack.onSuccess(bitmap);
            }
        }
    }

    @Nullable
    private static void getBitmapFromUrl(final String url, final CallBack callBack) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    if (Build.VERSION.SDK_INT >= 14) {
                        retriever.setDataSource(url, new HashMap<String, String>());
                    } else {
                        retriever.setDataSource(url);
                    }
                    bitmap = retriever.getFrameAtTime();
                } catch (Exception ex) {
                    // Assume this is a corrupt video file
                    ex.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                        // Ignore failures while cleaning up.
                    }
                }
                subscriber.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        if (bitmap != null) {
                            if (callBack != null) {
                                callBack.onSuccess(bitmap);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onError();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callBack != null) {
                            callBack.onError();
                        }
                    }
                });
    }

    public interface CallBack {
        void onSuccess(Bitmap bitmap);

        void onError();
    }

}
