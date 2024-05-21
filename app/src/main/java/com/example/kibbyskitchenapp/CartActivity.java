package com.example.kibbyskitchenapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        // Simulate sending order to backend
        // Replace with actual backend call
        showToast("Order placed");

        // Clear the cart
        Cart.getInstance().clear();

        // Display estimated delivery time
        showEstimatedDeliveryTime();
    }

    private void showEstimatedDeliveryTime() {
        // Simulate getting estimated delivery time from backend
        String estimatedTime = "45 minutes";  // Replace with actual call to backend
        Toast.makeText(this, "Estimated delivery time: " + estimatedTime, Toast.LENGTH_LONG).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
