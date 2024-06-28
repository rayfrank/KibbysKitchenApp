package com.example.kibbyskitchenapp;

import android.os.Bundle;
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

public class CartActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TextView cartItemsTextView = findViewById(R.id.cart_items);
        Button placeOrderButton = findViewById(R.id.place_order_button);

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
            String userId = user.getUid();
            Map<String, Object> order = new HashMap<>();
            order.put("userId", userId);
            order.put("items", items);
            order.put("status", "Pending");

            db.collection("orders")
                    .add(order)
                    .addOnSuccessListener(documentReference -> {
                        // Clear the cart
                        Cart.getInstance().clear();

                        showToast("Order placed for admin approval");

                        // Display estimated delivery time
                        showEstimatedDeliveryTime();
                    })
                    .addOnFailureListener(e -> showToast("Failed to place order: " + e.getMessage()));
        } else {
            showToast("User not authenticated");
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