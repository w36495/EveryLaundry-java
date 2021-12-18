package com.w36495.everylaundry.domain;

import com.google.gson.annotations.SerializedName;

/**
 * 카테고리 데이터 클래스
 */
public class Category {

    @SerializedName("CATEGORY_KEY")
    private int categoryKey;

    @SerializedName("CATEGORY_TITLE")
    private String categoryTitle;

    @SerializedName("REG_DT")
    private String categoryRegistDate;

    @SerializedName("UPD_DT")
    private String categoryUpdateDate;

    public Category(int categoryKey, String categoryTitle, String categoryRegistDate, String categoryUpdateDate) {
        this.categoryKey = categoryKey;
        this.categoryTitle = categoryTitle;
        this.categoryRegistDate = categoryRegistDate;
        this.categoryUpdateDate = categoryUpdateDate;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(int categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryRegistDate() {
        return categoryRegistDate;
    }

    public void setCategoryRegistDate(String categoryRegistDate) {
        this.categoryRegistDate = categoryRegistDate;
    }

    public String getCategoryUpdateDate() {
        return categoryUpdateDate;
    }

    public void setCategoryUpdateDate(String categoryUpdateDate) {
        this.categoryUpdateDate = categoryUpdateDate;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryKey=" + categoryKey +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", categoryRegistDate='" + categoryRegistDate + '\'' +
                ", categoryUpdateDate='" + categoryUpdateDate + '\'' +
                '}';
    }
}
