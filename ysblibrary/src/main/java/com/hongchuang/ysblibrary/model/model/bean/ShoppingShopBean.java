package com.hongchuang.ysblibrary.model.model.bean;


import java.util.Arrays;
import java.util.List;

/** 商城商铺表 */
public class ShoppingShopBean {

    //商户ID
    private String _id;
    //商铺图片
    private String imgUrl;
    //商铺名字
    private String name;
    //标签
    private String[] banner;
    //描述
    private String describe;

    //邮费
    private int postage_price;
    //商品信息
    private List<StoreGoodsBean> goodsList;
    //商品详情
    private StoreGoodsBean goods;

    protected boolean isChoosed;
    private boolean isEdtor;

    public ShoppingShopBean() { }

    public ShoppingShopBean(String shopId, String shopName) {
        this._id = shopId;
        this.name = shopName;
    }

    public String[] getBanner() {
        return banner;
    }

    public void setBanner(String[] banner) {
        this.banner = banner;
    }

    public List<StoreGoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<StoreGoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isEdtor() {
        return isEdtor;
    }

    public void setIsEdtor(boolean edtor) {
        isEdtor = edtor;
    }

    public int getPostage_price() {
        return postage_price;
    }

    public void setPostage_price(int postage_price) {
        this.postage_price = postage_price;
    }

    public void setEdtor(boolean edtor) {
        isEdtor = edtor;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public StoreGoodsBean getGoods() {
        return goods;
    }

    public void setGoods(StoreGoodsBean goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ShoppingShopBean{");
        sb.append("_id='").append(_id).append('\'');
        sb.append(", imgUrl='").append(imgUrl).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", shopLable=").append(banner == null ? "null" : Arrays.asList(banner).toString());
        sb.append(", describe='").append(describe).append('\'');
        sb.append(", postage_price=").append(postage_price);
        sb.append(", goodsList=").append(goodsList);
        sb.append(", goods=").append(goods);
        sb.append(", isChoosed=").append(isChoosed);
        sb.append(", isEdtor=").append(isEdtor);
        sb.append('}');
        return sb.toString();
    }
}
