package com.hongchuang.ysblibrary.photovideo.bean;

import java.util.ArrayList;
import java.util.List;

/***
 * 功能描述:(图片上传Recycler的实体类)
 * 作者:qiujialiu
 * 时间:2016/12/21
 * 版本:1.0
 ***/

public class UploadFileBean {
    public String url_;
    public int type_;//1图片2视频
    public int seq_;
    // private String type;//类型 视频:video  图片:image 添加更多:other
    //private String path;//地址
    private int status;//状态 0:默认 1:待上传 2:开始上传  3:上传中 4:上传失败
    private boolean finishDelete;//上传完成是否删除
    private double process;
    private int sort;
    private String forderPath;

    public UploadFileBean(int type) {
        this.type_ = type;
    }

    public UploadFileBean(int type, String path) {
        this.type_ = type;
        this.url_ = path;
    }

    public UploadFileBean(int type, String path, boolean finishDelete) {
        this.type_ = type;
        this.url_ = path;
        this.finishDelete = finishDelete;
    }

    public UploadFileBean() {
    }

    public static ArrayList<UploadFileBean> create(List<String> paths) {
        ArrayList<UploadFileBean> beans = new ArrayList<>();
        if (paths != null && paths.size() > 0) {
            for (String path : paths) {
                beans.add(new UploadFileBean(1, path));
            }
        }
        return beans;
    }

    public String getUrl_() {
        return url_;
    }

    public void setUrl_(String url_) {
        this.url_ = url_;
    }

    public int getType_() {
        return type_;
    }

    public void setType_(int type_) {
        this.type_ = type_;
    }

    public int getSeq_() {
        return seq_;
    }

    public void setSeq_(int seq_) {
        this.seq_ = seq_;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFinishDelete() {
        return finishDelete;
    }

    public void setFinishDelete(boolean finishDelete) {
        this.finishDelete = finishDelete;
    }

    public double getProcess() {
        return process;
    }

    public void setProcess(double process) {
        this.process = process;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getForderPath() {
        return forderPath;
    }

    public void setForderPath(String forderPath) {
        this.forderPath = forderPath;
    }

//    public static ArrayList<String> from(List<UploadFileBean> paths) {
//        ArrayList<String> beans = new ArrayList<>();
//        if (paths != null && paths.size() > 0) {
//            for (UploadFileBean path : paths) {
//                beans.add(path.getUrl_());
//            }
//        }
//        return beans;
//    }

//    public static boolean haveVideo(List<UploadFileBean> paths) {
//        if (paths != null && paths.size() > 0) {
//            for (UploadFileBean path : paths) {
//                if (path.getType() == 2) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


}
