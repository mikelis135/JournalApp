package com.alcwithgoogle.journalapp.Domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


/*
   @author Taiwo Adebayo
 */

@Entity(tableName = "category")
public class Category {

    //initializing the category table name column fields
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    private long id;
    @ColumnInfo(name = "name")
    private String name;

    //getters and setters of the category table column fields

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    }
