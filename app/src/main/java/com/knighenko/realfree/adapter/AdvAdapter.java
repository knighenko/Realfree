package com.knighenko.realfree.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.knighenko.realfree.R;
import com.knighenko.realfree.entity.Advertisement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;


public class AdvAdapter extends RecyclerView.Adapter<AdvAdapter.AdvListHolder> {
    private ArrayList<Advertisement> advertisements = new ArrayList<>();
    private OnAdvertisementClickListener onAdvertisementClickListener;

    public AdvAdapter(OnAdvertisementClickListener onAdvertisementClickListener) {
        this.onAdvertisementClickListener = onAdvertisementClickListener;
    }

    @NonNull
    @Override
    public AdvListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement, parent, false);
        return new AdvListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvListHolder holder, int position) {
        System.err.println("Advertisement on position " + position + " " + advertisements.get(position).getTitle());
        holder.bind(advertisements.get(position));

    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }


    public void clearListAdv() {
        advertisements.clear();
        notifyDataSetChanged();
    }

    public void setListAdv(Collection<Advertisement> advertisements) {
        this.advertisements.addAll(advertisements);
        notifyDataSetChanged();

    }

    class AdvListHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;

        private ImageView imageView;

        public AdvListHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitleAdv);
            description=itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.smallImageViewAdv);
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Advertisement adv=advertisements.get(getLayoutPosition());
                    onAdvertisementClickListener.onAdvClick(adv);
                }
            });
        }

        public void bind(Advertisement adv) {
            title.setText(adv.getTitle());
            title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            description.setText(adv.getDescription());
            String urlImg = adv.getImageSrc();
            paintImg(urlImg);
        }

        private void paintImg(String urlImg) {
            if (urlImg.equals("No Img")) {
                imageView.setImageResource(R.drawable.no_image);
            } else {
                Picasso.get().load(urlImg).resize(200, 200).centerCrop().into(imageView);
            }
        }
    }

    public interface OnAdvertisementClickListener {
        void onAdvClick(Advertisement advertisement);
    }
}
