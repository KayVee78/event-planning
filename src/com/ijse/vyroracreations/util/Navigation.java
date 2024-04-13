package com.ijse.vyroracreations.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
    private static Pane pane;

    public static void navigate(Routes route, Pane pane) throws IOException {
        Navigation.pane = pane;
        Navigation.pane.getChildren().clear();
        Stage window = (Stage)Navigation.pane.getScene().getWindow();

        switch (route) {
            case PURCHASABLE_PRODUCTS:
                window.setTitle("Purchasable Products View");
                initUI("PurchasableProductCatalogueForm.fxml");
                break;
            case RENTABLE_ITEMS:
                window.setTitle("Rentable Items View");
                initUI("RentableItemCatalogueForm.fxml");
                break;
            case VENDORS:
                window.setTitle("Vendors View");
                initUI("VendorCatalogueForm.fxml");
                break;
            case VENUES:
                window.setTitle("Venues View");
                initUI("VenueCatalogueForm.fxml");
                break;
            case MARKETPLACE:
                window.setTitle("Marketplace");
                initUI("MarketPlaceForm.fxml");
                break;
            case PLACE_ORDER:
                window.setTitle("Place Order");
                initUI("OrderProducts.fxml");
                break;
            case ORDER_DETAILS:
                window.setTitle("Order Details");
                initUI("ProductOrderDetailsForm.fxml");
                break;
            case HIRED_EMPLOYEES:
                window.setTitle("Hired Employees");
                initUI("HiredEmployeeForm.fxml");
                break;
            case CUSTOMERS:
                window.setTitle("Customer Registration");
                initUI("CustomerRegistrationForm.fxml");
                break;
            case LISTINGS:
                window.setTitle("Listings");
                initUI("ListingForm.fxml");
                break;
            case BOOK_RENTABLE:
                window.setTitle("Book Rentable");
                initUI("BookRentable.fxml");
                break;

            default:
                new Alert(Alert.AlertType.ERROR, "UI Not Found!").show();
        }
    }
    public static void initUI(String location) throws IOException {
        Navigation.pane.getChildren().add(FXMLLoader.load(Navigation.class.getResource("/com/ijse/vyroracreations/view/" + location)));
    }



}
