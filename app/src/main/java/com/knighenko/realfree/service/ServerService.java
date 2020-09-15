package com.knighenko.realfree.service;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.knighenko.realfree.R;
import com.knighenko.realfree.activities.MainActivity;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ServerService extends Service {
    private SQLiteDatabase myDB;
    private static final String SERVER_IP = "91.235.129.33";
    // private static final String SERVER_IP ="10.0.2.2";
    private static final int PORT = 8080;
    private static int notificationId = 1;
      public ServerService() {

    }

    @Override
    public void onCreate() {
        createDB();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       String url= intent.getStringExtra("url");
        readFromServerFefteenSec(url);
        return START_STICKY;
    }

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
     * Метод посылает каждые 60 секунд сообщение на сервер, сохраняет данные на телефон и проверяет совпадения и выводит новые обьявления
     */
    private void readFromServerFefteenSec(final String Url) {
        int delay = 1000; // delay for 1 sec.
        int period = 60000; // repeat every 60 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ConnectServer connectServer = null;
                try {
                    connectServer = new ConnectServer(SERVER_IP, PORT);
                    System.out.println("*******************" + Calendar.getInstance().getTime() + "////////////////////////////////////////");
                    for (Advertisement adv : new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements()) {
                        if (!checkInDB(adv.getTitle())) {
                            addToDB(adv,myDB);
                            Notification notification = new NotificationCompat.Builder(getBaseContext(), MainActivity.CHANNEL_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(adv.getTitle())
                                    .setContentText("Приехало")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getBaseContext());
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


}
