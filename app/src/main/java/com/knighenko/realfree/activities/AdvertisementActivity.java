package com.knighenko.realfree.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.knighenko.realfree.R;

public class AdvertisementActivity extends AppCompatActivity {
private TextView title;
private TextView description;
private static  final String LOG="MyApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement);
        Log.d(LOG,"onCreate");
        this.title=findViewById(R.id.textViewTitleAdv);
        this.description=findViewById(R.id.textViewDescription);
        Log.d(LOG,"url="+getIntent().getStringExtra("url"));
        title.setText(getIntent().getStringExtra("url"));
        description.setText(getIntent().getStringExtra("description"));
    }


}