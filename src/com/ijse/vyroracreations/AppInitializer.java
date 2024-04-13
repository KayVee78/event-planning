package com.ijse.vyroracreations;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class AppInitializer extends Application {

    public static final String CURRENCY = "Rs.";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/ijse/vyroracreations/view/MainForm.fxml"))));
        primaryStage.setTitle("Vyrora Creations");
        primaryStage.show();
    }
}
