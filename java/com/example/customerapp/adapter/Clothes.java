package com.example.customerapp.adapter;

import java.io.Serializable;

public class Clothes implements Serializable {
    private String Name;
    private String ImageUrl;
    private Double Price;
    private long Quantity;
    private int maxQuantity;
    private boolean isSelected;
    // Constructor
    public Clothes(String Name, Double Price, String ImageUrl) {
        this.Name = Name;
        this.Price = Price;
        this.ImageUrl = ImageUrl;
        this.Quantity = Quantity;
        this.maxQuantity = 0;
    }

    // Getters
    public String getName() {
        return Name;
    }

    public Double getPrice() {
        return Price;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public long getQuantity() { // Getter for quantity
        return Quantity;
    }

    public int getMaxQuantity() { // Add this method
        return maxQuantity;
    }

    // Setters
    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPrice(Double Price) {
        this.Price = Price;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public void setQuantity(long Quantity) { // Setter for quantity
        this.Quantity = Quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setMaxQuantity(int maxQuantity) { // Add this method
        this.maxQuantity = maxQuantity;
    }
}
