package com.alcwithgoogle.journalapp.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.alcwithgoogle.journalapp.Domain.Category;

import java.util.List;

/*
    author Taiwo Adebayo
 */

@Dao
public interface CategoryDao {

    @Insert
    long insert(Category category);


    @Query("SELECT * FROM category")
    List<Category> getAllCategory();

    @Delete
    int deleteAll(Category...categories);

}
