package com.ijse.vyroracreations.view.tm;

import javafx.scene.control.Button;

public class HiredEmployeeTm {
    private String id;
    private String name;
    private String address;
    private String email;
    private String telNo;
    private String specialization;
    private Double wages;
    private Button btn;

    public HiredEmployeeTm() {
    }

    public HiredEmployeeTm(String id, String name, String address, String email, String telNo, String specialization, Double wages, Button btn) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.telNo = telNo;
        this.specialization = specialization;
        this.wages = wages;
        this.btn = btn;
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

    public Double getWages() {
        return wages;
    }

    public void setWages(Double wages) {
        this.wages = wages;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
