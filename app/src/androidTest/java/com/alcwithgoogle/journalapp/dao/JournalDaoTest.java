package com.alcwithgoogle.journalapp.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alcwithgoogle.journalapp.Dao.CategoryDao;
import com.alcwithgoogle.journalapp.Dao.JournalDao;
import com.alcwithgoogle.journalapp.Database.AppDatabase;
import com.alcwithgoogle.journalapp.Domain.Journal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author acampbell
 */
@RunWith(AndroidJUnit4.class)
public class JournalDaoTest {

    private JournalDao JournalDao;
    private CategoryDao categoryDao;
    private AppDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        JournalDao = db.getJournalDao();
        categoryDao = db.getCategoryDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void shouldCreateDatabase() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(JournalDao);
        assertNotNull(categoryDao);
    }

    @Test
    public void shouldInsertJournal() {
        Journal Journal = new Journal();
        Journal.setTitle("name1");
        Journal.setDescription("description1");
        JournalDao.insertAll(Journal);
        List<Journal> Journals = JournalDao.getAll();

        assertEquals(1, Journals.size());
        Journal dbJournal = Journals.get(0);
        assertEquals(Journal.getTitle(), dbJournal.getTitle());
        assertEquals(Journal.getDescription(), dbJournal.getDescription());
        assertEquals(1, dbJournal.getId());
    }

    @Test
    public void shouldInsertTwoJournals() {
        Journal Journal1 = new Journal();
        Journal1.setTitle("name1");
        Journal1.setDescription("description1");

        Journal Journal2 = new Journal();
        Journal2.setTitle("name1");
        Journal2.setDescription("description2");

        JournalDao.insertAll(Journal1, Journal2);
        List<Journal> Journals = JournalDao.getAll();

        assertEquals(2, Journals.size());
        Journal dbJournal1 = Journals.get(0);
        assertEquals(Journal1.getTitle(), dbJournal1.getTitle());
        assertEquals(Journal1.getDescription(), dbJournal1.getDescription());
        assertEquals(1, dbJournal1.getId());

        Journal dbJournal2 = Journals.get(1);
        assertEquals(Journal2.getTitle(), dbJournal2.getTitle());
        assertEquals(Journal2.getDescription(), dbJournal2.getDescription());
        assertEquals(2, dbJournal2.getId());
    }

    @Test
    public void shouldInsertJournalWithId() {
        Journal Journal = new Journal();
        Journal.setId(99);
        JournalDao.insertAll(Journal);
        List<Journal> Journals = JournalDao.getAll();
        assertEquals(1, Journals.size());
        Journal dbJournal = Journals.get(0);
        assertEquals(Journal.getId(), dbJournal.getId());
    }

    @Test
    public void shouldDeleteJournal() {
        Journal Journal = new Journal();
        Journal.setTitle("name1");
        JournalDao.insertAll(Journal);
        List<Journal> Journals = JournalDao.getAll();

        assertEquals(1, Journals.size());
        JournalDao.deleteAllJournal(Journals.get(0));
        Journals = JournalDao.getAll();
        assertEquals(0, Journals.size());
    }

    @Test
    public void shouldUpdateJournal() {
        Journal Journal = new Journal();
        Journal.setTitle("name1");
        JournalDao.insertAll(Journal);
        List<Journal> Journals = JournalDao.getAll();

        assertEquals(1, Journals.size());
        Journal dbJournal = Journals.get(0);
        assertEquals(Journal.getTitle(), dbJournal.getTitle());

        dbJournal.setTitle("name2");
        JournalDao.updateAll(dbJournal);
        Journals = JournalDao.getAll();
        assertEquals(1, Journals.size());
        Journal dbJournal2 = Journals.get(0);
        assertEquals(dbJournal.getTitle(), dbJournal2.getTitle());
    }

}