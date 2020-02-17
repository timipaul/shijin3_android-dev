package com.hongchuang.ysblibrary.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/29.
 */

@Entity( nameInDb = "wuche_base_province" ,createInDb = false)
public class ProvinceBean {
    public String FID;
    public String FNAME;
    public String CFAREAIDID;
    public String CFSTATE;
    @Generated(hash = 944434688)
    public ProvinceBean(String FID, String FNAME, String CFAREAIDID,
            String CFSTATE) {
        this.FID = FID;
        this.FNAME = FNAME;
        this.CFAREAIDID = CFAREAIDID;
        this.CFSTATE = CFSTATE;
    }
    @Generated(hash = 1410713511)
    public ProvinceBean() {
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    public String getFNAME() {
        return this.FNAME;
    }
    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }
    public String getCFAREAIDID() {
        return this.CFAREAIDID;
    }
    public void setCFAREAIDID(String CFAREAIDID) {
        this.CFAREAIDID = CFAREAIDID;
    }
    public String getCFSTATE() {
        return this.CFSTATE;
    }
    public void setCFSTATE(String CFSTATE) {
        this.CFSTATE = CFSTATE;
    }

}