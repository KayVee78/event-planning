package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.Customer;
import com.ijse.vyroracreations.modal.Product;
import com.ijse.vyroracreations.modal.Venue;
import com.ijse.vyroracreations.view.tm.ProductTm;
import com.ijse.vyroracreations.view.tm.VenueTm;
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

public class VenueFormController {
    public AnchorPane VenueForm;
    public TextField txtId;
    public TextField txtName;
    public TextField txtLocation;
    public TextField txtEmail;
    public TextField txtTelNo;
    public TextField txtSearch;
    public Button btnSaveVenue;
    public TableView<VenueTm> tblVenue;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colLocation;
    public TableColumn colEmail;
    public TableColumn colTelNo;
    public TableColumn colOption;
    private String searchText="";
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelNo.setCellValueFactory(new PropertyValueFactory<>("telNo"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchVenues(searchText);

        tblVenue.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(null!=newValue){ //newValue!=null (this consumes more time)
                setData((VenueTm) newValue);
            }
        }));
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchVenues(searchText);
        }));

        btnSaveVenue.setDisable(true);

        Pattern idPattern = Pattern.compile("^(V00)[0-9]{1,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern locationPattern = Pattern.compile("^[A-z0-9]{3,100}$");
        Pattern telNoPattern = Pattern.compile("^(\\+94)[0-9]{9}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

        map.put(txtId, idPattern);
        map.put(txtName, namePattern);
        map.put(txtLocation, locationPattern);
        map.put(txtEmail, emailPattern);
        map.put(txtTelNo, telNoPattern);


    }

    private void setData(VenueTm tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtLocation.setText(tm.getLocation());
        txtEmail.setText(tm.getEmail());
        txtTelNo.setText(tm.getTelNo());
        btnSaveVenue.setText("Update Venue");
        btnSaveVenue.setStyle("-fx-background-color: #FFFF1A; -fx-text-fill: black;");

    }

    private void searchVenues(String text){
        String searchText="%"+text+"%";
        try {
            ObservableList<VenueTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM venue WHERE id LIKE ? || name LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Button btn = new Button("Delete");
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                VenueTm tm = new VenueTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5), btn);
                tmList.add(tm);

                //----------------------------Delete--------------------------------------------
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this venue?",ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        //-------------------connecting database
                        try {
                            String sql1 = "DELETE FROM venue WHERE id=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getId());

                            if(statement1.executeUpdate()>0){
                                searchVenues(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Venue Deleted").show();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
            tblVenue.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public void btnNewVenue(ActionEvent event) {
        btnSaveVenue.setText("Save Venue");
        btnSaveVenue.setStyle("-fx-background-color: #27ae60");
        clearFields();
    }

    public void btnSaveVenueOnAction(ActionEvent event) {
        Venue v1 = new Venue(txtId.getText(), txtName.getText(), txtLocation.getText(), txtEmail.getText(), txtTelNo.getText());

        if(btnSaveVenue.getText().equalsIgnoreCase("Save Venue")){
            //-------------------database connecting----------------------------------------
            try {
                saveVenue();
            }catch (ClassNotFoundException | SQLException e){ //compile time exception
                e.printStackTrace();
            }
        }else{
            //-----------------------------------Update--------------------------------------------------
            try {
                String sql = "UPDATE venue SET name=?, location=?, email=?, telNo=? WHERE id=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, v1.getName());
                statement.setString(2, v1.getLocation());
                statement.setString(3, v1.getEmail());
                statement.setString(4, v1.getTelNo());
                statement.setString(5, v1.getId());
                if(statement.executeUpdate()>0){
                    searchVenues(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Venue Updated!").show();
                }else{
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();

                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openVenueCatalogueOnAction(ActionEvent event) throws IOException {
        setUi("VenueCatalogueForm","Venue Catalogue", false);
    }

    private void saveVenue() throws SQLException, ClassNotFoundException {
        Venue v1 = new Venue(txtId.getText(), txtName.getText(), txtLocation.getText(), txtEmail.getText(), txtTelNo.getText());
        String sql = "INSERT INTO venue VALUES (?,?,?,?,?)";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, v1.getId());
        statement.setString(2, v1.getName());
        statement.setString(3, v1.getLocation());
        statement.setString(4, v1.getEmail());
        statement.setString(5, v1.getTelNo());
        if(statement.executeUpdate()>0){
            searchVenues(searchText);
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Venue Saved!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();
        }
    }

    private void clearFields(){
        txtId.clear();
        txtName.clear();
        txtLocation.clear();
        txtEmail.clear();
        txtTelNo.clear();

    }


    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) VenueForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }

    private Object validate(){
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
                saveVenue();

            }

        }
    }


    private void addError(TextField textField) {
        if(textField.getText().length()>0){
            textField.setStyle("-fx-border-color: #EE444A; -fx-border-width: 3");
            btnSaveVenue.setDisable(true);
        }

    }

    private void removeError(TextField textField) {
        textField.setStyle("-fx-border-color: #00FF00; -fx-border-width: 3");
        btnSaveVenue.setDisable(false);
    }
}
