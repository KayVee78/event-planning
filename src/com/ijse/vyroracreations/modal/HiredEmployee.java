package com.ijse.vyroracreations.modal;

public class HiredEmployee {
    private String id;
    private String name;
    private String address;
    private String email;
    private String telNo;
    private String specialization;
    private double wages;

    public HiredEmployee() {
    }

    public HiredEmployee(String id, String name, String address, String email, String telNo, String specialization, double wages) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.telNo = telNo;
        this.specialization = specialization;
        this.wages = wages;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getWages() {
        return wages;
    }

    public void setWages(double wages) {
        this.wages = wages;
    }
}
