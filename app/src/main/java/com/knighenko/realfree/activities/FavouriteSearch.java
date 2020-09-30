package com.knighenko.realfree.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import com.knighenko.realfree.service.ServerService;

public class FavouriteSearch extends AppCompatActivity {
    private Toolbar toolbar;
    private SQLiteDatabase myDB;
    private SwitchCompat switchCompat1;
    private SwitchCompat switchCompat2;
    private SwitchCompat switchCompat3;
    private SwitchCompat switchCompat4;
    private SwitchCompat switchCompat5;
    private SwitchCompat switchCompat6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_search);
        this.toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Мониторинг рубрик");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_home_garden);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_business_services);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_electronics);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_fashion_style);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_hobbies_leisure);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_transport_parts);
        switchCompat1.setOnCheckedChangeListener(createListener());
    }

    /**
     * Метод создает листенер для всех SwitchCompat
     */
    private CompoundButton.OnCheckedChangeListener createListener() {
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch_home_garden:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
                            startTracking(UrlOfPages.HOME_GARDEN);
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.HOME_GARDEN.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_business_services:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle(), UrlOfPages.BUSINESS_AND_SERVICES.getUrl(), myDB);
                            startTracking(UrlOfPages.BUSINESS_AND_SERVICES);
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_electronics:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.ELECTRONICS.getTitle(), UrlOfPages.ELECTRONICS.getUrl(), myDB);
                            startTracking(UrlOfPages.ELECTRONICS);
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.ELECTRONICS.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_fashion_style:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.FASHION_AND_STYLE.getTitle(), UrlOfPages.FASHION_AND_STYLE.getUrl(), myDB);
                            startTracking(UrlOfPages.FASHION_AND_STYLE);
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.FASHION_AND_STYLE.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_hobbies_leisure:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle(), UrlOfPages.HOBBIES_AND_LEISURE.getUrl(), myDB);
                            startTracking(UrlOfPages.HOBBIES_AND_LEISURE);
                        }

                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_transport_parts:
                        if (isChecked == true) {
                            createDB();
                            addToDB(UrlOfPages.TRANSPORT_PARTS.getTitle(), UrlOfPages.TRANSPORT_PARTS.getUrl(), myDB);
                            startTracking(UrlOfPages.TRANSPORT_PARTS);
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.TRANSPORT_PARTS.getTitle(), myDB);
                        }
                        break;
                }
            }
        };
        return listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * Метод запускает сервис по отслеживанию новых обьявлений из заданной рубрики
     */
    public void startTracking(final UrlOfPages object) {

        Intent myIntent = new Intent(this, ServerService.class);
        myIntent.putExtra("url", object.getUrl());
        myIntent.putExtra("titleOfUrl", object.getTitle());
        ContextCompat.startForegroundService(this, myIntent);

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
    private void deleteFromDB(String title, SQLiteDatabase myDB) {

        myDB.delete("favourite_search",
                "title = \'" + title + "\'", null);

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
