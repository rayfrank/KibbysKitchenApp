package com.kibbyskitchen.kibbyskitchenapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        buttonLogout = findViewById(R.id.buttonLogout);

        if (currentUser != null) {
            textViewUserEmail.setText("User Email: " + currentUser.getEmail());
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }

        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(Menu.this, LoginActivity.class));
            finish();
        });

        // Find TextViews by their IDs
        TextView meatSamosa = findViewById(R.id.item_meat_samosa);
        TextView vegetableSamosa = findViewById(R.id.vegetable_samosa);
        TextView pannerSamosa = findViewById(R.id.Panner_samosa);
        TextView meatPie = findViewById(R.id.meatPie);
        TextView cheesePie = findViewById(R.id.cheesepie);
        TextView chickenPie = findViewById(R.id.Chicken_Pie);
        TextView teaScones = findViewById(R.id.TeaScones);
        TextView plainCroissant = findViewById(R.id.PlainCroissant);
        TextView chocolateCroissant = findViewById(R.id.ChocolateCroissant);
        TextView almondflakesCroissant = findViewById(R.id.AlmondflakesCroissant);
        TextView danishPastry = findViewById(R.id.DanishPastry);
        TextView danishBlueberry = findViewById(R.id.DanishBlueBerry);
        TextView frenchBauguette = findViewById(R.id.FrenchBauguette);
        TextView kibbySandwichBread = findViewById(R.id.KibbySandwichbread);
        TextView kibbysGarlicCheeseBread = findViewById(R.id.KibbysGarliccheesebread);
        TextView kibbyBananaBread = findViewById(R.id.KibbyBananabread);
        TextView kibbySweetBread = findViewById(R.id.KibbySweetbread);
        TextView kibbyMultigrainCerealBread = findViewById(R.id.KibbyMultigraincerealbread);
        TextView kibbysBurgerBuns = findViewById(R.id.KibbysBurgerbuns);
        TextView kibbysHotdogBuns = findViewById(R.id.KibbysHotdogbuns);
        TextView kibbysFrenchBaguette = findViewById(R.id.KibbysFrenchbaguette);
        TextView kibbysGarlicBread = findViewById(R.id.KibbysGarlicbread);
        TextView kibbysFocassioBread = findViewById(R.id.KibbysFocassiobread);

        // Set click listeners for each TextView
        meatSamosa.setOnClickListener(v -> showPaymentConfirmationDialog("Meat Samosa (2 pcs)", 200));
        vegetableSamosa.setOnClickListener(v -> showPaymentConfirmationDialog("Vegetable Samosa (2 pcs)", 200));
        pannerSamosa.setOnClickListener(v -> showPaymentConfirmationDialog("Panner Samosa (2 pcs)", 200));
        meatPie.setOnClickListener(v -> showPaymentConfirmationDialog("Meat Pie", 180));
        cheesePie.setOnClickListener(v -> showPaymentConfirmationDialog("Cheese Pie", 180));
        chickenPie.setOnClickListener(v -> showPaymentConfirmationDialog("Chicken Pie", 200));
        teaScones.setOnClickListener(v -> showPaymentConfirmationDialog("Tea Scones", 100));
        plainCroissant.setOnClickListener(v -> showPaymentConfirmationDialog("Plain Croissant", 200));
        chocolateCroissant.setOnClickListener(v -> showPaymentConfirmationDialog("Chocolate Croissant", 230));
        almondflakesCroissant.setOnClickListener(v -> showPaymentConfirmationDialog("Almond flakes Croissant", 250));
        danishPastry.setOnClickListener(v -> showPaymentConfirmationDialog("Danish Pastry", 200));
        danishBlueberry.setOnClickListener(v -> showPaymentConfirmationDialog("Danish Blueberry", 230));
        frenchBauguette.setOnClickListener(v -> showPaymentConfirmationDialog("French Bauguette", 200));
        kibbySandwichBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby's Sandwich bread", 200));
        kibbysGarlicCheeseBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby's Garlic cheese bread", 200));
        kibbyBananaBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby's Banana bread", 220));
        kibbySweetBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby's Sweet bread", 220));
        kibbyMultigrainCerealBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby's Multi grain cereal bread", 220));
        kibbysBurgerBuns.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby’s Burger buns (4pcs)", 120));
        kibbysHotdogBuns.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby’s Hot dog buns (4pcs)", 120));
        kibbysFrenchBaguette.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby’s French baguette", 150));
        kibbysGarlicBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby’s Garlic bread", 150));
        kibbysFocassioBread.setOnClickListener(v -> showPaymentConfirmationDialog("Kibby’s Focassio bread", 150));

        // Button to view the cart
        findViewById(R.id.view_cart_button).setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, CartActivity.class);
            startActivity(intent);
        });
    }

    // Method to show the payment confirmation dialog
    private void showPaymentConfirmationDialog(String itemName, int price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order");
        builder.setMessage("Are you sure you want to order " + itemName + " for KES " + price + "?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cart.getInstance().addItem(new OrderItem(itemName, price));
                showToast(itemName + " added to cart");
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
