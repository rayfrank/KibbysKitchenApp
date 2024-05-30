package com.example.kibbyskitchenapp;

public class OrderItem {
    private String itemName;
    private int price;

    // No-argument constructor required for Firestore
    public OrderItem() {
    }

    public OrderItem(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }
}
