package com.example.babyneeds.model;

public class Item {
    private int id;
    private String name;
    private String color;
    private int quantity;
    private String size;
    private String dateItemAdded;

    public Item() {
    }

    public Item(String name, String color, int quantity, String size, String dateItemAdded) {
        this.name = name;
        this.color = color;
        this.quantity = quantity;
        this.size = size;
        this.dateItemAdded = dateItemAdded;
    }

    public Item(int id, String name, String color, int quantity, String size, String dateItemAdded) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.quantity = quantity;
        this.size = size;
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }
}
