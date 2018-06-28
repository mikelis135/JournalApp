package com.alcwithgoogle.journalapp.Domain;


import android.support.annotation.Nullable;

/**
 * @author Taiwo Adebayo
 */

    // CategoryJournal extends category Base Class with Category model

    public class CategoryJournal extends Journal{

    //getter and setter for the categoryName
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
