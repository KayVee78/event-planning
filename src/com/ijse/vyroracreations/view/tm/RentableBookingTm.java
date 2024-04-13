package com.ijse.vyroracreations.view.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class RentableBookingTm {
    private String bookingId;
    private String customerId;
    private Date date;
    private double totalCost;
    private Button btn;

    public RentableBookingTm(String bookingId, String customerId, Date date, double totalCost, Button btn) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.date = date;
        this.totalCost = totalCost;
        this.btn = btn;
    }

    public RentableBookingTm() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
