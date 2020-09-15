package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.knighenko.realfree.R;
import com.knighenko.realfree.adapter.AdvAdapter;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;
import com.knighenko.realfree.model.UrlOfPages;
import com.knighenko.realfree.service.ServerService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Advertisement> advertisements;
    private static final String SERVER_IP = "91.235.129.33";
    // private static final String SERVER_IP ="10.0.2.2";
    private static final int PORT = 8080;
    private RecyclerView listAdvRecyclerView;
    private AdvAdapter advAdapter;
    private SQLiteDatabase myDB;
    public static final String CHANNEL_1 = "channel1";
    private static int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        this.createNotificationChannels();
        createDB();

        String Url = getIntent().getStringExtra("url");
        connectToServerSearch(Url);
        startTracking(UrlOfPages.HOME_GARDEN.getUrl());
          //   readFromServerFefteenSec(UrlOfPages.BUSINESS_AND_SERVICES.getUrl());

    }

    /**
     * Метод запускает сервис по отслеживанию новых обьявлений
     */
    public void startTracking(String url) {
        Intent myIntent = new Intent(MainActivity.this, ServerService.class);
        myIntent.putExtra("url", url);
        this.startService(myIntent);
    }

    /**
     * Метод останавливает сервис по отслеживанию новых обьявлений
     */
    public void stopTracking() {
        Intent myIntent = new Intent(MainActivity.this, ServerService.class);
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
     * Метод проверяет на наличие записи по title в базе, true - элемент присутствует!
     */
    private boolean checkInDB(String title) {
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
     * Deleted
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    /**
     * Метод посылает каждые 15 секунд сообщение на сервер, сохраняет данные на телефон и проверяет совпадения и выводит новые обьявления
     */
    private void readFromServerFefteenSec(final String Url) {
        int delay = 1000; // delay for 1 sec.
        int period = 15000; // repeat every 15 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ConnectServer connectServer = null;
                try {
                    connectServer = new ConnectServer(SERVER_IP, PORT);
                    System.out.println("*******************" + Calendar.getInstance().getTime() + "////////////////////////////////////////");
                    for (Advertisement adv : new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements()) {
                        if (!checkInDB(adv.getTitle())) {
                            addToDB(adv, myDB);
                            Notification notification = new NotificationCompat.Builder(MainActivity.this, MainActivity.CHANNEL_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(adv.getTitle())
                                    .setContentText("Приехало")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                            notificationManagerCompat.notify(notificationId, notification);
                            notificationId++;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);

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
     * Метод соединяется первый раз с сервером и получает массив обьявлений и записывает данные в SharedPreferences
     */

    private void connectToServerSearch(final String Url) {

        if (Url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectServer connectServer = new ConnectServer(SERVER_IP, PORT);
                        advertisements = new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements();
                        for (Advertisement adv : advertisements) {
                            addToDB(adv, myDB);
                        }
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
        };
        advAdapter = new AdvAdapter(onAdvertisementClickListener);
        advAdapter.setListAdv(advertisements);
        listAdvRecyclerView.setAdapter(advAdapter);

    }

    /**
     * Метод реагирует на нажатие кнопки меню, в данном случае кнопки поиска
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchAdvertisements.class);
            startActivity(intent);
        }
        return true;
    }
}