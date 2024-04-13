package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.Customer;
import com.ijse.vyroracreations.view.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class CustomerRegistrationFormController {

    public AnchorPane customerRegistrationForm;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtTelNo;
    public TextField txtEmail;
    public Button btnSaveCustomer;
    public TextField txtSearch;
    public TableView<CustomerTm> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colTelNo;
    public TableColumn colEmail;
    public TableColumn colOption;
    private String searchText="";
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory<>("telNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomers(searchText);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(null!=newValue){ //newValue!=null (this consumes more time)
                setData(newValue);
            }
        }));
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchCustomers(searchText);
        }));

        btnSaveCustomer.setDisable(true);

        Pattern idPattern = Pattern.compile("^(C00)[0-9]{1,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,100}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9]{3,100}$");
        Pattern telNoPattern = Pattern.compile("^(\\+94)[0-9]{9}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

        map.put(txtId, idPattern);
        map.put(txtName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtTelNo, telNoPattern);
        map.put(txtEmail, emailPattern);
    }

    private void setData(CustomerTm tm){
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtTelNo.setText(tm.getTelNo());
        txtEmail.setText(tm.getEmail());

        btnSaveCustomer.setText("Update Customer");
        btnSaveCustomer.setStyle("-fx-background-color: #0984e3;");

    }

    private void searchCustomers(String text){
        String searchText="%"+text+"%";
        try {
            ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM customer WHERE name LIKE ? || address LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            statement.setString(2, searchText);
            ResultSet set = statement.executeQuery();

            while (set.next()){
                Button btn = new Button("Delete");
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                CustomerTm tm = new CustomerTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5), btn);
                tmList.add(tm);

                //----------------------------Delete--------------------------------------------
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure whether you want to delete this customer?",ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        //-------------------connecting database
                        try {
                            String sql1 = "DELETE FROM customer WHERE id=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getId());

                            if(statement1.executeUpdate()>0){
                                searchCustomers(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted").show();
                            }else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
            tblCustomer.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public void btnNewCustomer(ActionEvent event) {
        btnSaveCustomer.setText("Save Customer");
        btnSaveCustomer.setStyle("-fx-background-color: #27ae60");
        clearFields();
    }

    public void btnSaveCustomerOnAction(ActionEvent event) {
        Customer c1 = new Customer(txtId.getText(), txtName.getText(), txtAddress.getText(),txtTelNo.getText(), txtEmail.getText());

        if(btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")){
            //-------------------database connecting----------------------------------------
            try {
                saveCustomer();

            }catch (ClassNotFoundException | SQLException e){ //compile time exception
                e.printStackTrace();
            }
        }else{
            //-----------------------------------Update--------------------------------------------------
            try {
                String sql = "UPDATE customer SET name=?, address=?, telNo=?, email=? WHERE id=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, c1.getName());
                statement.setString(2, c1.getAddress());
                statement.setString(3, c1.getTelNo());
                statement.setString(4, c1.getEmail());
                statement.setString(5, c1.getId());
                if(statement.executeUpdate()>0){
                    searchCustomers(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").show();
                }else{
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();

                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }




        }

    }

    private void saveCustomer() throws SQLException, ClassNotFoundException {
        Customer c1 = new Customer(txtId.getText(), txtName.getText(), txtAddress.getText(),txtTelNo.getText(), txtEmail.getText());
        String sql = "INSERT INTO customer VALUES (?,?,?,?,?)";
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1, c1.getId());
        statement.setString(2, c1.getName());
        statement.setString(3, c1.getAddress());
        statement.setString(4, c1.getTelNo());
        statement.setString(5, c1.getEmail());
        if(statement.executeUpdate()>0){
            searchCustomers(searchText);
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Customer Saved!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Something went wrong!").show();
        }

    }

    private void clearFields(){
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtTelNo.clear();
        txtEmail.clear();
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) customerRegistrationForm.getScene().getWindow();
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
                saveCustomer();

            }

        }

    }

    //Previous validation
    /*private Object validate(){

        //Text field patterns
        Pattern idPattern = Pattern.compile("^(C00)[0-9]{1,5}$");
        Pattern namePattern = Pattern.compile("^[A-z, ,]{3,100}$");
        Pattern addressPattern = Pattern.compile("^[A-z]{3,100}$");
        Pattern telNoPattern = Pattern.compile("^(\\+94)[0-9]{9}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");


        if(!idPattern.matcher(txtId.getText()).matches()){
            addError(txtId);
            return txtId;
        }else{
            removeError(txtId);
            if(!namePattern.matcher(txtName.getText()).matches()){
                addError(txtName);
                return txtName;
            }else{
                removeError(txtName);
                if(!addressPattern.matcher(txtAddress.getText()).matches()) {
                    addError(txtAddress);
                    return txtAddress;
                }else {
                    removeError(txtAddress);
                    if(!telNoPattern.matcher(txtTelNo.getText()).matches()){
                        addError(txtTelNo);
                        return txtTelNo;
                    }else {
                        removeError(txtTelNo);
                        if(!emailPattern.matcher(txtEmail.getText()).matches()) {
                            addError(txtEmail);
                            return txtEmail;
                        }else {
                            removeError(txtEmail);
                        }
                    }
                }
            }
        }
        return true;
    }*/


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

    public void getCustomerReport(MouseEvent event) {
        try{
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/com/ijse/vyroracreations/view/reports/Customer.jrxml"));
            Connection connection=DBConnection.getInstance().getConnection();
            JasperReport compileReport= JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint=JasperFillManager.fillReport(compileReport, null, connection);
            JasperViewer.viewReport(jasperPrint, false);
        }catch (JRException e){
            e.printStackTrace();
        }catch (SQLException throwable){
            throwable.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }


    }

    private void addError(TextField textField) {
        if(textField.getText().length()>0){
            textField.setStyle("-fx-border-color: #EE444A; -fx-border-width: 3");
            btnSaveCustomer.setDisable(true);
        }

    }

    private void removeError(TextField textField) {
        textField.setStyle("-fx-border-color: #00FF00; -fx-border-width: 3");
        btnSaveCustomer.setDisable(false);
    }


}
