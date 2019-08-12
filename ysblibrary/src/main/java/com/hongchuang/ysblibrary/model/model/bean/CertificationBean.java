package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/26.
 */

public class CertificationBean {
    public String mode;

    public CertificationBean(String mode, Object material) {
        this.mode = mode;
        this.material = material;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Object getMaterial() {
        return material;
    }

    public void setMaterial(Object material) {
        this.material = material;
    }

    public Object material;
}
