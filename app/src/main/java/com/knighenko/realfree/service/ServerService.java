package com.knighenko.realfree.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;


import com.knighenko.realfree.R;
import com.knighenko.realfree.activities.AdvertisementActivity;
import com.knighenko.realfree.activities.MainActivity;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ServerService extends Service {
    private SQLiteDatabase myDB;
    private static final String SERVER_IP = "91.235.129.33";
    //  private static final String SERVER_IP ="10.0.2.2";
    private static final int PORT = 8080;
    private static int notificationId = 2;
    public static final String CHANNEL_1 = "ForegroundServiceChannel";
    private Timer timer;

    public ServerService() {

    }

    private void createNotificationChannel1() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_1,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationManager.createNotificationChannel(serviceChannel);

        }
    }

    @Override
    public void onCreate() {
        createDB();
        super.onCreate();
        timer = new Timer();
        createNotificationChannel1();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ArrayList<String> advertisements = intent.getStringArrayListExtra("advertisements");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1)
                .setContentTitle("Идет мониторинг обьявлений OLX")
                .setSmallIcon(R.mipmap.ic_message)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        for (String url : advertisements) {
            readFromServerTenSec(url);
        }

        return START_STICKY;
    }


    // Destroy
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Метод посылает каждые 10 секунд сообщение на сервер, сохраняет данные на телефон и проверяет совпадения и выводит новые обьявления с использованием TimerTask
     */
/*
    private void readFromServerTenSec(final String Url) {

        int delay = 10000; // delay for 10 sec.
        int period = 10000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //System.out.println("time before is"+ Calendar.getInstance().getTime());
                ConnectServer connectServer = null;
                try {
                    connectServer = new ConnectServer(SERVER_IP, PORT);

                    for (Advertisement adv : new JsonToObject(connectServer.readJsonString(Url)).getAdvertisements()) {
                        if (!checkInDB(adv.getTitle())) {

                            addToDB(adv, myDB);

                            Intent advIntent = new Intent(getBaseContext(),
                                    AdvertisementActivity.class);
                            advIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            advIntent.putExtra("urlAdv", adv.getUrl());
                            advIntent.putExtra("title", adv.getTitle());
                            advIntent.putExtra("description", adv.getDescription());
                            advIntent.putExtra("imageSrc", adv.getImageSrc());

                            PendingIntent pendingIntent = PendingIntent.getActivity(ServerService.this,
                                    notificationId, advIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification notification = new NotificationCompat.Builder(ServerService.this, CHANNEL_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(adv.getTitle())
                                    .setContentText(adv.getDescription())
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .build();


                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(notificationId, notification);
                            notificationId++;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
              //  System.out.println("time after is"+ Calendar.getInstance().getTime());
            }
        }, delay, period);

    }
/*

    /**
     * Метод посылает каждые 10 секунд сообщение на сервер, сохраняет данные на телефон и проверяет совпадения и выводит новые обьявления
     */
    private void readFromServerTenSec(final String Url) {

        int delay = 100; // delay for 10 sec.
        int period = 7000; // repeat every 10 sec.
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                ConnectServer connectServer = null;
                try {
                    connectServer = new ConnectServer(SERVER_IP, PORT);
                    ArrayList<Advertisement> advertisements = new JsonToObject(connectServer.readJsonString(Url)).getAdvertisements();
                    for (Advertisement adv : advertisements) {
                        if (!checkInDB(adv.getTitle())) {

                            addToDB(adv, myDB);

                            Intent advIntent = new Intent(getBaseContext(),
                                    AdvertisementActivity.class);
                            advIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            advIntent.putExtra("urlAdv", adv.getUrl());
                            advIntent.putExtra("title", adv.getTitle());
                            advIntent.putExtra("description", adv.getDescription());
                            advIntent.putExtra("imageSrc", adv.getImageSrc());

                            PendingIntent pendingIntent = PendingIntent.getActivity(ServerService.this,
                                    notificationId, advIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification notification = new NotificationCompat.Builder(ServerService.this, CHANNEL_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(adv.getTitle())
                                    .setContentText(adv.getDescription())
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(notificationId, notification);
                            notificationId++;

                        }

                    }
                    System.out.println("***************************time after is********************************" + Calendar.getInstance().getTime());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);

    }


    /**
     * Метод создает базу данных или открывает созданную + создает таблицу advertisement
     */
    private void createDB() {
        myDB = openOrCreateDatabase("myNew.db", MODE_PRIVATE, null);
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
     * Метод проверяет на наличие записи по title в базе, true - элемент присутствует!
     */
    private boolean checkInDB(String title) {
        String query = "SELECT EXISTS (SELECT  * FROM advertisement WHERE title = ? LIMIT 1)";
        // Cursor cursor = myDB.rawQuery("SELECT EXISTS (select * from  advertisement WHERE title = '" + title + "'  LIMIT 1)", null);
        Cursor cursor = myDB.rawQuery(query, new String[]{title});
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 1) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    class Read extends AsyncTask<String, Void, ArrayList<Advertisement>> {
        @Override
        protected ArrayList<Advertisement> doInBackground(String... strings) {
            ArrayList<Advertisement> advertisements = null;
            try {
                ConnectServer connectServer = new ConnectServer(SERVER_IP, PORT);
                advertisements = new JsonToObject(connectServer.readJsonString(strings[0])).getAdvertisements();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return advertisements;
        }
    }

}



