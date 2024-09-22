package com.example.customerapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customerapp.adapter.CartAdapter;
import com.example.customerapp.adapter.Clothes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    private CartAdapter adapter;
    private ArrayList<Clothes> cartList;
    private FirebaseFirestore firestore;
    private CheckBox selectAllCheckBox;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with the cart list and set it to the RecyclerView
        adapter = new CartAdapter(this, cartList);
        recyclerView.setAdapter(adapter);

        // Select all checkbox to manage all items selection in the cart
        selectAllCheckBox = findViewById(R.id.selectAllCheckBox);
        selectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            adapter.selectAllItems(isChecked);
        });

        // Fetch cart items from Firebase
        fetchCartItemsFromFirebase();
    }

    private void fetchCartItemsFromFirebase() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference cartRef = firestore.collection("users").document(userId).collection("cart");

        cartRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, "Fetched document: " + document.getId());
                    Log.d(TAG, "Document data: " + document.getData());

                    // Retrieve and validate data with different possible field names
                    String name = document.getString("Name");
                    if (name == null) {
                        name = document.getString("name"); // Try alternate field name
                    }
                    if (name == null) {
                        Log.e(TAG, "Name is null for document: " + document.getId());
                        continue; // Skip document if name is not found
                    }

                    Double price = document.getDouble("Price");
                    if (price == null) {
                        price = document.getDouble("price"); // Try alternate field name
                    }
                    if (price == null) {
                        Log.e(TAG, "Price is null for document: " + document.getId());
                        continue; // Skip document if price is not found
                    }

                    String imageUrl = document.getString("ImageUrl");
                    if (imageUrl == null) {
                        imageUrl = document.getString("imageUrl"); // Try alternate field name
                    }
                    if (imageUrl == null) {
                        Log.e(TAG, "ImageUrl is null for document: " + document.getId());
                        continue; // Skip document if imageUrl is not found
                    }

                    // Retrieve quantity with a possible field name
                    Long quantity = document.getLong("Quantity");
                    if (quantity == null) {
                        quantity = document.getLong("quantity"); // Try alternate field name
                    }
                    if (quantity == null) {
                        Log.e(TAG, "Quantity is null for document: " + document.getId());
                        continue;
                    }

                    // Create a Clothes object and add it to the list
                    Clothes clothes = new Clothes(name, price, imageUrl);
                    cartList.add(clothes);
                }

                adapter.updateData(cartList);
                Log.d(TAG, "Cart item count in list: " + cartList.size());
                Log.d(TAG, "Cart item count in adapter: " + adapter.getItemCount());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
