package com.ijse.vyroracreations.view.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class ProductOrderTm {
    private String orderId;
    private String customerId;
    private Date date;
    private double totalCost;
    private Button btn;

    public ProductOrderTm(String orderId, String customerId, Date date, double totalCost, Button btn) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.totalCost = totalCost;
        this.btn = btn;
    }

    public ProductOrderTm() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
