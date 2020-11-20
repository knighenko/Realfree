package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.knighenko.realfree.R;
import com.knighenko.realfree.adapter.AdvAdapter;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;
import com.knighenko.realfree.model.UrlOfPages;
import com.knighenko.realfree.service.ServerService;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Advertisement> advertisements;
    private static final String SERVER_IP = "91.235.129.33";
    //private static final String SERVER_IP ="10.0.2.2";
    private static final int PORT = 8080;
    private RecyclerView listAdvRecyclerView;
    private AdvAdapter advAdapter;
    private SQLiteDatabase myDB;
    private ProgressBar progressBar;

    private static int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        createDB();
        if (savedInstanceState == null || !savedInstanceState.containsKey("Advertisements")) {
            String Url = getIntent().getStringExtra("url");
            connectToServerSearch(Url);
        } else {
            if (advertisements != null) {
                advertisements = savedInstanceState.getParcelableArrayList("Advertisements");
                initRecyclerView();
            }
        }


    }

    /**
     * Метод запускает и останавливает мониторинг обьявление
     */
    private void startOrStopTracking() {
        ArrayList<String> strings = readFromDBFavourite();
        if (!isMyServiceRunning(ServerService.class) & strings.size() >= 1)
            startTracking(strings);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Advertisements", advertisements);
        super.onSaveInstanceState(outState);
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
     * Метод запускает сервис по отслеживанию новых обьявлений из заданной рубрики
     */
    public void startTracking(ArrayList<String> strings) {
        for (String url : strings) {
            Intent myIntent = new Intent(getApplicationContext(), ServerService.class);
            myIntent.putExtra("url", url);
            ContextCompat.startForegroundService(this, myIntent);
        }
    }

    /**
     * Метод останавливает сервис по отслеживанию новых обьявлений
     */
    public void stopTracking() {
        Intent myIntent = new Intent(getApplicationContext(), ServerService.class);
        this.stopService(myIntent);
    }

    /**
     * Метод создает базу данных или открывает созданную
     */
    private void createDB() {
        myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS advertisement ( title TEXT, url TEXT, srcUrl Text)");

    }

    /**
     * Метод добавляет элементы в базу данных
     */
    private void addToDB(Advertisement adv, SQLiteDatabase myDB) {
        ContentValues row = new ContentValues();
        row.put("title", adv.getTitle());
        row.put("url", adv.getUrl());
        row.put("srcUrl", adv.getImageSrc());
        myDB.insert("advertisement", null, row);

    }

    /**
     * Метод печатает в консоль все записи из базы
     */
    private void printDB() {
        Cursor cursor = myDB.rawQuery("select title, url, srcUrl from advertisement", null);
        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
        }
        cursor.close();
    }

    /**
     * Метод проверяет на наличие записи по title (Обьявления) в базе, true - элемент присутствует!
     */
    private boolean checkInDBAdvert(String title) {
        Cursor cursor = myDB.rawQuery("SELECT EXISTS (select * from  advertisement WHERE title = \"" + title + "\"  LIMIT 1)", null);
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
     * Метод соединяется первый раз с сервером и получает массив обьявлений и записывает данные в SharedPreferences
     */

    private void connectToServerSearch(final String Url) {

        if (Url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        ConnectServer connectServer = new ConnectServer(SERVER_IP, PORT);
                        advertisements = new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements();
                        for (Advertisement adv : advertisements) {
                            addToDB(adv, myDB);
                        }
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRecyclerView();
                        }
                    });


                }
            }).start();
        }

    }

    /**
     * Метод инициализирует список обьявлений из выбранной рубрики
     */
    private void initRecyclerView() {
        listAdvRecyclerView = findViewById(R.id.listAdvRecyclerView);
        listAdvRecyclerView.setHasFixedSize(true);
        listAdvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdvAdapter.OnAdvertisementClickListener onAdvertisementClickListener = new AdvAdapter.OnAdvertisementClickListener() {
            @Override
            public void onAdvClick(Advertisement advertisement) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(advertisement.getUrl()));
                startActivity(intent);

            }

            /**
             * Метод обрабатывает нажатие на кнопку ватцапа
             */
            @Override
            public void onWhatsClick(Advertisement advertisement) {
                String urlAdv = advertisement.getUrl();
                try {
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=" + "+380677533012" + "&text=" + URLEncoder.encode(urlAdv, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        advAdapter = new AdvAdapter(onAdvertisementClickListener);
        advAdapter.setListAdv(advertisements);
        listAdvRecyclerView.setAdapter(advAdapter);

    }

    /**
     * Метод создает меню
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    /**
     * Метод реагирует на нажатие кнопки меню, в данном случае кнопки поиска
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(this, SearchAdvertisements.class);
                startActivity(intent);
                return true;
            case R.id.favourite_search:
                intent = new Intent(this, FavouriteSearch.class);
                startActivity(intent);
                return true;
            case R.id.favourite_adv:
                intent = new Intent(this, FavouriteAdvertisements.class);
                startActivity(intent);
                return true;
            case R.id.clear:
                getApplicationContext().deleteDatabase("myNew.db");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "База данных очищена!!!", Toast.LENGTH_SHORT);
                toast.show();
                myDB = openOrCreateDatabase("myNew.db", MODE_PRIVATE, null);
                myDB.execSQL("CREATE TABLE IF NOT EXISTS advertisement ( title TEXT, url TEXT, srcUrl Text)");
                return true;
        }

        return false;
    }
}