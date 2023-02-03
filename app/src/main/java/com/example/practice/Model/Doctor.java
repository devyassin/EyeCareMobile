package com.example.practice.Model;

public class Doctor {
    public String name, email, mobile, ville,hopitale,specialite, password,imageUser;

    public Doctor(){

    }

    public Doctor(String name, String email, String mobile, String ville, String hopitale, String specialite, String password, String imageUser) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.ville = ville;
        this.hopitale = hopitale;
        this.specialite = specialite;
        this.password = password;
        this.imageUser = imageUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getHopitale() {
        return hopitale;
    }

    public void setHopitale(String hopitale) {
        this.hopitale = hopitale;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }
}