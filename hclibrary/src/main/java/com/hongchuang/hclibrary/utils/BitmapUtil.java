package com.hongchuang.hclibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2017/8/18
 ***/

public class BitmapUtil {
    /**
     * Insert an image and create a thumbnail for it.
     *
     * @param cr          The content resolver to use
     * @param imagePath   The path to the image to insert
     * @param name        The name of the image
     * @param description The description of the image
     * @return The URL to the newly created image
     * @throws FileNotFoundException
     */
    public static String insertImage(ContentResolver cr, String imagePath,
                                     String name, String description) throws FileNotFoundException {
        // Check if file exists with a FileInputStream
        FileInputStream stream = new FileInputStream(imagePath);
        try {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);

            //将得到的照片进行270°旋转，使其竖直
//            Matrix matrix = new Matrix();
//            matrix.preRotate(270);
//            bm = Bitmap.createBitmap(bm ,0,0, bm .getWidth(), bm.getHeight(),matrix,true);
            String ret = insertImage(cr, bm, name, description);
            bm.recycle();
            return ret;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Insert an image and create a thumbnail for it.
     * jliu : 对源码作过修改，加入了时间信息，默认插入时间为当前时间
     *
     * @param cr          The content resolver to use
     * @param source      The stream to use for the image
     * @param title       The name of the image
     * @param description The description of the image
     * @return The URL to the newly created image, or <code>null</code> if the image failed to be stored
     * for any reason.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String insertImage(ContentResolver cr, Bitmap source,
                                     String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()); // DATE HERE
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                try (OutputStream imageOut = cr.openOutputStream(url)) {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String insertImage1(ContentResolver cr, Bitmap source,
                                      String title, String description, Activity activity) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()); // DATE HERE
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                try (OutputStream imageOut = cr.openOutputStream(url)) {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        } finally {
            if (source != null && !source.isRecycled()) {
                source.recycle();  //回收图片所占的内存
                System.gc();  //提醒系统及时回收
            }
        }

        if (url != null) {
            stringUrl = ImageUtil.getRealFilePath(activity, url);
        }

        return stringUrl;
    }

    private static Bitmap StoreThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width, float height,
            int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);

            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public static String saveBitmapToFile(Bitmap mBitmap, String fileName, int quality, boolean isPng, Context mContext) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        file.mkdirs();// 创建文件夹
        fileName = file + fileName;//图片名字
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(fileName);
            if (isPng) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, quality, b);// 把数据写入文件
            } else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, b);// 把数据写入文件
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                mBitmap, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        mContext.sendBroadcast(intent);
        return fileName;
    }

    public static String saveBitmapToFile(Context context, Bitmap mBitmap, String fileName, int quality, boolean isPng) {
        File file = new File(context.getCacheDir().getAbsolutePath() + "/image");
        file.mkdirs();// 创建文件夹
        fileName = context.getCacheDir().getAbsolutePath() + "/image" + File.separator + fileName;//图片名字
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(fileName);
            if (isPng) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, quality, b);// 把数据写入文件
            } else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, b);// 把数据写入文件
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
