package com.kibbyskitchen.kibbyskitchenapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kibbyskitchen.kibbyskitchenapp.R;

import java.util.HashSet;
import java.util.Set;

public class AdminActivity extends AppCompatActivity {

    private TextView textViewOrders;
    private Button buttonApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminactivity);

        textViewOrders = findViewById(R.id.textViewOrders);
        buttonApprove = findViewById(R.id.buttonApprove);

        // Display orders
        displayOrders();

        buttonApprove.setOnClickListener(v -> {
            // Approve orders
            approveOrders();
        });
    }

    private void displayOrders() {
        SharedPreferences preferences = getSharedPreferences("ORDERS_DATA", MODE_PRIVATE);
        Set<String> orders = preferences.getStringSet("orders", new HashSet<>());

        StringBuilder ordersList = new StringBuilder();
        for (String order : orders) {
            ordersList.append(order).append("\n");
        }
        textViewOrders.setText(ordersList.toString());
    }

    private void approveOrders() {
        SharedPreferences preferences = getSharedPreferences("ORDERS_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("orders");
        editor.apply();

        textViewOrders.setText("");
        Toast.makeText(this, "Orders approved", Toast.LENGTH_SHORT).show();
    }
}
