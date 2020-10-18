package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.knighenko.realfree.R;
import com.squareup.picasso.Picasso;

public class AdvertisementActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private ImageView imageView;
    private static final String LOG = "MyApp";
    private Toolbar toolbar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_adv);

        this.title = findViewById(R.id.textViewTitleAdv);
        this.description = findViewById(R.id.textViewDescription);
        this.imageView = findViewById(R.id.bigImageViewAdv);
        this.toolbar = findViewById(R.id.toolbar);
        this.url=getIntent().getStringExtra("urlAdv");
        toolbar.setTitle("OLX бесплатно");
        setSupportActionBar(toolbar);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
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

    public void advClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}