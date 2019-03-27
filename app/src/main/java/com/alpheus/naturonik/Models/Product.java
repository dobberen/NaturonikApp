package com.alpheus.naturonik.Models;

public class Product {
    private int id;
    private String countryName;
    private String sortName;
    private String img;
    private String description;
    private String typePack;
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
        return countryName;
    }

    public String getSortName() {
        return sortName;
    }

    public String getTypePack() {
        return typePack;
    }
}

