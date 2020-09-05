package com.knighenko.realfree.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.knighenko.realfree.R;

public class SearchAdvertisements extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Chip chip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_advertisements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Выбор рубрики");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chipGroup = findViewById(R.id.chip_group);
        for (int i=0; i<chipGroup.getChildCount();i++){
            Chip chip = (Chip)chipGroup.getChildAt(i);
            if (chip.isChecked()){
                System.out.println("Hello");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


}
