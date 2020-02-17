package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/* 商城分类 三级分类 */
public class GoodsClassifyBean {

    private String _id;
    private String chName;
    private String type;
    private List<Child> child;

    //点击
    private boolean isClick;

    //二级分类
    public static class Child {
        private String _id;
        private String chName;
        private List<ThreeChild> child;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getChName() {
            return chName;
        }

        public void setChName(String chName) {
            this.chName = chName;
        }

        //三级分类
        public class ThreeChild {

            private String _id;
            private String chName;
            private String coverImg;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getChName() {
                return chName;
            }

            public void setChName(String chName) {
                this.chName = chName;
            }

            public String getCoverImg() {
                return coverImg;
            }

            public void setCoverImg(String coverImg) {
                this.coverImg = coverImg;
            }


        }

        public List<ThreeChild> getChild() {
            return child;
        }

        public void setChild(List<ThreeChild> child) {
            this.child = child;
        }

        @Override
        public String toString() {
            return "Child{" +
                    "_id='" + _id + '\'' +
                    ", chName='" + chName + '\'' +
                    '}';
        }
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
