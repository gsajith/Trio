package com.volley.model;

import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl;
    //private int year;
    //private double rating;
    //private ArrayList<String> genre;
    private String price;
    private String location;
    private String time;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, /*int year, double rating,
                 ArrayList<String> genre*/String price, String location, String time) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        //this.year = year;
        //this.rating = rating;
        //this.genre = genre;
        this.price = price;
        this.location = location;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }*/

}