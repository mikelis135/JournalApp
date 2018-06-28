package com.alcwithgoogle.journalapp.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alcwithgoogle.journalapp.Adapter.CategoryAdapter;
import com.alcwithgoogle.journalapp.Database.AppDatabase;
import com.alcwithgoogle.journalapp.Domain.Category;
import com.alcwithgoogle.journalapp.Domain.CategoryJournal;
import com.alcwithgoogle.journalapp.Domain.Journal;
import com.alcwithgoogle.journalapp.R;

import java.util.List;


/*
    author Taiwo Adebayo
 */

public class EditJounalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String EXTRA_Journal_ID = "EXTRA_Journal_ID";
    private Long JournalId;
    private AppDatabase db;
    private TextView title;
    private TextView description;
    private TextView category;
    private Spinner categorySpinner;
    private Journal Journal;
    private CategoryAdapter adapter;
    private Button save;
    private int success = 0;
    private TextInputLayout textInputLayout;
    private TextInputLayout textInputLayout2;

    public static Bundle newInstanceBundle(long JournalId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_Journal_ID, JournalId);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jounal);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_Journal_ID)) {

            setTitle("Edit Journal");
            JournalId = intent.getLongExtra(EXTRA_Journal_ID, -1);

             TextView  description = findViewById(R.id.description);

        }

        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        db = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).build();
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        save  = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveJournal();
            }
        });

        categorySpinner = findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void loadJournal() {
        new AsyncTask<Long, Void, CategoryJournal>() {
            @Override
            protected CategoryJournal doInBackground(Long... params) {
                return db.getJournalDao().getCategoryJournal(params[0]);
            }

            @Override
            protected void onPostExecute(CategoryJournal Journal) {
                setJournal(Journal);
            }
        }.execute(JournalId);
    }

    private void setJournal(Journal Journal) {
        this.Journal = Journal;
        title.setText(Journal.getTitle());
        description.setText(Journal.getDescription());
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                categorySpinner.setVisibility(View.INVISIBLE);
                Log.e("tyope", "type");
            }

            @Override
            public void afterTextChanged(final Editable editable) {

            }
        });
        int categoryPosition = adapter.getCategoryPosition(Journal.getCategoryId());
        if (categoryPosition > 0) {
            categorySpinner.setSelection(categoryPosition);
        }
    }

    private void loadCategories() {
        new AsyncTask<Void, Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... params) {
                return db.getCategoryDao().getAllCategory();
            }

            @Override
            protected void onPostExecute(List<Category> adapterCategories) {
                setCategories(adapterCategories);
                if (JournalId != null) {
                    loadJournal();
                }
            }
        }.execute();
    }

    private void setCategories(List<Category> categories) {
        adapter = new CategoryAdapter(this, android.R.layout.simple_list_item_1, categories);
        categorySpinner.setAdapter(adapter);
    }

    private void saveJournal() {

        if (Journal == null) {
            Journal = new Journal();
        }
        if (title.getText().toString().trim().equalsIgnoreCase("")){
           // title.setHint("You forgot the title");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textInputLayout2.setTooltipText("You forgot the description");
            }
            success = 0;
        } else{
            Journal.setTitle(title.getText().toString().trim());
            success = 1;
        }

        if (description.getText().toString().trim().equalsIgnoreCase("")){
            //description.setHint("You forgot the description");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textInputLayout.setTooltipText("You forgot the description");
            }
            success = 0;
        }else {
            Journal.setDescription(description.getText().toString().trim());
            success = 1;
        }

        if (success==1) {
            final String categoryName = category.getText().toString().trim();
            Category selectedCategory = adapter.getItem(categorySpinner.getSelectedItemPosition());
            final Long selectedCategoryId = (selectedCategory == null || selectedCategory.getId() <= 0) ? null : selectedCategory.getId();

            new AsyncTask<Journal, Void, Void>() {
                @Override
                protected Void doInBackground(Journal... params) {
                    Journal saveJournal = params[0];
                    if (selectedCategoryId != null) {
                        saveJournal.setCategoryId(selectedCategoryId);
                    } else if (categoryName.length() > 0) {
                        Category category = new Category();
                        category.setName(categoryName);
                        long categoryId = db.getCategoryDao().insert(category);
                        saveJournal.setCategoryId(categoryId);
                    } else {
                        Journal.setCategoryId(null);
                    }
                    if (saveJournal.getId() > 0) {
                        db.getJournalDao().updateAll(saveJournal);
                    } else {
                        db.getJournalDao().insertAll(saveJournal);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    setResult(RESULT_OK);
                    finish();
                }
            }.execute(Journal);
        }
    }
}
