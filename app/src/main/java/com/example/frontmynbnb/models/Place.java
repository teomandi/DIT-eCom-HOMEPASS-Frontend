package com.example.frontmynbnb.models;

import java.util.List;

public class Place {

    private String createdAt=null;
    private String updatedAt=null;
    private int id=-1;
    private String mainImage;
    private String address;
    private String latitude;
    private String longitude;
    private int maxGuest;
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

    public void printDetails(){
        System.out.println("Place Details");
        System.out.println("id: " + id);
        System.out.println("mainImage: " + mainImage);
        System.out.println("address: " + address);
        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);
        System.out.println("maxGuest: " + maxGuest);
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
        for(Benefit b: benefits)
            System.out.println("- " + b.getContent());
        System.out.println("~~Rules: ");
        for(Rule r: rules)
            System.out.println("- " + r.getContent());
        System.out.println("~~Images: ");
        for(Image i: images)
            System.out.println("- " + i.getId() + ") " + i.getFilename());
        System.out.println("~~Availabilities: ");
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
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
}
