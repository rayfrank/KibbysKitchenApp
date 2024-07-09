package com.kibbyskitchen.kibbyskitchenapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kibbyskitchen.kibbyskitchenapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private static final String CHANNEL_ID = "order_channel";
    private static final int PERMISSIONS_REQUEST_CODE = 1;
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

        createNotificationChannel();
        requestPermissions();
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        // Permissions granted, proceed with order placement
                        placeOrder();
                    } else {
                        showToast("Storage permissions are required to place an order.");
                    }
                } else {
                    // Permissions granted, proceed with order placement
                    placeOrder();
                }
            } else {
                showToast("Notification permission is required to place an order.");
            }
        }
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

                db.collection("users").document(email).collection("orders").add(orderSummary)
                        .addOnSuccessListener(documentReference -> {
                            // Add individual items as documents in a sub-collection
                            for (OrderItem item : items) {
                                Map<String, Object> orderItem = new HashMap<>();
                                orderItem.put("itemName", item.getItemName());
                                orderItem.put("price", item.getPrice());
                                orderItem.put("status", "Pending");

                                db.collection("users").document(email).collection("orders")
                                        .document(documentReference.getId()).collection("items").add(orderItem)
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

                            // Create a notification for the order
                            createOrderNotification(email, items);
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

    private void createOrderNotification(String email, List<OrderItem> items) {
        String itemCount = String.valueOf(items.size());
        String message = "You have placed a new order with " + itemCount + " items.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("New Order")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Channel";
            String description = "Channel for order notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
