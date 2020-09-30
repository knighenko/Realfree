package com.knighenko.realfree.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.knighenko.realfree.R;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.UrlOfPages;

public class FavouriteSearch extends AppCompatActivity {
    private Toolbar toolbar;
    private SQLiteDatabase myDB;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_search);
        this.toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Мониторинг рубрик");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        switchCompat = (SwitchCompat) findViewById(R.id.switch_home_garden);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    System.out.println("Switch State=" + isChecked);
                    createDB();
                    addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
                }
                if (isChecked==false){

                }
                System.out.println("Is in database: " + checkInDB(UrlOfPages.HOME_GARDEN.getTitle()));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    /**
     * Метод создает базу данных или открывает созданную + создает таблицу advertisement
     */
    private void createDB() {
        myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS favourite_search ( title TEXT, url TEXT)");

    }

    /**
     * Метод добавляет элементы в базу данных
     */
    private void addToDB(String title, String url, SQLiteDatabase myDB) {
        ContentValues row = new ContentValues();
        row.put("title", title);
        row.put("url", url);
        myDB.insert("favourite_search", null, row);

    }
    /**
     * Метод удаляет элементы из базу данных
     */
    private void deleteFromDB(String title,  SQLiteDatabase myDB) {
        ContentValues row = new ContentValues();
        row.put("title", title);
        myDB.insert("favourite_search", null, row);

    }

    /**
     * Метод читает из базы данных избранные рубрики для поиска
     */
    private boolean checkInDB(String title) {
        Cursor cursor = myDB.rawQuery("SELECT EXISTS (select url from  favourite_search WHERE title = \'" + title + "\'  LIMIT 1)", null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 1) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

}