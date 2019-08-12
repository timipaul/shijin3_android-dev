package com.hongchuang.hclibrary.view.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.hongchuang.hclibrary.view.banner.transformer.AccordionTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.BackgroundToForegroundTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.CubeInTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.CubeOutTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.DefaultTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.DepthPageTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.FlipHorizontalTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.FlipVerticalTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.ForegroundToBackgroundTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.RotateDownTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.RotateUpTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.ScaleInOutTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.StackTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.TabletTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.ZoomInTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.ZoomOutSlideTransformer;
import com.hongchuang.hclibrary.view.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
