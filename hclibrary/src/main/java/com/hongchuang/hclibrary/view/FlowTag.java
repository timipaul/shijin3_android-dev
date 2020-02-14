package com.hongchuang.hclibrary.view;

/**
 * 用于搜索
 */
public class FlowTag {

    private int id;//id
    private String title;//标签上的文字


    public FlowTag(int _id, String _title){
        this.setId(_id);
        this.setTitle(_title);
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
