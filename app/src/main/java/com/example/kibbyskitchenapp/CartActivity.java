package com.example.kibbyskitchenapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView cartItemsTextView;
    private Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cartItemsTextView = findViewById(R.id.cart_items);
        placeOrderButton = findViewById(R.id.place_order_button);

        StringBuilder itemsList = new StringBuilder();
        for (OrderItem item : Cart.getInstance().getItems()) {
            itemsList.append(item.getItemName()).append(" - KES ").append(item.getPrice()).append("\n");
        }
        cartItemsTextView.setText(itemsList.toString());

        placeOrderButton.setOnClickListener(v -> {
            if (!Cart.getInstance().getItems().isEmpty()) {
                placeOrder();
            } else {
                showToast("Cart is empty");
            }
        });
    }

    private void placeOrder() {
        List<OrderItem> items = Cart.getInstance().getItems();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            if (email != null && !email.isEmpty()) {
                Log.d(TAG, "Email retrieved: " + email);

                Map<String, Object> orderSummary = new HashMap<>();
                orderSummary.put("email", email);
                orderSummary.put("status", "Pending");

                db.collection("users").document(email)
                        .set(orderSummary)
                        .addOnSuccessListener(aVoid -> {
                            // Add individual items as documents in a sub-collection
                            for (OrderItem item : items) {
                                Map<String, Object> orderItem = new HashMap<>();
                                orderItem.put("itemName", item.getItemName());
                                orderItem.put("price", item.getPrice());
                                orderItem.put("status", "Pending");

                                db.collection("users").document(email)
                                        .collection("orders").document()
                                        .set(orderItem)
                                        .addOnSuccessListener(docRef -> {
                                            Log.d(TAG, "Item added: " + item.getItemName());
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to add item: " + item.getItemName(), e);
                                        });
                            }

                            // Clear the cart
                            Cart.getInstance().clear();
                            showToast("Order placed for admin approval");
                            // Display estimated delivery time
                            showEstimatedDeliveryTime();
                        })
                        .addOnFailureListener(e -> showToast("Failed to place order: " + e.getMessage()));
            } else {
                showToast("Email not found");
                Log.e(TAG, "Email is empty or null");
            }
        } else {
            showToast("User not authenticated");
            Log.e(TAG, "User is not authenticated");
        }
    }

    private void showEstimatedDeliveryTime() {
        String estimatedTime = "45 minutes";  // Replace with actual call to backend
        Toast.makeText(this, "Estimated delivery time: " + estimatedTime, Toast.LENGTH_LONG).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
