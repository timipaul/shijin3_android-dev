package com.hongchuang.ysblibrary.model.model.bean;

import java.util.Arrays;
import java.util.List;

/** 商城商品表 */
public class StoreGoodsBean {

    private String _id;
    //提交订单需要
    private String sku_id;
    //商品id
    private String goods_id;
    //商品名字
    private String name;
    //商品图片
    private String goodsImg;
    //礼品图片
    private String homeImg;
    //优惠前价格
    private double originalPrice;
    //优惠后价格
    private double price;
    private double discount;
    //商品销量
    private int goodsNum;
    //描述文字
    private String hintText;
    //已售出数量
    private int salesVolume;
    //详情图片
    private String[] detailsImg;
    //轮播图片
    private String[] coverImg;
    //多条描述文字
    private String[] banner;
    //产品规格
    private Specification[] attribute;
    //接收的规格
    private String attriValStr;
    //接收的规格价格
    private List<Sku> sku;
    //创建时间
    private String createdAt;

    //ID
    private String order_id;
    //订单号
    private String out_trade_no;
    //支付状态
    private String payState;
    //订单状态
    private String state;
    //快递类型
    private String expCode;
    //快递号码
    private String expNum;
    //邮费
    private String postage;



    //private String desc;
    private int num;
    private String color;
    private String size;
    private double discountPrice;
    protected boolean isChoosed;

    public StoreGoodsBean(){}


    public StoreGoodsBean(String goodsImg, double newPrice) {
        this.goodsImg = goodsImg;
        this.price = newPrice;
    }


    //产品规格选择
    public class  Specification {
        private String _id;
        private String key;
        private List<Val> val;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<Val> getVal() {
            return val;
        }

        public void setVal(List<Val> val) {
            this.val = val;
        }

    }

    public class Val{
        private String _id;
        private String text;
        private String state;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }


    }

    public class Sku{
        private String _id;
        private String attriValStr;
        private String coverImg;
        private String state;
        //库存
        private int stock;
        //规格价格
        private double price;
        //折扣价格
        private double discount;
        //sv值
        private int sv;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAttriValStr() {
            return attriValStr;
        }

        public void setAttriValStr(String attriValStr) {
            this.attriValStr = attriValStr;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getSv() {
            return sv;
        }

        public void setSv(int sv) {
            this.sv = sv;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String[] getDetailsImg() {
        return detailsImg;
    }

    public void setDetailsImg(String[] detailsImg) {
        this.detailsImg = detailsImg;
    }

    public String[] getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String[] coverImg) {
        this.coverImg = coverImg;
    }

    public String[] getBanner() {
        return banner;
    }

    public void setBanner(String[] banner) {
        this.banner = banner;
    }

    public Specification[] getAttribute() {
        return attribute;
    }

    public void setAttribute(Specification[] attribute) {
        this.attribute = attribute;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getAttriValStr() {
        return attriValStr;
    }

    public void setAttriValStr(String attriValStr) {
        this.attriValStr = attriValStr;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<Sku> getSku() {
        return sku;
    }

    public void setSku(List<Sku> sku) {
        this.sku = sku;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpNum() {
        return expNum;
    }

    public void setExpNum(String expNum) {
        this.expNum = expNum;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    @Override
    public String toString() {
        return "StoreGoodsBean{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", originalPrice=" + originalPrice +
                ", price=" + price +
                ", goodsNum=" + goodsNum +
                ", hintText='" + hintText + '\'' +
                ", salesVolume=" + salesVolume +
                ", detailsImg=" + Arrays.toString(detailsImg) +
                ", coverImg=" + Arrays.toString(coverImg) +
                ", banner=" + Arrays.toString(banner) +
                ", attribute=" + Arrays.toString(attribute) +
                ", num=" + num +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", discountPrice=" + discountPrice +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
