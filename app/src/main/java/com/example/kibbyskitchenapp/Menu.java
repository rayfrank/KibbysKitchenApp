package com.example.kibbyskitchenapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });

        // Find TextViews by their IDs
        TextView meatSamosa = findViewById(R.id.item_meat_samosa);
        TextView vegetableSamosa = findViewById(R.id.vegetable_samosa);
        // Add more TextViews here...

        // Set click listeners for each TextView
        meatSamosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentConfirmationDialog("Meat Samosa (2 pcs)", 200);
            }
        });

        vegetableSamosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentConfirmationDialog("Vegetable Samosa (2 pcs)", 200);
            }
        });
        // Add click listeners for other items similarly...
    }

    // Method to show the payment confirmation dialog
    private void showPaymentConfirmationDialog(String itemName, int price)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment");
        builder.setMessage("Are you sure you want to purchase " + itemName + " for Ksh " + price + "?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Here you can implement the code to prompt the M-Pesa toolkit for payment
                // For demonstration purpose, I'll just show a toast
                // Replace this with the actual M-Pesa toolkit integration
                showToast("Payment initiated for " + itemName);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to show a toast message
    private void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
