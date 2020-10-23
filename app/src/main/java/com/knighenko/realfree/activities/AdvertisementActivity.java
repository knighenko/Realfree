package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.knighenko.realfree.R;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.model.UrlOfPages;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;

public class AdvertisementActivity extends AppCompatActivity {
    private TextView titleView;
    private TextView descriptionView;
    private ImageView imageView;
    private static final String LOG = "MyApp";
    private Toolbar toolbar;
    private String url;
    private String title;
    private String description;
    private SQLiteDatabase myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_adv);

        this.titleView = findViewById(R.id.textViewTitleAdv);
        this.descriptionView = findViewById(R.id.textViewDescription);
        this.imageView = findViewById(R.id.bigImageViewAdv);
        this.toolbar = findViewById(R.id.toolbar);
        this.url=getIntent().getStringExtra("urlAdv");
        this.title=getIntent().getStringExtra("title");
        this.description=getIntent().getStringExtra("description");
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        titleView.setText(title);
        descriptionView.setText(description);
        paintImg(getIntent().getStringExtra("srcUrl"));
    }

    private void paintImg(String urlImg) {
        if (urlImg.equals("No Img")) {
            imageView.setImageResource(R.drawable.no_image);
        } else {
            Picasso.get().load(urlImg).resize(300, 300).centerCrop().into(imageView);
        }
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
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchAdvertisements.class);
            startActivity(intent);
        }
        return true;
    }
/**Метод реагирует на нажатие по описанию нового обьявления*/
    public void advClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    /**Метод реагирует на нажатие WhatsApp нового обьявления*/
    public void advWhatsClick(View view) {

        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String urlWhats = "https://api.whatsapp.com/send?phone=" + "+380677533012" + "&text=" + URLEncoder.encode(url, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(urlWhats));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**Метод реагирует на нажатие Favourite нового обьявления*/
    public void advFavClick(View view) {
        createDB();
        addToDB(title,url,description,myDB);
        Toast.makeText(this, "Обьявление добавлено в Избранное ", Toast.LENGTH_LONG).show();
    }
    /**
     * Метод создает базу данных или открывает созданную
     */
    private void createDB() {
        myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS favourite ( title TEXT, url TEXT, description Text)");

    }
    /**
     * Метод добавляет элементы в таблицу избранных обьявлений
     */
    private void addToDB(String title, String url, String description, SQLiteDatabase myDB) {
        ContentValues row = new ContentValues();
        row.put("title", title);
        row.put("url", url);
        row.put("description", description);
        myDB.insert("favourite", null, row);

    }
}