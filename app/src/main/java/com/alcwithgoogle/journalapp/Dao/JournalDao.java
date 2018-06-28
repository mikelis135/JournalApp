package com.alcwithgoogle.journalapp.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import com.alcwithgoogle.journalapp.Domain.CategoryJournal;
import com.alcwithgoogle.journalapp.Domain.Journal;

import java.util.List;

/*
    author Taiwo Adebayo
 */

@Dao
public interface JournalDao {

    @Insert
    void insertAll(Journal...journals);
    
    @Update
    void updateAll(Journal...journals);

    @Query("SELECT * FROM journal")
    List<Journal> getAll();


    @Query("SELECT journal.id, journal.title, journal.description, category.name as categoryName, journal.category_id " +
            "FROM journal " +
            "LEFT JOIN category ON journal.category_id = category.id")
    List<CategoryJournal>   getCategoryjournals();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT journal.id, journal.title, journal.description, journal.category_id " +
            "FROM journal " +
            "LEFT JOIN category ON journal.category_id = category.id " +
            "WHERE journal.id = :journalId")
    CategoryJournal getCategoryJournal(long journalId);

    @Delete
    void deleteAllJournal(Journal...journals);
}
