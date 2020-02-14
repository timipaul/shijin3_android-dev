package com.hongchuang.ysblibrary.model.model.bean;

/** 商城评论实体类 */
public class StroeCommentBean {
    private String id;
    //用户名
    private String name;
    //头像
    private String img;
    //评论时间
    private String comment_time;
    //规格
    private String goods_measure;
    //内容
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getGoods_measure() {
        return goods_measure;
    }

    public void setGoods_measure(String goods_measure) {
        this.goods_measure = goods_measure;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StroeCommentBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", comment_time='" + comment_time + '\'' +
                ", goods_measure='" + goods_measure + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
