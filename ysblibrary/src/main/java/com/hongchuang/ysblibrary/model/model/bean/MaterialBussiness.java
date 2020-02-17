package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/9/26.
 */

public class MaterialBussiness {
    public String license;
    public String contact;
    public String telephone;

    public MaterialBussiness(String license, String contact, String telephone) {
        this.license = license;
        this.contact = contact;
        this.telephone = telephone;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
//
//    public String getId_face() {
//        return id_face;
//    }
//
//    public void setId_face(String id_face) {
//        this.id_face = id_face;
//    }
//
//    public String getId_back() {
//        return id_back;
//    }
//
//    public void setId_back(String id_back) {
//        this.id_back = id_back;
//    }
//
//    public String getId_handheld() {
//        return id_handheld;
//    }
//
//    public void setId_handheld(String id_handheld) {
//        this.id_handheld = id_handheld;
//    }
//
//    public String id_face;
//    public String id_back;
//    public String id_handheld;
}
