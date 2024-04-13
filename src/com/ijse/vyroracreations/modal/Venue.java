package com.ijse.vyroracreations.modal;

public class Venue {
    private String id;
    private String name;
    private String location;
    private String email;
    private String telNo;

    public Venue(String id, String name, String location, String email, String telNo) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
        this.telNo = telNo;
    }

    public Venue() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
}
