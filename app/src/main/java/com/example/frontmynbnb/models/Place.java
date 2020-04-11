package com.example.frontmynbnb.models;

public class Place {

    private int image; //drawable id
    private String country, city, name;
    private float costPerDay, getCostPerPerson;
    private float rating;

    public Place(int image, String country, String city, String name,
                 float costPerDay, float getCostPerPerson, float rating) {
        this.image = image;
        this.country = country;
        this.city = city;
        this.name = name;
        this.costPerDay = costPerDay;
        this.getCostPerPerson = getCostPerPerson;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(float costPerDay) {
        this.costPerDay = costPerDay;
    }

    public float getGetCostPerPerson() {
        return getCostPerPerson;
    }

    public void setGetCostPerPerson(float getCostPerPerson) {
        this.getCostPerPerson = getCostPerPerson;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
