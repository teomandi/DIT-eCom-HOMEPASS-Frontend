package com.example.frontmynbnb.models;

import android.graphics.Bitmap;

import java.util.List;

public class Place {

    private String createdAt=null;
    private String updatedAt=null;
    private int id=-1;
    private String mainImage;
    private Bitmap mainBitmap=null;
    private String address;
    private double latitude;
    private double longitude;
    private int maxGuests;
    private int minCost;
    private int costPerPerson;
    private String type;
    private String description;
    private int beds;
    private int baths;
    private int bedrooms;
    private boolean livingRoom;
    private int area;
    private List<Benefit> benefits;
    private List<Rule> rules;
    private List<Image> images;
    private List<Availability> availabilities;
    private List<Rating> ratings;

    public void printDetails(){
        System.out.println("Place Details");
        System.out.println("id: " + id);
        System.out.println("mainImage: " + mainImage);
        System.out.println("address: " + address);
        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);
        System.out.println("maxGuest: " + maxGuests);
        System.out.println("minCost: " + minCost);
        System.out.println("costPerPerson: " + costPerPerson);
        System.out.println("type: " + type);
        System.out.println("description: " + description);
        System.out.println("beds: " + beds);
        System.out.println("baths: " + baths);
        System.out.println("bedrooms: " + bedrooms);
        System.out.println("livingRoom: " + livingRoom);
        System.out.println("area: " + area);
        System.out.println("~~Benefits: ");
        if (benefits != null)
            for(Benefit b: benefits)
                System.out.println("- " + b.getContent());
        System.out.println("~~Rules: ");
        if (rules != null)
            for(Rule r: rules)
                System.out.println("- " + r.getContent());
        System.out.println("~~Images: ");
        if (images != null)
            for(Image i: images)
                System.out.println("- " + i.getId() + ") " + i.getFilename());
        System.out.println("~~Ratings: ");
        if (ratings != null)
            for(Rating r: ratings)
                System.out.println("- " + r.getId() + ") [" + r.getDegree() + "] " + r.getComment());
        System.out.println("~~Availabilities: ");
        if (availabilities != null)
            for(Availability a: availabilities)
                a.print();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(int costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public boolean isLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(boolean livingRoom) {
        this.livingRoom = livingRoom;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public List<Benefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<Benefit> benefits) {
        this.benefits = benefits;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Bitmap getMainBitmap() {
        return mainBitmap;
    }

    public void setMainBitmap(Bitmap mainBitmap) {
        this.mainBitmap = mainBitmap;
    }
}
