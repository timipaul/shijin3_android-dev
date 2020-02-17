package com.hongchuang.ysblibrary.qrcode;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hongchuang.hclibrary.R;
import com.hongchuang.ysblibrary.qrcode.camera.CameraManager;
import com.hongchuang.ysblibrary.qrcode.decoding.CaptureActivityHandler;
import com.hongchuang.ysblibrary.qrcode.decoding.InactivityTimer;
import com.hongchuang.ysblibrary.qrcode.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;


/***
 * 功能描述: 二维码
 * 作者:huangyongcan
 * 时间:2016/8/16
 ***/
public abstract class BaseQRCodeActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //    x.view().inject(this);
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        //  initEvints();
    }

    public abstract void scanResult(String resultString);

    public abstract SurfaceView getSurfaceView();

    public abstract ViewfinderView getViewfinder();

    public abstract void initEvints();

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        scanResult(resultString);
        handler.removeMessages(R.id.restart_preview);
        handler.sendEmptyMessageDelayed(R.id.restart_preview, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = getSurfaceView().getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if ((audioService != null ? audioService.getRingerMode() : 0) != AudioManager.RINGER_MODE_NORMAL) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return getViewfinder();
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        getViewfinder().drawViewfinder();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

}
