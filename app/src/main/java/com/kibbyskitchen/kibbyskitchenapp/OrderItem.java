package com.kibbyskitchen.kibbyskitchenapp;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String itemName;
    private int price;

    public OrderItem() {
        // Default constructor required for calls to DataSnapshot.getValue(OrderItem.class)
    }

    public OrderItem(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
