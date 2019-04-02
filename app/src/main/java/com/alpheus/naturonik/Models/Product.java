package com.alpheus.naturonik.Models;

public class Product {
    private int id;
    private String countrys_name;
    private String sorts_name;
    private String img;
    private String description;
    private String type_pack;
    private String price;

    public Product() {
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

    public int getId() {
        return id;
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
}

