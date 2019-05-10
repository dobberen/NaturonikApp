package com.alpheus.naturonik.Models;

public class Favourite {

    public String countrys_name;
    public String sorts_name;
    public String img;
    public String description;
    public String price;
    public String about;
    public String energy_value;
    public String nutritional_value;

    public Favourite(String countrys_name, String sorts_name, String img, String description,
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


}
