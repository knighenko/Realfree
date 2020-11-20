package com.knighenko.realfree.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
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

import java.util.ArrayList;

public class FavouriteSearch extends AppCompatActivity {
    private Toolbar toolbar;
    private SQLiteDatabase myDB;
    private SwitchCompat switchCompat1;
    private SwitchCompat switchCompat2;
    private SwitchCompat switchCompat3;
    private SwitchCompat switchCompat4;
    private SwitchCompat switchCompat5;
    private SwitchCompat switchCompat6;
    private SwitchCompat switchCompat7;
    private SwitchCompat switchCompat8;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_search);
        this.toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Мониторинг рубрик");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createDB();
        switchCompat1 = (SwitchCompat) findViewById(R.id.switch_home_garden);
        switchCompat1.setOnCheckedChangeListener(createListener());
        switchCompat1.setChecked(checkInDB(UrlOfPages.HOME_GARDEN.getTitle()));
        switchCompat2 = (SwitchCompat) findViewById(R.id.switch_business_services);
        switchCompat2.setOnCheckedChangeListener(createListener());
        switchCompat2.setChecked(checkInDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle()));
        switchCompat3 = (SwitchCompat) findViewById(R.id.switch_electronics);
        switchCompat3.setOnCheckedChangeListener(createListener());
        switchCompat3.setChecked(checkInDB(UrlOfPages.ELECTRONICS.getTitle()));
        switchCompat4 = (SwitchCompat) findViewById(R.id.switch_fashion_style);
        switchCompat4.setOnCheckedChangeListener(createListener());
        switchCompat4.setChecked(checkInDB(UrlOfPages.FASHION_AND_STYLE.getTitle()));
        switchCompat5 = (SwitchCompat) findViewById(R.id.switch_hobbies_leisure);
        switchCompat5.setOnCheckedChangeListener(createListener());
        switchCompat5.setChecked(checkInDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle()));
        switchCompat6 = (SwitchCompat) findViewById(R.id.switch_transport_parts);
        switchCompat6.setOnCheckedChangeListener(createListener());
        switchCompat6.setChecked(checkInDB(UrlOfPages.TRANSPORT_PARTS.getTitle()));
        switchCompat7 = (SwitchCompat) findViewById(R.id.switch_world_of_children);
        switchCompat7.setOnCheckedChangeListener(createListener());
        switchCompat7.setChecked(checkInDB(UrlOfPages.WORLD_OF_CHILDREN.getTitle()));
        switchCompat8 = (SwitchCompat) findViewById(R.id.switch_zooproduct);
        switchCompat8.setOnCheckedChangeListener(createListener());
        switchCompat8.setChecked(checkInDB(UrlOfPages.ZOOPRODUCT.getTitle()));
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
                            if (!checkInDB(UrlOfPages.HOME_GARDEN.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.HOME_GARDEN.getTitle(), UrlOfPages.HOME_GARDEN.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.HOME_GARDEN.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_business_services:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle(), UrlOfPages.BUSINESS_AND_SERVICES.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.BUSINESS_AND_SERVICES.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_electronics:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.ELECTRONICS.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.ELECTRONICS.getTitle(), UrlOfPages.ELECTRONICS.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.ELECTRONICS.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_fashion_style:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.FASHION_AND_STYLE.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.FASHION_AND_STYLE.getTitle(), UrlOfPages.FASHION_AND_STYLE.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.FASHION_AND_STYLE.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_hobbies_leisure:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle(), UrlOfPages.HOBBIES_AND_LEISURE.getUrl(), myDB);
                            }
                        }

                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.HOBBIES_AND_LEISURE.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_transport_parts:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.TRANSPORT_PARTS.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.TRANSPORT_PARTS.getTitle(), UrlOfPages.TRANSPORT_PARTS.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.TRANSPORT_PARTS.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_world_of_children:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.WORLD_OF_CHILDREN.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.WORLD_OF_CHILDREN.getTitle(), UrlOfPages.WORLD_OF_CHILDREN.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.WORLD_OF_CHILDREN.getTitle(), myDB);
                        }
                        break;
                    case R.id.switch_zooproduct:
                        if (isChecked == true) {
                            if (!checkInDB(UrlOfPages.ZOOPRODUCT.getTitle())) {
                                createDB();
                                addToDB(UrlOfPages.ZOOPRODUCT.getTitle(), UrlOfPages.ZOOPRODUCT.getUrl(), myDB);
                            }
                        }
                        if (isChecked == false) {
                            deleteFromDB(UrlOfPages.ZOOPRODUCT.getTitle(), myDB);
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
    public void startTracking(ArrayList<String> strings) {

            Intent myIntent = new Intent(getApplicationContext(), ServerService.class);
            myIntent.putStringArrayListExtra("advertisements",strings);
             this.startService(myIntent);

    }

    /**
     * Метод создает базу данных или открывает созданную + создает таблицу advertisement
     */
    private void createDB() {
        myDB = openOrCreateDatabase("myNew.db", MODE_PRIVATE, null);
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
     * Метод читает из базы данных избранные рубрики для поиска (проверка на наличие в базе)
     */
    private boolean checkInDB(String title) {
        Cursor cursor = myDB.rawQuery("SELECT EXISTS (select title from  favourite_search WHERE title = \'" + title + "\'  LIMIT 1)", null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 1) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * Метод запускает и останавливает мониторинг обьявление
     */
    private void startOrStopTracking() {
        ArrayList<String> strings = readFromDBFavourite();

            startTracking(strings);
       if (isMyServiceRunning(ServerService.class) & strings.size() == 0) {
            stopTracking();
        }
    }

    /**
     * Метод читает из базы данных избранные рубрики для поиска
     */
    private ArrayList<String> readFromDBFavourite() {
        ArrayList<String> strings = new ArrayList<String>();
        Cursor cursor = myDB.rawQuery("select url from  favourite_search", null);
        while (cursor.moveToNext()) {
            strings.add(cursor.getString(0));
        }
        cursor.close();
        return strings;
    }

    /**
     * Метод проверяет запущен ли сервис по отслеживанию или нет
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод останавливает сервис по отслеживанию новых обьявлений
     */
    public void stopTracking() {
        Intent myIntent = new Intent(getApplicationContext(), ServerService.class);
        this.stopService(myIntent);
    }

    public void clickFavOk(View view) {
        intent = new Intent(FavouriteSearch.this, MainActivity.class);
        startOrStopTracking();
        startActivity(intent);
    }
}
