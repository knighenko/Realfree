package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.knighenko.realfree.R;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Advertisement> advertisements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        String Url = getIntent().getStringExtra("url");

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
     * Метод соединяется с сервером
     */

    private void connectToServer(final String Url) {

        if (Url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        ConnectServer connectServer = new ConnectServer("91.235.129.33", 8080);
                        advertisements = new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements();

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }).start();
        }

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