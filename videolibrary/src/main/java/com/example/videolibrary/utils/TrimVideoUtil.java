package com.example.videolibrary.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.example.videolibrary.interfaces.TrimVideoListener;
import com.example.videolibrary.models.VideoInfo;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import iknow.android.utils.DeviceUtil;
import iknow.android.utils.UnitConverter;
import iknow.android.utils.callback.SimpleCallback;
import iknow.android.utils.callback.SingleCallback;
import iknow.android.utils.thread.BackgroundExecutor;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class TrimVideoUtil {

  private static final String TAG = TrimVideoUtil.class.getSimpleName();
  public static final long MIN_SHOOT_DURATION = 3000L;// 最小剪辑时间3s
  public static final int VIDEO_MAX_TIME = 180;// 10秒
  public static final long MAX_SHOOT_DURATION = VIDEO_MAX_TIME * 1000L;//视频最多剪切多长时间10s
  public static final int MAX_COUNT_RANGE = 10;  //seekBar的区域内一共有多少张图片
  private static final int SCREEN_WIDTH_FULL = DeviceUtil.getDeviceWidth();
  public static final int RECYCLER_VIEW_PADDING = UnitConverter.dpToPx(35);
  public static final int VIDEO_FRAMES_WIDTH = SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2;
  private static final int THUMB_WIDTH = (SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2) / VIDEO_MAX_TIME;
  private static final int THUMB_HEIGHT = UnitConverter.dpToPx(50);

  public static void trim(Context context, String inputFile, String outputFile, long startMs, long endMs, final TrimVideoListener callback) {
    final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    final String outputName = "trimmedVideo_" + timeStamp + ".mp4";
    outputFile = outputFile + "/" + outputName;
    String[] a=inputFile.split("\\.");
    String start = convertSecondsToTime(startMs / 1000);
    float time =(endMs-startMs)/1000;
    String duration = convertSecondsToTime((endMs - startMs) / 1000);
    //String start = String.valueOf(startMs);
    //String duration = String.valueOf(endMs - startMs);

    /** 裁剪视频ffmpeg指令说明：
     * ffmpeg -ss START -t DURATION -i INPUT -codec copy -avoid_negative_ts 1 OUTPUT
     -ss 开始时间，如： 00:00:20，表示从20秒开始；
     -t 时长，如： 00:00:10，表示截取10秒长的视频；
     -i 输入，后面是空格，紧跟着就是输入视频文件；
     -codec copy -avoid_negative_ts 1 表示所要使用的视频和音频的编码格式，这里指定为copy表示原样拷贝；
     INPUT，输入视频文件；
     OUTPUT，输出视频文件
     */
    //String cmd = "-ss " + start + " -t " + duration + " -accurate_seek" + " -i " + inputFile + " -codec copy -avoid_negative_ts 1 " + outputFile;
    String cmd;
    //https://superuser.com/questions/138331/using-ffmpeg-to-cut-up-video
    if (a[a.length-1].equals("mp4")) {
      cmd = "-ss " + start + " -i " + inputFile + " -ss " + start + " -t " + duration + " -c copy -avoid_negative_ts 1 " + outputFile;
    }else {
      cmd = "-ss " + start + " -i " + inputFile + " -ss " + start + " -t " + duration + " " + outputFile;
    }
    String[] command = cmd.split(" ");
    try {
      final String tempOutFile = outputFile;
      FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {
        @Override
        public void onProgress(String message) {
          super.onProgress(message);
          Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
          Scanner sc = new Scanner(message);
          String match = sc.findWithinHorizon(timePattern, 0);
          if (match != null) {
            String[] matchSplit = match.split(":");
            if (time!= 0) {
              float progress = (Integer.parseInt(matchSplit[0]) * 3600 +
                      Integer.parseInt(matchSplit[1]) * 60 +
                      Float.parseFloat(matchSplit[2])) / time;
              int showProgress = (int) (progress * 100);
              Log.e(TAG, "onProgress: "+showProgress);
              if(showProgress>100){
                showProgress = 100;
              }
              callback.onProgress(showProgress);
            }
          }

        }

        @Override public void onSuccess(String s) {
          callback.onFinishTrim(tempOutFile);
        }

        @Override public void onStart() {
          callback.onStartTrim();
        }

        @Override
        public void onFailure(String message) {
          super.onFailure(message);
          Log.e(TAG, "onFailure: "+message );
        }
      });
    } catch (FFmpegCommandAlreadyRunningException e) {
      e.printStackTrace();
      callback.onError();
    }
  }

  public static void backgroundShootVideoThumb(final Context context, final Uri videoUri, final int totalThumbsCount, final long startPosition,
      final long endPosition, final SingleCallback<Bitmap, Integer> callback) {
    BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
      @Override public void execute() {
        try {
          MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
          mediaMetadataRetriever.setDataSource(context, videoUri);
          // Retrieve media data use microsecond
          long interval = (endPosition - startPosition) / (totalThumbsCount - 1);
          for (long i = 0; i < totalThumbsCount; ++i) {
            long frameTime = startPosition + interval * i;
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            try {
              bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_WIDTH, THUMB_HEIGHT, false);
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            }
            callback.onSingleCallback(bitmap, (int) interval);
          }
          mediaMetadataRetriever.release();
        } catch (final Throwable e) {
          Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
        }
      }
    });
  }

  @SuppressLint("CheckResult")
  public static void loadAllVideoFiles(final Context mContext, final SimpleCallback simpleCallback) {
    Observable.create((ObservableOnSubscribe<List<VideoInfo>>) emitter -> {
      List<VideoInfo> videos = new ArrayList<>();
      try {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
          while (cursor.moveToNext()) {
            VideoInfo videoInfo = new VideoInfo();
            if (cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)) != 0) {
              videoInfo.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
              videoInfo.setVideoPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
              videoInfo.setCreateTime(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
              videoInfo.setVideoName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
              videos.add(videoInfo);
            }
          }
          cursor.close();
        }
        emitter.onNext(videos);
      } catch (Throwable t) {
        emitter.onError(t);
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(videoInfos -> {
      if (simpleCallback != null) simpleCallback.success(videoInfos);
    }, throwable -> Log.e(TAG, throwable.getMessage()));
  }

  public static String getVideoFilePath(String url) {
    if (TextUtils.isEmpty(url) || url.length() < 5) return "";
    if (url.substring(0, 4).equalsIgnoreCase("http")) {

    } else {
      url = "file://" + url;
    }

    return url;
  }

  private static String convertSecondsToTime(long seconds) {
    String timeStr = null;
    int hour = 0;
    int minute = 0;
    int second = 0;
    if (seconds <= 0) {
      return "00:00";
    } else {
      minute = (int) seconds / 60;
      if (minute < 60) {
        second = (int) seconds % 60;
        timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
      } else {
        hour = minute / 60;
        if (hour > 99) return "99:59:59";
        minute = minute % 60;
        second = (int) (seconds - hour * 3600 - minute * 60);
        timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
      }
    }
    return timeStr;
  }

  private static String unitFormat(int i) {
    String retStr = null;
    if (i >= 0 && i < 10) {
      retStr = "0" + Integer.toString(i);
    } else {
      retStr = "" + i;
    }
    return retStr;
  }
}
