package com.ijse.vyroracreations.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormController {

    public BorderPane mainFormContext;

    public void openAdminLoginOnAction(ActionEvent event) throws IOException {
        setUi("AdminLoginForm","Admin Login", false);

    }

    public void openCustomerLoginOnAction(ActionEvent event) throws IOException {
        setUi("CustomerLoginForm","Customer Login", false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) mainFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }
}
