    package com.example.customerapp;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.bumptech.glide.Glide;
    import com.example.customerapp.databinding.ActivityItemDetailsBinding;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.CollectionReference;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;

    import java.util.HashMap;
    import java.util.Map;

    public class ItemDetails extends AppCompatActivity {

        private ActivityItemDetailsBinding binding;
        private FirebaseFirestore firestore;
        private static final String TAG = "ItemDetails";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            firestore = FirebaseFirestore.getInstance();

            String name = getIntent().getStringExtra("name");
            Double price = getIntent().getDoubleExtra("price", 0.0);
            String formattedPrice = String.format("%.2f", price);
            binding.clothesPrice.setText("₱ %.2f" + formattedPrice);
            String imageUrl = getIntent().getStringExtra("imageUrl");

            // Bind data to views
            binding.clothesName.setText(name);
            binding.clothesPrice.setText(formattedPrice);
            Glide.with(this).load(imageUrl).into(binding.clothesImage);

            // Add to Cart functionality
            binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemToCart(name, price, imageUrl);
                }
            });
        }

        private void addItemToCart(String name, double price, String imageUrl) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(ItemDetails.this, "User not logged in!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = currentUser.getUid();
            CollectionReference cartRef = firestore.collection("users").document(userId).collection("cart");
            cartRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean itemExists = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String itemName = document.getString("Name");
                        Double itemPrice = document.getDouble("Price");
                        String itemImageUrl = document.getString("ImageUrl");
                        Long itemQuantity = document.getLong("Quantity");

                        if (itemName == null || itemPrice == null || itemImageUrl == null || itemQuantity == null) {
                            Log.e(TAG, "Skipping document with null values: " + document.getId());
                            continue;
                        }

                        if (itemName.equals(name) && itemImageUrl.equals(imageUrl)) {
                            int currentQuantity = itemQuantity.intValue();
                            document.getReference().update("Quantity", currentQuantity + 1)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ItemDetails.this, "Quantity updated!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ItemDetails.this, "Error updating quantity", Toast.LENGTH_SHORT).show();
                                    });
                            itemExists = true;
                            break;
                        }
                    } // If item doesn't exist, add it to the cart
                    if (!itemExists) {
                        Map<String, Object> cartItem = new HashMap<>();
                        cartItem.put("Name", name);
                        cartItem.put("Price", price);
                        cartItem.put("ImageUrl", imageUrl);
                        cartItem.put("Quantity", 1);

                        cartRef.add(cartItem)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(ItemDetails.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ItemDetails.this, "Error adding item to cart", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    // Handle failure in fetching documents
                    Toast.makeText(ItemDetails.this, "Error checking cart", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

