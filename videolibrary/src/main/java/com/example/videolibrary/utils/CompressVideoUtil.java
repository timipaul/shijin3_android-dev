package com.example.videolibrary.utils;

import android.content.Context;
import android.util.Log;

import com.example.videolibrary.interfaces.CompressVideoListener;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/03/16/3:23 PM
 * version: 1.0
 * description:
 */
public class CompressVideoUtil {
  //ffmpeg -y -i input.mp4 -strict -2 -vcodec libx264  -preset ultrafast -crf 24 -acodec copy -ar 44100 -ac 2 -b:a 12k -s 640x352 -aspect 16:9 output.mp4

  public static void compress(Context context, String inputFile, String outputFile, final CompressVideoListener callback,long time) {
    String cmd = "-threads 2 -y -i " + inputFile + " -strict -2 -vcodec libx264 -preset ultrafast -crf 28 -acodec copy -ac 2 " + outputFile;
    String[] command = cmd.split(" ");
    try {
      FFmpeg.getInstance(context).execute(command, new ExecuteBinaryResponseHandler() {
        @Override
        public void onFailure(String msg) {
          callback.onFailure("Compress video failed!");
          callback.onFinish();
        }

        @Override
        public void onSuccess(String msg) {
          callback.onSuccess("Compress video successed!");
          callback.onFinish();
        }

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
                      Float.parseFloat(matchSplit[2])) / (time/1000);
              int showProgress = (int) (progress * 100);
              Log.e("Compress", "CompressOnProgress: "+showProgress);
              if(showProgress>100){
                showProgress = 100;
              }
              callback.onProgress(showProgress+"");
            }
          }

          Log.e("TAG", "onProgress: "+message);
        }
      });
    } catch (FFmpegCommandAlreadyRunningException e) {
      e.printStackTrace();
      callback.onFailure("压缩失败,请重试");
    }
  }
}
