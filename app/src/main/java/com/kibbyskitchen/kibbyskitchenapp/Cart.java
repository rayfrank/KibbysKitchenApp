package com.kibbyskitchen.kibbyskitchenapp;

import com.kibbyskitchen.kibbyskitchenapp.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> items;
    private static Cart instance;

    private Cart() {
        items = new ArrayList<>();
    }

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}
