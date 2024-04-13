package com.ijse.vyroracreations.view.tm;

import javafx.scene.control.Button;

public class VenueTm {
    private String id;
    private String name;
    private String location;
    private String email;
    private String telNo;
    private Button btn;

    public VenueTm(String id, String name, String location, String email, String telNo, Button btn) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
        this.telNo = telNo;
        this.btn = btn;
    }

    public VenueTm() {
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
