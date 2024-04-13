package com.ijse.vyroracreations.modal;

import java.util.ArrayList;
import java.util.Date;

public class ProductOrder {
    private String orderId;
    private String customerId;
    private Date date;
    private double totalCost;
    private ArrayList<ProductDetails> productDetails;

    public ProductOrder(String orderId, Date date, double totalCost, String customerId, ArrayList<ProductDetails> productDetails) {
        this.orderId = orderId;
        this.date = date;
        this.totalCost = totalCost;
        this.customerId = customerId;
        this.productDetails = productDetails;
    }

    public ProductOrder() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ArrayList<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }
}
