package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/10/29.
 */

public class BasketBean {

    /**
     * Code : 100
     * Detail : {"sigSource":0}
     * Msg : pass_1
     * RequestId : 13973E1E-CD44-4067-BC70-973014F5DDA4
     * RiskLevel :
     */

    private String Code;
    private String Detail;
    private String Msg;
    private String RequestId;
    private String RiskLevel;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String Detail) {
        this.Detail = Detail;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }

    public String getRiskLevel() {
        return RiskLevel;
    }

    public void setRiskLevel(String RiskLevel) {
        this.RiskLevel = RiskLevel;
    }

}
