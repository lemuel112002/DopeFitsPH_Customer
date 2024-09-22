package com.example.customerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.customerapp.Homepage;
import com.example.customerapp.ItemDetails;
import com.example.customerapp.R;

import java.util.ArrayList;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {
    private ArrayList<Clothes> clothesList;
    private Context context;

    // Constructor with context
    public ClothesAdapter(Context context, ArrayList<Clothes> clothesList) {
        this.context = context;
        this.clothesList = clothesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothes clothes = clothesList.get(position);
        holder.nameTextView.setText(clothes.getName());

        Double price = clothes.getPrice();
        if (price != null) {
            String formattedPrice = "₱ " + String.format("%.2f", price);
            holder.priceTextView.setText(formattedPrice);
        } else {
            // Handle the case where the price is null
            holder.priceTextView.setText("Price not available");
        }

        String imageUrl = clothes.getImageUrl();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.imgg)
                .error(R.drawable.imgerror)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (context instanceof Homepage) {
                            ((Homepage) context).hideProgressBar();
                        }
                        return false;
                    }
                })
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetails.class);
            intent.putExtra("name", clothes.getName());
            intent.putExtra("price", clothes.getPrice());
            intent.putExtra("imageUrl", clothes.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    public void updateData(ArrayList<Clothes> clothesList) {
        this.clothesList = clothesList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            priceTextView = itemView.findViewById(R.id.txtPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
