package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.RentableItem;
import com.ijse.vyroracreations.view.tm.ProductTm;
import com.ijse.vyroracreations.view.tm.RentableItemTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class RentableItemFormController {
    public AnchorPane rentableForm;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtSearch;
    public Button btnSaveRentable;
    public TableView<RentableItemTm> tblRentable;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOption;
    private String searchText="";
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchRentable(searchText);

        tblRentable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(null!=newValue){ //newValue!=null (this consumes more time)
                setData(newValue);
            }
        }));
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchRentable(searchText);
        }));

        btnSaveRentable.setDisable(true);

        Pattern codePattern = Pattern.compile("^(P00)[0-9]{1,5}$");
        Pattern descriptionPattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern unitPricePattern = Pattern.compile("(?!0+(?:\\.0+)?$)[0-9]+(?:\\.[0-9]+)?");
        Pattern quantityPattern = Pattern.compile("^[0-9]{1,4}");

        map.put(txtCode, codePattern);
        map.put(txtDescription, descriptionPattern);
        map.put(txtUnitPrice, unitPricePattern);
        map.put(txtQtyOnHand, quantityPattern);

    }

    private void setData(RentableItemTm tm) {
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
        btnSaveRentable.setText("Update Rentable");
        btnSaveRentable.setStyle("-fx-background-color: #FFFF1A; -fx-text-fill: black;");

    }

    public void btnSaveRentableOnAction(ActionEvent event) {
        RentableItem ri1 = new RentableItem(txtCode.getText(), txtDescription.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText()));

        if(btnSaveRentable.getText().equalsIgnoreCase("Save Rentable")){
            try {
                saveRentable();
            }catch (ClassNotFoundException | SQLException e){ //compile time exception
                e.printStackTrace();
            }
        }else{
            //-----------------------------------Update--------------------------------------------------
            try {
                String sql = "UPDATE rentable_item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, ri1.getDescription());
                statement.setDouble(2, ri1.getUnitPrice());
                statement.setInt(3, ri1.getQtyOnHand());
                statement.setString(4, ri1.getCode());
                if(statement.executeUpdate()>0){
                    searchRentable(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Rentable Item Updated!").show();
                }else{
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();

                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void searchRentable(String text){
        String searchText="%"+text+"%";
        try {
            ObservableList<RentableItemTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM rentable_item WHERE code LIKE ? || description LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Button btn = new Button("Delete");
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                RentableItemTm tm = new RentableItemTm(
                        set.getString(1),
                        set.getString(2),
                        set.getDouble(3),
                        set.getInt(4), btn);
                tmList.add(tm);

                //----------------------------Delete--------------------------------------------
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this rentable_item?",ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        //-------------------connecting database
                        try {
                            String sql1 = "DELETE FROM rentable_item WHERE code=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getCode());

                            if(statement1.executeUpdate()>0){
                                searchRentable(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Rentable Item Deleted").show();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
            tblRentable.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnNewRentable(ActionEvent event) {
        btnSaveRentable.setText("Save Rentable");
        btnSaveRentable.setStyle("-fx-background-color: #27ae60");
        clearFields();
    }

    private void saveRentable() throws SQLException, ClassNotFoundException {
        RentableItem ri1 = new RentableItem(txtCode.getText(), txtDescription.getText(), Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText()));
        String sql = "INSERT INTO rentable_item VALUES (?,?,?,?)";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, ri1.getCode());
        statement.setString(2, ri1.getDescription());
        statement.setDouble(3, ri1.getUnitPrice());
        statement.setInt(4, ri1.getQtyOnHand());
        if(statement.executeUpdate()>0){
            searchRentable(searchText);
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Rentable Item Saved!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();
        }
    }

    private void clearFields(){
        txtCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    public void openRentableCatalogueOnAction(ActionEvent event) throws IOException {
        setUi("RentableItemCatalogueForm", "Rentable Item Catalogue", false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) rentableForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }

    public void txtFieldKeyReleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        Object validate = validate();

        //if enter key is pressed
        if(keyEvent.getCode() == KeyCode.ENTER){
            Object response = validate();
            //if the response is a text field
            //that means there is an error
            if(response instanceof TextField){
                TextField textField = (TextField) response;
                textField.requestFocus(); //if there is an error focus it
            }else if(response instanceof Boolean){
                saveRentable();

            }

        }
    }




    private Object validate() {
        for(TextField key : map.keySet()){
            Pattern pattern = map.get(key);
            if(!pattern.matcher(key.getText()).matches()){
                //if the input is not matching
                addError(key);
                return key;
            }
            removeError(key);
        }
        return true;
    }

    private void addError(TextField textField) {
        if(textField.getText().length()>0){
            textField.setStyle("-fx-border-color: #EE444A; -fx-border-width: 3");
            btnSaveRentable.setDisable(true);
        }

    }

    private void removeError(TextField textField) {
        textField.setStyle("-fx-border-color: #00FF00; -fx-border-width: 3");
        btnSaveRentable.setDisable(false);
    }

}
