package com.example.customerapp;

import com.example.customerapp.adapter.Clothes;
import com.example.customerapp.adapter.ClothesAdapter;
import com.example.customerapp.databinding.ActivityHomepageBinding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Homepage extends AppCompatActivity {
    private ClothesAdapter adapter;
    private ActivityHomepageBinding binding;
    private ArrayList<Clothes> clothesList, filteredClothesList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView cart = binding.cart;
        SearchView searchView = binding.searchView;

        clothesList = new ArrayList<>();
        filteredClothesList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ClothesAdapter(this, filteredClothesList);
        recyclerView.setAdapter(adapter);

        binding.progressBar.setVisibility(View.VISIBLE);

        fetchDataFromFirebase();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterClothes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    resetClothesList();
                }
                return false;
            }
        });

        BottomNavigationView botNav = binding.bottomNavigation;
        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.loc) {
                    startActivity(new Intent(Homepage.this, Location.class));
                    return true;
                } else if (id == R.id.pay) {
                    startActivity(new Intent(Homepage.this, Payment.class));
                    return true;
                } else if (id == R.id.prof) {
                    startActivity(new Intent(Homepage.this, Profile.class));
                    return true;
                }
                return false;
            }
        });
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, Cart.class));
            }
        });
    }

    private void resetClothesList() {
        filteredClothesList.clear();
        filteredClothesList.addAll(clothesList);
        adapter.updateData(filteredClothesList);
    }

    private void filterClothes(String query) {
        filteredClothesList.clear();

        if (query.isEmpty()) {
            filteredClothesList.addAll(clothesList);
        } else {
            for (Clothes clothes : clothesList) {
                if (clothes.getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    filteredClothesList.add(clothes);
                }
            }
        }
        adapter.updateData(filteredClothesList);
        if (!filteredClothesList.isEmpty()) {
            binding.recyclerView.scrollToPosition(0);
        }
    }

    private void fetchDataFromFirebase() {
        firestore.collection("clothes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Clothes> newClothesList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("FirestoreData", "Document ID: " + document.getId() + ", Data: " + document.getData());

                            String name = document.getString("Name");
                            String priceString = document.getString("Price");
                            String imageUrl = document.getString("ImageUrl");


                            if (name == null || priceString == null || imageUrl == null ) {
                                Log.e("FirestoreData", "Null value found in document: " + document.getId());
                                continue;
                            }

                            double price;
                            try {
                                price = Double.parseDouble(priceString);
                            } catch (NumberFormatException e) {
                                Log.e("FirestoreData", "Error parsing price: " + priceString);
                                continue;
                            }

                            Clothes clothes = new Clothes(name, price, imageUrl);
                            newClothesList.add(clothes);
                        }

                        clothesList = newClothesList;
                        filteredClothesList.addAll(clothesList);
                        adapter.updateData(filteredClothesList);
                        hideProgressBar();

                    } else {
                        Log.d("FirestoreData", "Error getting documents: ", task.getException());
                        hideProgressBar();
                    }
                });
    }


    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}
