package com.hongchuang.ysblibrary.model.model.bean;

import java.util.List;

/**
 * Created by yrdan on 2018/9/4.
 */

public class RecomWordsBean {
    public String default_word;

    public String getDefault_word() {
        return default_word;
    }

    public void setDefault_word(String default_word) {
        this.default_word = default_word;
    }

    public List<String> getRecom_words() {
        return recom_words;
    }

    public void setRecom_words(List<String> recom_words) {
        this.recom_words = recom_words;
    }

    public List<String> recom_words;
}
