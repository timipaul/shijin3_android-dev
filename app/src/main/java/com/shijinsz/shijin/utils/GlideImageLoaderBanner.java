package com.shijinsz.shijin.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hongchuang.hclibrary.view.banner.loader.ImageLoader;


/**
 * Created by Administrator on 2017/12/29.
 */

public class GlideImageLoaderBanner extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);


    }
}
