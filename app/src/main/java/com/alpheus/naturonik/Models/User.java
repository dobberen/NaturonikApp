package com.alpheus.naturonik.Models;

public class User {

    public String user_img;
    private String user_name;
    private String telephone;
    private String vk_link;
    private String birthday;
    private String address;

    public User(String user_img, String user_name, String telephone, String vk_link, String birthday, String address) {
        this.user_img = user_img;
        this.user_name = user_name;
        this.telephone = telephone;
        this.vk_link = vk_link;
        this.birthday = birthday;
        this.address = address;
    }

    public User() {
    }

    public String getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getVk_link() {
        return vk_link;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setVk_link(String vk_link) {
        this.vk_link = vk_link;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
