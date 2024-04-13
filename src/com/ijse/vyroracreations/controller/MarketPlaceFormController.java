package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.util.Navigation;
import com.ijse.vyroracreations.util.Routes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MarketPlaceFormController {


    public AnchorPane marketPlaceContext;

    @FXML
    private AnchorPane pane;

    public void openPurchasableProductView(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PURCHASABLE_PRODUCTS, pane);
    }

    public void openRentableItemView(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.RENTABLE_ITEMS, pane);
    }

    public void openVendorView(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.VENDORS, pane);
    }

    public void openVenueView(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.VENUES, pane);
    }

    public void btnGoBackOnAction(ActionEvent event) throws IOException {
        setUi("AdminDashboardForm", "Admin Dashboard", false);
    }

    public void openEventPlanningOnAction(ActionEvent event) throws IOException {
        setUi("EventPlanningForm", "Event Planning Window", false);
    }

    public void openPlaceOrderOnAction(ActionEvent event) throws IOException {
        setUi("PlaceOrderForm", "Place Order", false);
    }

    public void openPurchasableProducts(ActionEvent event) throws IOException {
        setUi("PurchasableProductCatalogueForm","Products Catalogue", false);
    }

    public void openRentableItems(ActionEvent event) throws IOException {
        setUi("RentableItemCatalogueForm","Rentable Items Catalogue",false);

    }

    public void openVenues(ActionEvent event) throws IOException {
        setUi("VenueCatalogueForm","Venue Catalogue", false);
    }

    public void openVendors(ActionEvent event) throws IOException {
        setUi("VendorCatalogueForm","Vendor Catalogue", false);
    }

    public void goHomeOnAction(ActionEvent event) throws IOException {
        setUi("AdminDashboardForm","Dashboard", false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) marketPlaceContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }



}
