package com.hongchuang.ysblibrary.utils.entity;

/***
 * 功能描述:分享的实体
 * 作者:qiujialiu
 * 时间:2017/7/6
 ***/

public class ShareBean {


    public Type type;//分享类型

    public String content_;// (string, optional): 内容 ,

    public String img_;// (string, optional): 头像，图片 ,

    public String title_;// (string, optional): 标题 ,

    public String url_;// (string, optional): 分享的url

    public String from_;

    public String main_img_;

//    public String share_url_;

    public enum Type {
        TYPE_URL, TYPE_IMAGE, TYPE_TEXT
    }
}
