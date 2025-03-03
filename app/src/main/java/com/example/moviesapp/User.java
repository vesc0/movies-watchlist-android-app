package com.example.moviesapp;

public class User {
    private String name;
    private String email;
    private String favoriteGenre;

    public User(String name, String email, String favoriteGenre) {
        this.name = name;
        this.email = email;
        this.favoriteGenre = favoriteGenre;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }
}
