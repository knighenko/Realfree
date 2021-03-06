package com.knighenko.realfree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.chip.ChipGroup;
import com.knighenko.realfree.entity.Advertisement;
import com.knighenko.realfree.R;
import com.knighenko.realfree.model.UrlOfPages;

import java.util.ArrayList;


public class SearchAdvertisements extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Intent intent;
    private ArrayList<Advertisement> advertisements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_advertisements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Поиск по рубрикам");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chipGroup = (ChipGroup) findViewById(R.id.my_chip_group);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                chekChip(chipGroup);
            }
        });
    }

    /**
     * Метод проверяет какой чип выбран и генерирует результат
     */
    private void chekChip(ChipGroup chipGroup) {

        switch (chipGroup.getCheckedChipId()) {
            case R.id.chip_home_garden:
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.HOME_GARDEN.getTitle(), Toast.LENGTH_LONG).show();

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.HOME_GARDEN.getUrl() + 1);
                startActivity(intent);

                break;
            case R.id.electronics:
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.ELECTRONICS.getTitle(), Toast.LENGTH_LONG).show();
                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.ELECTRONICS.getUrl() + 1);
                startActivity(intent);

                break;
            case R.id.transport_parts:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.TRANSPORT_PARTS.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.TRANSPORT_PARTS.getTitle(), Toast.LENGTH_LONG).show();
                break;
            case R.id.business_services:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.BUSINESS_AND_SERVICES.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.BUSINESS_AND_SERVICES.getTitle(), Toast.LENGTH_LONG).show();
                break;
            case R.id.fashion_style:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.FASHION_AND_STYLE.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.FASHION_AND_STYLE.getTitle(), Toast.LENGTH_LONG).show();
                break;
            case R.id.hobbies_leisure:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.HOBBIES_AND_LEISURE.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.HOBBIES_AND_LEISURE.getTitle(), Toast.LENGTH_LONG).show();
                break;
            case R.id.world_of_children:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.WORLD_OF_CHILDREN.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.WORLD_OF_CHILDREN.getTitle(), Toast.LENGTH_LONG).show();
                break;
            case R.id.zooproduct:

                intent = new Intent(SearchAdvertisements.this, MainActivity.class);
                intent.putExtra("url", UrlOfPages.ZOOPRODUCT.getUrl() + 1);
                startActivity(intent);
                Toast.makeText(this, "Поиск в рубрике " + UrlOfPages.ZOOPRODUCT.getTitle(), Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


}
