package com.ijse.vyroracreations.view.tm;

import javafx.scene.control.Button;

public class RentableItemTm {
    private String code;
    private String description;
    private Double unitPrice;
    private int qtyOnHand;
    private Button btn;

    public RentableItemTm(String code, String description, Double unitPrice, int qtyOnHand, Button btn) {
        this.code = code;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.btn = btn;
    }

    public RentableItemTm() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
