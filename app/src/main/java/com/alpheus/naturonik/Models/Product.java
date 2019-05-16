package com.alpheus.naturonik.Models;

import com.google.firebase.database.Exclude;

public class Product {
    private String countrys_name;
    private String sorts_name;
    public String img;
    private String description;
    private String price;
    private String about;
    private String energy_value;
    private String nutritional_value;

    public Product() {
    }

    public Product(String countrys_name, String sorts_name, String img, String description,
                   String price, String about, String energy_value, String nutritional_value) {
        this.countrys_name = countrys_name;
        this.sorts_name = sorts_name;
        this.img = img;
        this.description = description;
        this.price = price;
        this.about = about;
        this.energy_value = energy_value;
        this.nutritional_value = nutritional_value;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getPrice() {
        return price;
    }

    public String getCountrys_name() {
        return countrys_name;
    }

    public String getSorts_name() {
        return sorts_name;
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


    public void setCountrys_name(String countrys_name) {
        this.countrys_name = countrys_name;
    }

    public void setSorts_name(String sorts_name) {
        this.sorts_name = sorts_name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setEnergy_value(String energy_value) {
        this.energy_value = energy_value;
    }

    public void setNutritional_value(String nutritional_value) {
        this.nutritional_value = nutritional_value;
    }
}

