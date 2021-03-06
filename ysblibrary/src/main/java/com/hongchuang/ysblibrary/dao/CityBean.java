package com.hongchuang.ysblibrary.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/29.
 */

@Entity(nameInDb = "wuche_base_city", createInDb = false)
public class CityBean {
    public String FLONGNAME;
    public String FID;
    public String FNAME;
    public String CFPROVINCEIDID;
    public String CFSTATE;
    public String CFCITYLEVEL;
    public String CFHKLEVEL;
    public String CFKJLEVEL;
    public String CFPJLEVEL;
    @Generated(hash = 852754634)
    public CityBean(String FLONGNAME, String FID, String FNAME,
            String CFPROVINCEIDID, String CFSTATE, String CFCITYLEVEL,
            String CFHKLEVEL, String CFKJLEVEL, String CFPJLEVEL) {
        this.FLONGNAME = FLONGNAME;
        this.FID = FID;
        this.FNAME = FNAME;
        this.CFPROVINCEIDID = CFPROVINCEIDID;
        this.CFSTATE = CFSTATE;
        this.CFCITYLEVEL = CFCITYLEVEL;
        this.CFHKLEVEL = CFHKLEVEL;
        this.CFKJLEVEL = CFKJLEVEL;
        this.CFPJLEVEL = CFPJLEVEL;
    }
    @Generated(hash = 273649691)
    public CityBean() {
    }
    public String getFLONGNAME() {
        return this.FLONGNAME;
    }
    public void setFLONGNAME(String FLONGNAME) {
        this.FLONGNAME = FLONGNAME;
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
    public String getCFPROVINCEIDID() {
        return this.CFPROVINCEIDID;
    }
    public void setCFPROVINCEIDID(String CFPROVINCEIDID) {
        this.CFPROVINCEIDID = CFPROVINCEIDID;
    }
    public String getCFSTATE() {
        return this.CFSTATE;
    }
    public void setCFSTATE(String CFSTATE) {
        this.CFSTATE = CFSTATE;
    }
    public String getCFCITYLEVEL() {
        return this.CFCITYLEVEL;
    }
    public void setCFCITYLEVEL(String CFCITYLEVEL) {
        this.CFCITYLEVEL = CFCITYLEVEL;
    }
    public String getCFHKLEVEL() {
        return this.CFHKLEVEL;
    }
    public void setCFHKLEVEL(String CFHKLEVEL) {
        this.CFHKLEVEL = CFHKLEVEL;
    }
    public String getCFKJLEVEL() {
        return this.CFKJLEVEL;
    }
    public void setCFKJLEVEL(String CFKJLEVEL) {
        this.CFKJLEVEL = CFKJLEVEL;
    }
    public String getCFPJLEVEL() {
        return this.CFPJLEVEL;
    }
    public void setCFPJLEVEL(String CFPJLEVEL) {
        this.CFPJLEVEL = CFPJLEVEL;
    }
}