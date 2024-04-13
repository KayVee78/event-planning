package com.ijse.vyroracreations.modal;

import java.util.ArrayList;
import java.util.Date;

public class RentableBooking {
    private String bookingId;
    private String customerId;
    private Date date;
    private double totalCost;
    private ArrayList<RentableDetails> rentableDetails;

    public RentableBooking(String bookingId, Date date, double totalCost, String customerId, ArrayList<RentableDetails> rentableDetails) {
        this.bookingId = bookingId;
        this.date = date;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.rentableDetails = rentableDetails;
    }

    public RentableBooking() {
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

    public ArrayList<RentableDetails> getRentableDetails() {
        return rentableDetails;
    }

    public void setRentableDetails(ArrayList<RentableDetails> rentableDetails) {
        this.rentableDetails = rentableDetails;
    }
}
