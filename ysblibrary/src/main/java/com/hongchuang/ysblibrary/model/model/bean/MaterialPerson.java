package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/26.
 */

public class MaterialPerson {
    public MaterialPerson(String id_face, String id_back, String id_handheld) {
        this.id_face = id_face;
        this.id_back = id_back;
        this.id_handheld = id_handheld;
    }

    public String getId_face() {
        return id_face;
    }

    public void setId_face(String id_face) {
        this.id_face = id_face;
    }

    public String getId_back() {
        return id_back;
    }

    public void setId_back(String id_back) {
        this.id_back = id_back;
    }

    public String getId_handheld() {
        return id_handheld;
    }

    public void setId_handheld(String id_handheld) {
        this.id_handheld = id_handheld;
    }

    public String id_face;
    public String id_back;
    public String id_handheld;
}
