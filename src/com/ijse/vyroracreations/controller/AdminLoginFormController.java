package com.ijse.vyroracreations.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Pattern;

public class AdminLoginFormController {
    public AnchorPane adminLoginContext;
    public Label invalidUsernameLbl;
    public Label invalidPasswordLbl;
    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    public ImageView progress;


    private Pattern userNamePattern;
    private Pattern passwordPattern;

    public void initialize(){
        userNamePattern = Pattern.compile("kithmi");
        passwordPattern = Pattern.compile("880");
    }

    public void btnGoBackOnAction(ActionEvent event) throws IOException {
        setUi("MainForm", "Vyrora Creations", true);
    }

    public void openAdminDashboardOnAction(ActionEvent event) throws IOException {
        boolean isUsernameMatched =  userNamePattern.matcher(txtUsername.getText()).matches();
        boolean isPasswordMatched =  passwordPattern.matcher(txtPassword.getText()).matches();

        if(isUsernameMatched){
            invalidUsernameLbl.setVisible(false);
            if(isPasswordMatched){
                setUi("AdminDashboardForm", "Dashboard", false);

            }else {
                invalidPasswordLbl.setVisible(true);
                txtPassword.setFocusColor(Paint.valueOf("red"));
                txtPassword.requestFocus();
            }

        }else{
            invalidUsernameLbl.setVisible(true);
            txtPassword.setFocusColor(Paint.valueOf("red"));
            txtUsername.requestFocus();
        }
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) adminLoginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }


}
