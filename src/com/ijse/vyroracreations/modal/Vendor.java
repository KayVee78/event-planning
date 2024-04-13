package com.ijse.vyroracreations.modal;

public class Vendor {
    private String id;
    private String companyName;
    private String address;
    private String telNo;
    private String email;

    public Vendor(String id, String companyName, String address, String telNo, String email) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.telNo = telNo;
        this.email = email;
    }

    public Vendor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
