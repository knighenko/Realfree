package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.knighenko.realfree.R;
import com.knighenko.realfree.adapter.AdvAdapter;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;
import com.knighenko.realfree.model.UrlOfPages;

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
    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        String Url = getIntent().getStringExtra("url");
        connectToServerStart(Url);


        readFromServer(UrlOfPages.HOME_GARDEN.getUrl());


    }

    /**
     * Метод сохраняет данные поиска на устройство с выбранной рубрики
     */
    private void saveToAndroidFirst(ArrayList<Advertisement> myAdvertisements) {
        this.myPreferences
                = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor1 = myPreferences.edit();
        for (Advertisement adv : myAdvertisements) {
            myEditor1.putString(adv.getTitle(), adv.getUrl());
        }
        myEditor1.commit();
    }

    /**
     * Метод сохраняет данные поиска на устройство с выбранной рубрики через каждые 15 сек.
     */
    private void saveToAndroidFifteenSec(ArrayList<Advertisement> myAdvertisements) {
        SharedPreferences myPreferences2
                = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor2 = myPreferences2.edit();
        for (Advertisement adv : myAdvertisements) {
            myEditor2.putString(adv.getTitle(), adv.getUrl());
        }
        myEditor2.commit();
    }

    /**
     * Метод посылает каждые 15 секунд сообщение на сервер, сохраняет данные на телефон и проверяет совпадения и выводит новые обьявления
     */
    private void readFromServer(final String Url) {
        int delay = 1000; // delay for 1 sec.
        int period = 15000; // repeat every 15 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ConnectServer connectServer = null;
                try {
                    connectServer = new ConnectServer(SERVER_IP, PORT);
                    for (Advertisement adv : new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements()) {
                        if ((myPreferences!=null)&&(myPreferences.contains(adv.getTitle())))
                        {

                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(MainActivity.this, adv.getTitle())
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentTitle("Напоминание")
                                            .setContentText("Пора покормить кота")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager =
                                    NotificationManagerCompat.from(MainActivity.this);
                            notificationManager.notify(101, builder.build());
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

    private void connectToServerStart(final String Url) {

        if (Url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectServer connectServer = new ConnectServer(SERVER_IP, PORT);
                        advertisements = new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements();
                        saveToAndroidFirst(advertisements);
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