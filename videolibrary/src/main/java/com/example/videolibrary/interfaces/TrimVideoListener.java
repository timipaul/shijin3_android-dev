package com.example.videolibrary.interfaces;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public interface TrimVideoListener {
    void onStartTrim();
    void onFinishTrim(String url);
    void onCancel();
    void onProgress(int progress);
    void onError();
}
