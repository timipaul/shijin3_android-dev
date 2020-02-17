package com.hongchuang.ysblibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import com.hongchuang.ysblibrary.utils.entity.ShareBean;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit.callback.YRequestCallback;

//import com.tencent.connect.share.QQShare;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXImageObject;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;


/***
 * 功能描述:分享
 * 作者:xiongning
 * 时间:2017/01/23
 * 版本:1.0
 ***/

public class ThirdShareManager {
    private static ThirdShareManager instance;
    //private Context mContext;

    private ThirdShareManager() {
    }

    public static ThirdShareManager getInstance() {
        if (instance == null) {
            instance = new ThirdShareManager();
        }
        return instance;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

//    /**
//     * 发短信
//     * @param content
//     */
//    public void sendSms(String content,String phone){
//        String smsBody = content;
//        Uri smsToUri = Uri.parse( "smsto:" );
//        //一般调起短信 用 Intent.ACTION_SENDTO；
//        Intent sendIntent =  new  Intent(Intent.ACTION_SENDTO, smsToUri);
//        if(TextUtil.isNotEmpty(phone)){
//            sendIntent.putExtra("address", phone); // 电话号码，这行去掉的话，默认就没有电话
//        }
//        //短信内容
//        sendIntent.putExtra( "sms_body", smsBody);
//        //Android 6.0 以下是没问题的，但android 6.0 以后不再被官方支持，就会导致 activity not found，
//        //   sendIntent.setType( "vnd.android-dir/mms-sms" );
//        YunShlApplication.getInstance().getTopActivity().startActivityForResult(sendIntent, 1002);
//    }

//    /**
//     * 把网络资源图片转化成bitmap
//     * @param url  网络资源图片
//     * @return  Bitmap
//     */
//    public static Bitmap GetLocalOrNetBitmap(String url) {
//        Bitmap bitmap;
//        InputStream in;
//        BufferedOutputStream out;
//        try {
//            in = new BufferedInputStream(new URL(url).openStream(), 1024);
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//            out = new BufferedOutputStream(dataStream, 1024);
//            copy(in, out);
//            out.flush();
//            byte[] data = dataStream.toByteArray();
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 短信分享
     *
     * @param mContext
     * @param smstext  短信分享内容
     * @return
     */
    public Boolean sendSms(Context mContext, String smstext) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smstext);
        mContext.startActivity(mIntent);
        return null;
    }

    public void shareToQQ(Activity activity, ShareBean mShareBean, final YRequestCallback callback) {
//        if (mShareBean != null) {
//            try {
//                final Bundle params = new Bundle();
//
//                if (mShareBean.type == ShareBean.Type.TYPE_IMAGE) {
//                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, mShareBean.img_);
//                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//                } else {
//                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//                    if (TextUtil.isNotEmpty(mShareBean.title_)) {
//                        params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareBean.title_);
//                    }
//                    if (TextUtil.isNotEmpty(mShareBean.content_)) {
//                        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareBean.content_);
//                    }
//                    if (TextUtil.isNotEmpty(mShareBean.url_)) {
//                        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareBean.url_);
//                    }
//                    if (TextUtil.isNotEmpty(mShareBean.img_)) {
//                        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareBean.img_);
//                    }
//                }
//                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
//                YSBLibrary.getLibrary().getTencent().shareToQQ(activity, params, new IUiListener() {
//                    @Override
//                    public void onComplete(Object o) {
//                        LogUtils.d("-----", " onComplete ");
//                        if (callback != null) {
//                            callback.onSuccess(null);
//                        }
//                    }
//
//                    @Override
//                    public void onError(UiError uiError) {
//                        LogUtils.d("-----", " onError ");
//                        if (callback != null) {
//                            callback.onFailed("", "分享失败" + uiError.errorMessage);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        LogUtils.d("-----", " onCancel ");
//                        if (callback != null) {
//                            callback.onFailed("", "分享失败");
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                if (callback != null) {
//                    callback.onFailed("", "分享失败");
//                }
//            }
//        } else {
//            if (callback != null) {
//                callback.onFailed("", "分享失败");
//            }
//        }
    }

    public void shareToWeChat(final ShareBean mShareBean, final boolean isWeChat, final YRequestCallback callback) {
//        if (mShareBean == null) {
//            if (callback != null) {
//                callback.onFailed("", "分享失败");
//            }
//            return;
//        }
//        Observable.create(new Observable.OnSubscribe<WXMediaMessage>() {
//            @Override
//            public void call(Subscriber<? super WXMediaMessage> subscriber) {
//
//                if (mShareBean.type == ShareBean.Type.TYPE_IMAGE) {
//                    Bitmap bitmap = null;
//                    if (mShareBean.img_.startsWith("http://") || mShareBean.img_.startsWith("https://")) {
//                        bitmap = GetLocalOrNetBitmap(mShareBean.img_);
//                    } else {
//                        bitmap = BitmapFactory.decodeFile(mShareBean.img_);
//                    }
//                    WXImageObject imageObject = new WXImageObject(bitmap);
//                    WXMediaMessage msg = new WXMediaMessage(imageObject);
//                    msg.title = mShareBean.title_;
//                    msg.description = mShareBean.content_;
//                    subscriber.onNext(msg);
//                } else {
//                    WXWebpageObject webpage = new WXWebpageObject();
//                    webpage.webpageUrl = mShareBean.url_;
//                    WXMediaMessage msg = new WXMediaMessage(webpage);
//                    msg.title = mShareBean.title_;
//                    msg.description = mShareBean.content_;
//                    //图文详情类的分享，缩略图不要超过32KB，否则可能分享失败
//                    if (TextUtil.isNotEmpty(mShareBean.img_)) {
//                        Bitmap thumbBmp = Bitmap.createScaledBitmap(GetLocalOrNetBitmap(mShareBean.img_), 120, 120, true);//压缩Bitmap
//                        msg.thumbData = bmpToByteArray(thumbBmp, true);
//                    } else {
//                        Bitmap bmp = BitmapFactory.decodeResource(YSBLibrary.getLibrary().getContext().getResources(), R.drawable.shape_toastutils_bg);
//                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 120, 120, true);
//                        bmp.recycle();
//                        msg.thumbData = bmpToByteArray(thumbBmp, true);
//                    }
//                    subscriber.onNext(msg);
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<WXMediaMessage>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (callback != null) {
//                            callback.onFailed("", "分享失败");
//                        }
//                    }
//
//                    @Override
//                    public void onNext(WXMediaMessage obj) {
//                        SendMessageToWX.Req req = new SendMessageToWX.Req();
//                        req.transaction = buildTransaction("webpage");
//                        req.message = obj;
//                        if (isWeChat) {
//                            req.scene = SendMessageToWX.Req.WXSceneSession;
//                        } else {
//                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
//                        }
//                        boolean isSuccess = YSBLibrary.getLibrary().getWeChat().sendReq(req);
//
//                        if (!isSuccess) {
//                            if (callback != null) {
//                                callback.onFailed("", "分享失败");
//                            }
//                        }
//                    }
//                });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
