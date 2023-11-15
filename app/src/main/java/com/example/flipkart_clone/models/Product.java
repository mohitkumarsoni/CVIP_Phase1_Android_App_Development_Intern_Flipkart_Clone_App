package com.example.flipkart_clone.models;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Item, Serializable {
    private String name, image, status;
    private double price, discount, final_discount_price;
    private int id, quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFinal_discount_price() {
        return final_discount_price;
    }

    public void setFinal_discount_price(double final_discount_price) {
        this.final_discount_price = final_discount_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    private int stock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product() {
    }

    public Product(String name, String image, String status, double price, double discount,int stock, int id) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.id = id;
    }

    @Override
    public BigDecimal getItemPrice() {
        return new BigDecimal(final_discount_price);
    }

    @Override
    public String getItemName() {
        return name;
    }
}
