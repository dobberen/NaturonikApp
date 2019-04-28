package com.alpheus.naturonik.Models;

public class Product {
    private String countrys_name;
    private String sorts_name;
    private String img;
    private String description;
    private String type_pack;
    private String price;
    private String about;
    private String energy_value;
    private String nutritional_value;

    public Product() {
    }

    public Product(String countrys_name, String sorts_name, String img, String description, String type_pack, String price, String about, String energy_value, String nutritional_value) {
        this.countrys_name = countrys_name;
        this.sorts_name = sorts_name;
        this.img = img;
        this.description = description;
        this.type_pack = type_pack;
        this.price = price;
        this.about = about;
        this.energy_value = energy_value;
        this.nutritional_value = nutritional_value;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return img;
    }

    public String getPrice() {
        return price;
    }

    public String getCountryName() {
        return countrys_name;
    }

    public String getSortName() {
        return sorts_name;
    }

    public String getTypePack() {
        return type_pack;
    }

    public String getAbout() {
        return about;
    }

    public String getEnergy_value() {
        return energy_value;
    }

    public String getNutritional_value() {
        return nutritional_value;
    }
}

