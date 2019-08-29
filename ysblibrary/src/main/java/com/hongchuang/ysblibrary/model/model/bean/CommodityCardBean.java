package com.hongchuang.ysblibrary.model.model.bean;

/**
 * Copyright (C)
 * FileName: CommodityCardBean
 * Author: Administrator
 * Date: 2019/8/22 0022 上午 10:13
 * Description: 商品兑换卡
 */
public class CommodityCardBean {

    private String cardId;
    //价格
    private String price;
    //图片
    private String imgUrl;
    //分类名称
    private String categoryName;
    //数量
    private String num;
    //邮费
    private String postage;

    private String attach;

    //public AttachBean attach;

    public class AttachBean{
        private String name;
        private String address;
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "AttachBean{" +
                    "name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    public CommodityCardBean(){};

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
