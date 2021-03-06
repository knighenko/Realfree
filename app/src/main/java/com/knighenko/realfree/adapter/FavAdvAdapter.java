package com.knighenko.realfree.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.knighenko.realfree.entity.Advertisement;

import java.util.ArrayList;

public class FavAdvAdapter extends RecyclerView.Adapter<AdvAdapter.AdvListHolder>  {
    private ArrayList<Advertisement> advertisements = new ArrayList<>();
    private AdvAdapter.OnAdvertisementClickListener onAdvertisementClickListener;
    @NonNull
    @Override
    public AdvAdapter.AdvListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdvAdapter.AdvListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public interface OnAdvertisementClickListener {
        void onAdvClick(Advertisement advertisement);
        void onWhatsClick(Advertisement advertisement);
    }
}
