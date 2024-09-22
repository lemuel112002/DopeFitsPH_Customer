package com.example.customerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.customerapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<Clothes> cartList;

    public CartAdapter(Context context, ArrayList<Clothes> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Clothes clothes = cartList.get(position);

        // Set image
        Glide.with(context).load(clothes.getImageUrl()).into(holder.cartImageView);

        // Set name and price
        holder.tvCartName.setText(clothes.getName());
        holder.tvCartPrice.setText(String.format("₱ %.2f", clothes.getPrice()));
        holder.tvQuantity.setText(String.valueOf(clothes.getQuantity()));

        // Increase and Decrease Quantity
        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = Math.toIntExact(clothes.getQuantity());
            if (quantity < clothes.getMaxQuantity()) {
                clothes.setQuantity(quantity + 1);
                holder.tvQuantity.setText(String.valueOf(clothes.getQuantity()));
            } else {
                Snackbar.make(v, "Maximum quantity reached", Snackbar.LENGTH_SHORT).show();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = Math.toIntExact(clothes.getQuantity());
            if (quantity > 1) {
                clothes.setQuantity(quantity - 1);
                holder.tvQuantity.setText(String.valueOf(clothes.getQuantity()));
            } else {
                Snackbar.make(v, "Quantity cannot be less than 1", Snackbar.LENGTH_SHORT).show();
            }
        });

        // Remove from Cart
        holder.btnRemoveFromCart.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
        });
    }

    public void selectAllItems(boolean isSelected) {
        for (Clothes item : cartList) {
            item.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Clothes> newCartList) {
        this.cartList.clear(); // Clear old data
        this.cartList.addAll(newCartList); // Add new data
        notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox cartSelectCheckBox;
        ImageView cartImageView;
        TextView tvCartName, tvCartPrice, tvQuantity;
        ImageView btnIncrease, btnDecrease, btnRemoveFromCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartSelectCheckBox = itemView.findViewById(R.id.cartSelectCheckBox);
            cartImageView = itemView.findViewById(R.id.cartImageView);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }
}
