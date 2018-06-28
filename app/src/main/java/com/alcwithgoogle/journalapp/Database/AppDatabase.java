package com.alcwithgoogle.journalapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.alcwithgoogle.journalapp.Dao.CategoryDao;
import com.alcwithgoogle.journalapp.Dao.JournalDao;
import com.alcwithgoogle.journalapp.Domain.Category;
import com.alcwithgoogle.journalapp.Domain.Journal;

/*
    author Taiwo Adebayo
 */

@Database(entities = {Journal.class, Category.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DB_NAME = "journal_db";

    public abstract JournalDao getJournalDao();

    public abstract CategoryDao getCategoryDao();
}
