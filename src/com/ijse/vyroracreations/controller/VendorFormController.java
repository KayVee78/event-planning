package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.Vendor;
import com.ijse.vyroracreations.modal.Venue;
import com.ijse.vyroracreations.view.tm.VendorTm;
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

public class VendorFormController {
    public AnchorPane VendorForm;
    public TextField txtId;
    public TextField txtCompanyName;
    public TextField txtAddress;
    public TextField txtTelNo;
    public TextField txtEmail;
    public Button btnSaveVendor;
    public TableView<VendorTm> tblVendor;
    public TableColumn colId;
    public TableColumn colCompanyName;
    public TableColumn colAddress;
    public TableColumn colTelNo;
    public TableColumn colEmail;
    public TableColumn colOption;
    public TextField txtSearch;
    private String searchText="";
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory<>("telNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchVendors(searchText);

        tblVendor.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(null!=newValue){ //newValue!=null (this consumes more time)
                setData(newValue);
            }
        }));
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchVendors(searchText);
        }));

        btnSaveVendor.setDisable(true);

        Pattern idPattern = Pattern.compile("^(Vendor00)[0-9]{1,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9]{3,100}$");
        Pattern telNoPattern = Pattern.compile("^(\\+94)[0-9]{9}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

        map.put(txtId, idPattern);
        map.put(txtCompanyName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtTelNo, telNoPattern);
        map.put(txtEmail, emailPattern);

    }

    private void setData(VendorTm tm) {
        txtId.setText(tm.getId());
        txtCompanyName.setText(tm.getCompanyName());
        txtAddress.setText(tm.getAddress());
        txtTelNo.setText(tm.getTelNo());
        txtEmail.setText(tm.getEmail());

        btnSaveVendor.setText("Update Vendor");
        btnSaveVendor.setStyle("-fx-background-color: #FFFF1A; -fx-text-fill: black;");
    }

    private void searchVendors(String text){
        String searchText="%"+text+"%";
        try {
            ObservableList<VendorTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM vendor WHERE id LIKE ? || companyName LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Button btn = new Button("Delete");
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                VendorTm tm = new VendorTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5), btn);
                tmList.add(tm);

                //----------------------------Delete--------------------------------------------
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this vendor?",ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        //-------------------connecting database
                        try {
                            String sql1 = "DELETE FROM vendor WHERE id=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getId());

                            if(statement1.executeUpdate()>0){
                                searchVendors(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Vendor Deleted").show();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
            tblVendor.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    private void saveVendor() throws SQLException, ClassNotFoundException {
        Vendor v1 = new Vendor(txtId.getText(), txtCompanyName.getText(), txtAddress.getText(), txtTelNo.getText(), txtEmail.getText());
        String sql = "INSERT INTO vendor VALUES (?,?,?,?,?)";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, v1.getId());
        statement.setString(2, v1.getCompanyName());
        statement.setString(3, v1.getAddress());
        statement.setString(4, v1.getTelNo());
        statement.setString(5, v1.getEmail());

        if(statement.executeUpdate()>0){
            searchVendors(searchText);
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Vendor Saved!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();
        }
    }

    private void clearFields(){
        txtId.clear();
        txtCompanyName.clear();
        txtAddress.clear();
        txtTelNo.clear();
        txtEmail.clear();
    }

    public void btnNewVendor(ActionEvent event) {
        btnSaveVendor.setText("Save Vendor");
        btnSaveVendor.setStyle("-fx-background-color: #27ae60");
        clearFields();
    }

    public void btnSaveVendorOnAction(ActionEvent event) {
        Vendor v1 = new Vendor(txtId.getText(), txtCompanyName.getText(), txtAddress.getText(), txtTelNo.getText(), txtEmail.getText());

        if(btnSaveVendor.getText().equalsIgnoreCase("Save Vendor")){
            //-------------------database connecting----------------------------------------
            try {
                saveVendor();

            }catch (ClassNotFoundException | SQLException e){ //compile time exception
                e.printStackTrace();
            }
        }else{
            //-----------------------------------Update--------------------------------------------------
            try {
                String sql = "UPDATE vendor SET companyName=?, address=?, telNo=?, email=? WHERE id=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, v1.getCompanyName());
                statement.setString(2, v1.getAddress());
                statement.setString(3, v1.getTelNo());
                statement.setString(4, v1.getEmail());
                statement.setString(5, v1.getId());
                if(statement.executeUpdate()>0){
                    searchVendors(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Vendor Updated!").show();
                }else{
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();

                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openVendorCatalogueOnAction(ActionEvent event) throws IOException {
        setUi("VendorCatalogueForm","Vendor Catalogue", false);
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) VendorForm.getScene().getWindow();
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
                saveVendor();

            }

        }
    }




    private void addError(TextField textField) {
        if(textField.getText().length()>0){
            textField.setStyle("-fx-border-color: #EE444A; -fx-border-width: 3");
            btnSaveVendor.setDisable(true);
        }

    }

    private void removeError(TextField textField) {
        textField.setStyle("-fx-border-color: #00FF00; -fx-border-width: 3");
        btnSaveVendor.setDisable(false);
    }


}
