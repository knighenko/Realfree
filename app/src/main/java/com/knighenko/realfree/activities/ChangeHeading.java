package com.knighenko.realfree.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.knighenko.realfree.R;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.UrlOfPages;

public class ChangeHeading extends AppCompatActivity {
    private Toolbar toolbar;
    private SQLiteDatabase myDB;
    private Chip chip;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_heading);
        this.toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Выбор рубрик для мониторинга");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * Обработка нажатия на кнопку выбора
     */
    public void changeBtn(View view) {
   byte count=0;
        createDB();
        if (((Chip) (findViewById(R.id.chip_home_garden_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }
        if (((Chip) (findViewById(R.id.electronics_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }
        if (((Chip) (findViewById(R.id.transport_parts_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }
        if (((Chip) (findViewById(R.id.business_services_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }
        if (((Chip) (findViewById(R.id.fashion_style_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }
        if (((Chip) (findViewById(R.id.hobbies_leisure_change))).isChecked()) {
            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
            count++;
        }


        Toast.makeText(this, "Добавлено в поиск "+count+" рубрик", Toast.LENGTH_SHORT).show();

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
     * Метод читает из базы данных избранные рубрики для поиска
     */
    private boolean checkInDB(String title) {
        Cursor cursor = myDB.rawQuery("SELECT EXISTS (select * from  advertisement WHERE title = \'" + title + "\'  LIMIT 1)", null);
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