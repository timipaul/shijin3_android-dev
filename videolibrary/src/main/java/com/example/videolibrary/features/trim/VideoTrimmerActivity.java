package com.example.videolibrary.features.trim;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.videolibrary.R;
import com.example.videolibrary.interfaces.TrimVideoListener;

import com.example.videolibrary.widget.VideoTrimmerView;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;




/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoTrimmerActivity extends AppCompatActivity implements TrimVideoListener {

  private static final String TAG = "jason";
  private static final String VIDEO_PATH_KEY = "video-file-path";
  public static final int VIDEO_TRIM_REQUEST_CODE = 0x001;
  private ProgressDialog mProgressDialog;

  public static void call(FragmentActivity from, String videoPath) {
    if (!TextUtils.isEmpty(videoPath)) {
      Bundle bundle = new Bundle();
      bundle.putString(VIDEO_PATH_KEY, videoPath);
      Intent intent = new Intent(from, VideoTrimmerActivity.class);
      intent.putExtras(bundle);
      from.startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
    }
  }
  private VideoTrimmerView trimmerView;

  @Override protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_trimmer_layout);
    Bundle bd = getIntent().getExtras();
    String path = "";
    if (bd != null) path = bd.getString(VIDEO_PATH_KEY);
    trimmerView=findViewById(R.id.trimmer_view);
    if (trimmerView != null) {
      trimmerView.setOnTrimVideoListener(this);
      trimmerView.initVideoByURI(Uri.parse(path));
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();
    trimmerView.onVideoPause();
    trimmerView.setRestoreState(true);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    trimmerView.onDestroy();
  }

  @Override public void onStartTrim() {
    buildDialog(getResources().getString(R.string.trimming),0).show();
  }

  @Override public void onFinishTrim(String in) {
//    FileUtil.deleteFile(in);
    if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
    Intent intent = new Intent();
    intent.putExtra("url",in);
    setResult(RESULT_OK,intent);
    finish();
//    String out = Environment.getExternalStorageDirectory()+"/compress.mp4";
//    mProgressDialog.dismiss();
//    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//    mProgressDialog.setMessage(getString(R.string.compressing));
//    mProgressDialog.setMax(100);
//    mProgressDialog.setProgress(0);
//    mProgressDialog.show();
////    buildDialog(getResources().getString(R.string.compressing),0).show();
//    MediaPlayer mediaPlayer = new MediaPlayer();
//    try {
//      mediaPlayer.setDataSource(in);
//      mediaPlayer.prepare();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    int time =mediaPlayer.getDuration();
//
//
//    CompressVideoUtil.compress(this, in, out, new CompressVideoListener() {
//      @Override public void onSuccess(String message) {
//      }
//
//      @Override public void onFailure(String message) {
//      }
//
//      @Override public void onFinish() {
//        FileUtil.deleteFile(in);
//        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
//        Intent intent = new Intent();
//        intent.putExtra("url",out);
//        setResult(RESULT_OK,intent);
//        finish();
//      }
//
//      @Override
//      public void onProgress(String message) {
//        super.onProgress(message);
//        pro=Integer.parseInt(message);
//        mProgressDialog.setProgress(Integer.parseInt(message));
//      }
//    },time);
  }

  @Override public void onCancel() {
    trimmerView.onDestroy();
    finish();
  }

  @Override
  public void onError() {
    mProgressDialog.setProgress(0);
    mProgressDialog.dismiss();
    Toast.makeText(this, "视频剪辑失败，请重新尝试", Toast.LENGTH_SHORT).show();
  }

  private int pro=0;
  @Override
  public void onProgress(int progress) {
      pro=progress;
      mProgressDialog.setProgress(progress);
  }

  private ProgressDialog buildDialog(String msg,int progress) {
    if (mProgressDialog == null) {
      mProgressDialog =new ProgressDialog(this);
    }
    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    mProgressDialog.setMax(100);
    mProgressDialog.setProgress(progress);
    mProgressDialog.setMessage(msg);
    mProgressDialog.setCancelable(false);
    mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
      }
    });

    mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        if (pro<100)
        FFmpeg.getInstance(VideoTrimmerActivity.this).killRunningProcesses();
      }
    });
    return mProgressDialog;
  }
}
