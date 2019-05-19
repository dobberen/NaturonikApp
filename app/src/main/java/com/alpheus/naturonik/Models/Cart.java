package com.alpheus.naturonik.Models;

public class Cart {

    public String img;
    private String description;
    private String price;
    private String total_price;
    private String amount;
    private String energy_value;
    private String nutritional_value;

    public Cart(String img, String description, String price, String total_price, String amount, String energy_value,
                String nutritional_value) {
        this.img = img;
        this.description = description;
        this.price = price;
        this.total_price = total_price;
        this.amount = amount;
        this.energy_value = energy_value;
        this.nutritional_value = nutritional_value;
    }

    public Cart(){

    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getEnergy_value() {
        return energy_value;
    }

    public String getNutritional_value() {
        return nutritional_value;
    }

    public String getTotal_price() {
        return total_price;
    }
}
