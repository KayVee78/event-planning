package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.util.Navigation;
import com.ijse.vyroracreations.util.Routes;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardFormController {
    public AnchorPane adminDashboardContext;
    public AnchorPane pane;


    public void openMarketPlaceOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.MARKETPLACE, pane);
    }

    public void openPlaceOrderOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PLACE_ORDER, pane);
    }

    public void openOrderDetailsOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.ORDER_DETAILS, pane);
    }

    public void openHiredEmployees(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.HIRED_EMPLOYEES, pane);
    }

    public void openCustomerRegistration(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.CUSTOMERS, pane);
    }

    public void openListingsOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.LISTINGS, pane);
    }






    /*public void openMarketplaceOnAction(ActionEvent event) throws IOException {
        setUi("MarketPlaceForm","Marketplace", false);
    }

    public void openHiredEmployeeDetailsOnAction(ActionEvent event) throws IOException {
        setUi("HiredEmployeeForm", "Hired Employee Details", false);
    }

    public void openPlaceOrderOnAction(ActionEvent event) throws IOException {
        setUi("PlaceOrderForm", "Place Order", true);
    }

    public void openAdminOrderDetailsOnAction(ActionEvent event) throws IOException {
        setUi("AdminOrderDetailsForm", "Order Details", true);
    }

    public void openRegisteredUsersOnAction(ActionEvent event) throws IOException {
        setUi("RegisteredUsersForm", "Registered Customers", false);
    }

    public void btnGoBackOnAction(ActionEvent event) throws IOException {
        setUi("AdminLoginForm", "Admin Login", false);
    }

    public void openCustomerRegistrationOnAction(ActionEvent event) throws IOException {
        setUi("CustomerRegistrationForm", "Customer Registration", false);
    }*/

    /*public void openCustomerRegistration(ActionEvent event) throws IOException {
        initWindow("CustomerRegistrationForm");
    }*/
    public void goBackOnAction(ActionEvent event) throws IOException {
        setUi("AdminLoginForm", "Admin Login", false);
    }

    public void openHomeOnAction(ActionEvent event) throws IOException {
        setUi("AdminDashboardForm", "Dashboard", false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) adminDashboardContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }

    public void initWindow(String location, String title, boolean resize) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        Stage stage  = new Stage();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.NONE);
        stage.setResizable(resize);
        stage.setTitle(title);
        stage.initOwner(adminDashboardContext.getScene().getWindow());
        stage.toFront();
        stage.show();
    }



}
