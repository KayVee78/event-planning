package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.HiredEmployee;
import com.ijse.vyroracreations.view.tm.HiredEmployeeTm;
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

public class HiredEmployeeFormController {
    public AnchorPane hiredEmployeeForm;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtEmail;
    public TextField txtTelNo;
    public TextField txtSpecialization;
    public TextField txtWages;
    public Button btnSaveEmployee;
    public TableView<HiredEmployeeTm> tblEmployee;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public TableColumn colTelNo;
    public TableColumn colSpecialization;
    public TableColumn colWages;
    public TableColumn colOption;
    public TextField txtSearch;
    private String searchText="";
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelNo.setCellValueFactory(new PropertyValueFactory<>("telNo"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colWages.setCellValueFactory(new PropertyValueFactory<>("wages"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchEmployees(searchText);

        tblEmployee.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(null!=newValue){ //newValue!=null (this consumes more time)
                setData(newValue);
            }
        }));
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchEmployees(searchText);
        }));

        btnSaveEmployee.setDisable(true);

        Pattern idPattern = Pattern.compile("^(E00)[0-9]{1,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9]{3,100}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
        Pattern telNoPattern = Pattern.compile("^(\\+94)[0-9]{9}$");
        Pattern specializationPattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern wagesPattern = Pattern.compile("(?!0+(?:\\.0+)?$)[0-9]+(?:\\.[0-9]+)?");


        map.put(txtId, idPattern);
        map.put(txtName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtEmail, emailPattern);
        map.put(txtTelNo, telNoPattern);
        map.put(txtSpecialization, specializationPattern);
        map.put(txtWages, wagesPattern);

    }

    private void setData(HiredEmployeeTm tm){
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtEmail.setText(tm.getEmail());
        txtTelNo.setText(tm.getTelNo());
        txtSpecialization.setText(tm.getSpecialization());
        txtWages.setText(String.valueOf(tm.getWages()));
        btnSaveEmployee.setText("Update Employee");
        btnSaveEmployee.setStyle("-fx-background-color: #0984e3;");

    }

    private void searchEmployees(String text){
        String searchText="%"+text+"%";
        try {
            ObservableList<HiredEmployeeTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM hired_employee WHERE name LIKE ? || address LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Button btn = new Button("Delete");
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                HiredEmployeeTm tm = new HiredEmployeeTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        set.getDouble(7), btn);
                tmList.add(tm);

                //----------------------------Delete--------------------------------------------
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this employee?",ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        //-------------------connecting database
                        try {
                            String sql1 = "DELETE FROM hired_employee WHERE id=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getId());

                            if(statement1.executeUpdate()>0){
                                searchEmployees(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Employee Deleted").show();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
            tblEmployee.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public void btnNewEmployee(ActionEvent event) {
        btnSaveEmployee.setText("Save Employee");
        btnSaveEmployee.setStyle("-fx-background-color: #27ae60");
        clearFields();
    }

    public void btnSaveEmployeeOnAction(ActionEvent event) {
        HiredEmployee e1 = new HiredEmployee(txtId.getText(), txtName.getText(), txtAddress.getText(), txtEmail.getText(), txtTelNo.getText(), txtSpecialization.getText(), Double.parseDouble(txtWages.getText()));

        if(btnSaveEmployee.getText().equalsIgnoreCase("Save Employee")){
            //-------------------database connecting----------------------------------------
            try {
                saveEmployee();
            }catch (ClassNotFoundException | SQLException e){ //compile time exception
                e.printStackTrace();
            }
        }else{
            //-----------------------------------Update--------------------------------------------------
            try {
                String sql = "UPDATE hired_employee SET name=?, address=?, email=?, telNo=?, specialization=?, wages=? WHERE id=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, e1.getName());
                statement.setString(2, e1.getAddress());
                statement.setString(3, e1.getEmail());
                statement.setString(4, e1.getTelNo());
                statement.setString(5, e1.getSpecialization());
                statement.setDouble(6, e1.getWages());
                statement.setString(7, e1.getId());
                if(statement.executeUpdate()>0){
                    searchEmployees(searchText); //employee kenek save unaa nm data tika table ekata load wenna one
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Employee Updated!").show();
                }else{
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();

                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }




        }
    }
    private void saveEmployee() throws SQLException, ClassNotFoundException {
        HiredEmployee e1 = new HiredEmployee(txtId.getText(), txtName.getText(), txtAddress.getText(), txtEmail.getText(), txtTelNo.getText(), txtSpecialization.getText(), Double.parseDouble(txtWages.getText()));
        String sql = "INSERT INTO hired_employee VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, e1.getId());
        statement.setString(2, e1.getName());
        statement.setString(3, e1.getAddress());
        statement.setString(4, e1.getEmail());
        statement.setString(5, e1.getTelNo());
        statement.setString(6, e1.getSpecialization());
        statement.setDouble(7, e1.getWages());
        if(statement.executeUpdate()>0){
            searchEmployees(searchText); //employee kenek save unaa nm data tika table ekata load wenna one
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Employee Saved!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();
        }
    }

    private void clearFields(){
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtTelNo.clear();
        txtSpecialization.clear();
        txtWages.clear();
    }

    public void btnGoBackOnAction(ActionEvent event) {
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) hiredEmployeeForm.getScene().getWindow();
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
                saveEmployee();

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
            btnSaveEmployee.setDisable(true);
        }

    }

    private void removeError(TextField textField) {
        textField.setStyle("-fx-border-color: #00FF00; -fx-border-width: 3");
        btnSaveEmployee.setDisable(false);
    }
}
