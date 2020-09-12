package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.knighenko.realfree.R;
import com.knighenko.realfree.adapter.AdvAdapter;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.ConnectServer;
import com.knighenko.realfree.model.JsonToObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Advertisement> advertisements;
    private static final String SERVER_IP ="91.235.129.33";
   // private static final String SERVER_IP ="10.0.2.2";
    private static final int PORT =8080;
    private RecyclerView listAdvRecyclerView;
    private AdvAdapter advAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        String Url = getIntent().getStringExtra("url");
        connectToServer(Url);

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
     * Метод соединяется с  сервером и получает массив обьявлений
     */

    private void connectToServer(final String Url) {

        if (Url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConnectServer connectServer = new ConnectServer(SERVER_IP, PORT);
                        advertisements = new JsonToObject(connectServer.readJsonStrig(Url)).getAdvertisements();
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

/**Метод инициализирует список обьявлений из выбранной рубрики*/
    private void initRecyclerView() {
        listAdvRecyclerView = findViewById(R.id.listAdvRecyclerView);
        listAdvRecyclerView.setHasFixedSize(true);
        listAdvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdvAdapter.OnAdvertisementClickListener onAdvertisementClickListener=new AdvAdapter.OnAdvertisementClickListener() {
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