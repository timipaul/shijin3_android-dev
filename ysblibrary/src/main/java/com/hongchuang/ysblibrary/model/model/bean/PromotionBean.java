package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Created by yrdan on 2018/8/10.
 */

public class PromotionBean {
    public UserPromotion getUser_promotion() {
        return user_promotion;
    }

    public void setUser_promotion(UserPromotion user_promotion) {
        this.user_promotion = user_promotion;
    }

    public UserPromotion user_promotion;
    public class UserPromotion{
        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Material getMaterial() {
            return material;
        }

        public void setMaterial(Material material) {
            this.material = material;
        }

        public String getPromotion_status() {
            return promotion_status;
        }

        public void setPromotion_status(String promotion_status) {
            this.promotion_status = promotion_status;
        }

        public String user_id;
        public String mode;

        public String getAdvertiser_mode() {
            return advertiser_mode;
        }

        public void setAdvertiser_mode(String advertiser_mode) {
            this.advertiser_mode = advertiser_mode;
        }

        public String advertiser_mode;
        public Material material;
        public class Material{
            public String id_face;

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

            public String getTelphone() {
                return telphone;
            }

            public void setTelphone(String telphone) {
                this.telphone = telphone;
            }

            public String id_back;
            public String id_handheld;
            public String license;
            public String contact;
            public String telphone;
        }
        public String promotion_status;

        public String getCheck_info() {
            return check_info;
        }

        public void setCheck_info(String check_info) {
            this.check_info = check_info;
        }

        public String check_info;
    }
}
