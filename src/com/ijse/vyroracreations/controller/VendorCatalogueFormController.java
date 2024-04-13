package com.ijse.vyroracreations.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class VendorCatalogueFormController {
    public AnchorPane vendorCatalogue;

    public void btnGoBackOnAction(ActionEvent event) throws IOException {
        setUi("MarketPlaceForm","Marketplace",false);
    }

    public void openVendorFormOnAction(ActionEvent event) throws IOException {
        setUi("VendorForm","Vendor Listings",false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) vendorCatalogue.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }
}
