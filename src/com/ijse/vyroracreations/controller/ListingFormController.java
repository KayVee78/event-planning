package com.ijse.vyroracreations.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ListingFormController {
    public AnchorPane listingButtonContext;

    public void openProductFormOnAction(ActionEvent event) throws IOException {
        initWindow("ProductForm", "Product Listing", false);

    }

    public void openRentableItemFormOnAction(ActionEvent event) throws IOException {
        initWindow("RentableItemForm", "Rentable Item Listing", false);
    }

    public void openVendorFormOnAction(ActionEvent event) throws IOException {
        initWindow("VendorForm", "Vendor Listing", false);
    }

    public void openVenueFormOnAction(ActionEvent event) throws IOException {
        initWindow("VenueForm", "Venue Listing", false);
    }

    /*public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) listingButtonContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }*/

    public void initWindow(String location, String title, boolean resize) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        Stage stage  = new Stage();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.NONE);
        stage.setResizable(resize);
        stage.setTitle(title);
        stage.initOwner(listingButtonContext.getScene().getWindow());
        stage.toFront();
        stage.show();
    }



}
