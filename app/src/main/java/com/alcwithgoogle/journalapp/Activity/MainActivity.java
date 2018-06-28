package com.alcwithgoogle.journalapp.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import com.alcwithgoogle.journalapp.Adapter.JournalAdapter;
import com.alcwithgoogle.journalapp.Database.AppDatabase;
import com.alcwithgoogle.journalapp.Domain.CategoryJournal;
import com.alcwithgoogle.journalapp.Domain.Journal;
import com.alcwithgoogle.journalapp.R;

import java.util.List;

/*
    author Taiwo Adebayo
 */


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, JournalAdapter.ActionCallBack {

    private  static AppDatabase appDatabase;
    private static JournalAdapter journalAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).build();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditJounalActivity.class));
            }
        });

        journalAdapter = new JournalAdapter(this);

        //setting up the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(journalAdapter);


        //swipe to delete journal interaction
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteJournal(journalAdapter.getJournal(viewHolder.getAdapterPosition()));
            }
        };

        //attach swipe event to recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onEdit(CategoryJournal journal) {
        Intent intent = new Intent(this, EditJounalActivity.class);
        intent.putExtras(EditJounalActivity.newInstanceBundle(journal.getId()));
        startActivity(intent);

    }

    private static void loadJournals() {
        new AsyncTask<Void, Void, List<CategoryJournal>>() {
            @Override
            protected List<CategoryJournal> doInBackground(Void... params) {
                return appDatabase.getJournalDao().getCategoryjournals();
            }

            @Override
            protected void onPostExecute(List<CategoryJournal> notes) {
                journalAdapter.setNotes(notes);
            }
        }.execute();
    }

    private static void deleteJournal(Journal journal) {
        new AsyncTask<Journal, Void, Void>() {
            @Override
            protected Void doInBackground(Journal... params) {
                appDatabase.getJournalDao().deleteAllJournal(params);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadJournals();
            }
        }.execute(journal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJournals();
    }
}
