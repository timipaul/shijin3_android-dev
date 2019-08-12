package com.hongchuang.ysblibrary.model.model;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by yrdan on 2018/8/10.
 */

public interface OssCallback {
    void  onSuccess(PutObjectRequest request, PutObjectResult result);
    void  onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);
}
