package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.modal.ProductDetails;
import com.ijse.vyroracreations.modal.ProductOrder;
import com.ijse.vyroracreations.view.tm.CartTm;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class OrderProductController {

    public AnchorPane placeOrderContext;
    public JFXTextField txtOrderId;
    public JFXTextField txtDate;
    public ToggleButton rentable;
    public JFXTextField txtCustomerId;
    public JFXComboBox cmbCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerTelNo;
    public JFXTextField txtCustomerEmail;
    public AnchorPane context;
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblTotal;
    //purchasablePane
    public JFXTextField txtPurchasableCode;
    public JFXComboBox cmbPurchasableDesc;
    public JFXTextField txtPurchasablePrice;
    public JFXTextField txtPurchasableQtyOnHand;
    public JFXTextField txtPurchasableQty;


    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setDateAndOrderId();
        loadAllCustomerNames();
        loadAllPurchasableDesc();
        setOrderId();

        cmbCustomerName.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setCustomerDetails();
            }
        }));

        cmbPurchasableDesc.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setPurchasableDetails();
            }
        }));
    }

    private void setOrderId() {
        try{
            String sql = "SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1"; //10 ta giyaata passe not working... (Use UNSIGN and resolve this)
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                //generate Id
                String tempOrderId=set.getString(1); //D-3
                String[] array = tempOrderId.split("-"); //[D,3]
                int temNumber = Integer.parseInt(array[1]);
                int finalizedOrderId = temNumber+1;
                txtOrderId.setText("D-"+finalizedOrderId);

            }else{
                //make Id as D-1
                txtOrderId.setText("D-1");
                return;
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private void setCustomerDetails() {
        try {
            String sql = "SELECT * FROM customer WHERE name=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, (String) cmbCustomerName.getValue());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                txtCustomerId.setText(set.getString(1));
                txtCustomerAddress.setText(set.getString(3));
                txtCustomerTelNo.setText(set.getString(4));
                txtCustomerEmail.setText(set.getString(5));
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void setPurchasableDetails()  {
        try {
            String sql = "SELECT * FROM product WHERE description=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, (String) cmbPurchasableDesc.getValue());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                txtPurchasableCode.setText(set.getString(1));
                txtPurchasablePrice.setText(String.valueOf(set.getDouble(3)));
                txtPurchasableQtyOnHand.setText(String.valueOf(set.getInt(4)));
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomerNames() {
        try {
            String sql = "SELECT name FROM customer";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            ArrayList<String> nameList=new ArrayList<>();
            while (set.next()){
                nameList.add(set.getString(1));
            }

            ObservableList<String> obList= FXCollections.observableArrayList(nameList);
            cmbCustomerName.setItems(obList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllPurchasableDesc() {
        try {
            String sql = "SELECT description FROM product";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            ArrayList<String> descriptionList=new ArrayList<>();
            while (set.next()){
                descriptionList.add(set.getString(1));
            }

            ObservableList<String> obList=FXCollections.observableArrayList(descriptionList);
            cmbPurchasableDesc.setItems(obList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void setDateAndOrderId() {
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    //-----------------------validating------------------
    private boolean checkQtyOfPurchasable(String code, int qty){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vyrora_creations", "root", "1234");
            String sql = "SELECT qtyOnHand FROM product WHERE code=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code);
            ResultSet set = statement.executeQuery();

            if(set.next()){
                //check
                int tempQty=set.getInt(1);
                if(tempQty>=qty){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }

    ObservableList<CartTm> obList= FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent event) {
        //-----------------------validating------------------
        if(!checkQtyOfPurchasable(txtPurchasableCode.getText(), Integer.parseInt(txtPurchasableQty.getText()))){
            new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();
            return;
        }
        double purchasableUnitPrice = Double.parseDouble(txtPurchasablePrice.getText());
        int purchasableQty = Integer.parseInt(txtPurchasableQty.getText());
        double purchasableTotal = purchasableUnitPrice*purchasableQty;

        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");


        //Updating an already added record by changing its qty (Purchasable)
        int rowPurchasable = isAlreadyExists(txtPurchasableCode.getText());
        if(rowPurchasable==-1){
            CartTm tm = new CartTm(txtPurchasableCode.getText(), (String) cmbPurchasableDesc.getValue(), purchasableUnitPrice,purchasableQty, purchasableTotal,btn);
            obList.add(tm);
            tblCart.setItems(obList);
        }else{
            int tempQty=obList.get(rowPurchasable).getQty()+purchasableQty;
            double tempTotal=purchasableUnitPrice*tempQty;

            if(!checkQtyOfPurchasable(txtPurchasableCode.getText(), tempQty)){
                new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();
                return;
            }

            obList.get(rowPurchasable).setQty(tempQty);
            obList.get(rowPurchasable).setTotal(tempTotal);
            tblCart.refresh();

        }
        calculateTotal();
        clearFieldsOfPurchasable();
        cmbPurchasableDesc.requestFocus();


        //Delete records from the table using option
        btn.setOnAction(event1 -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if(buttonType.get()==ButtonType.YES){
                for(CartTm tm:obList){
                    obList.remove(tm);
                    calculateTotal();
                    tblCart.refresh();
                    return;
                }
            }
        });
    }

    private int isAlreadyExists(String code) {
        for(int i=0; i<obList.size(); i++){
            if(obList.get(i).getCode().equals(code)){
                return i;
            }
        }
        return -1;
    }

    private void calculateTotal(){
        double total=0.00;
        for(CartTm tm : obList){
            total+=tm.getTotal();
        }
        lblTotal.setText(String.valueOf(total));

    }

    private void clearFieldsOfPurchasable() {
        txtPurchasableCode.clear();
        cmbPurchasableDesc.valueProperty().set(null);
        txtPurchasablePrice.clear();
        txtPurchasableQtyOnHand.clear();
        txtPurchasableQty.clear();
    }


    public void placeOrderOnAction(ActionEvent event) throws SQLException {
        if(obList.isEmpty()) return;
        ArrayList<ProductDetails> details = new ArrayList<>();
        for(CartTm tm:obList){
            details.add(new ProductDetails(tm.getCode(), tm.getDescription(), tm.getUnitPrice(), tm.getQty()));
        }
        ProductOrder order = new ProductOrder(
                txtOrderId.getText(), new Date(),
                Double.parseDouble(lblTotal.getText()),
                txtCustomerId.getText(), details
        );

        Connection con=null;
        try{

            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            String sql = "INSERT `order` VALUES(?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,order.getOrderId());
            statement.setString(2,txtDate.getText());
            statement.setDouble(3,order.getTotalCost());
            statement.setString(4,order.getCustomerId());

            boolean isOrderSaved = statement.executeUpdate()>0;
            if (isOrderSaved){
                boolean isAllUpdated = manageQty(details);
                if (isAllUpdated){
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                    clearAll();
                }else{
                    con.setAutoCommit(true);
                    con.rollback();
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }

            }else{
                con.setAutoCommit(true);
                con.rollback();
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            con.setAutoCommit(true);
        }


    }

    private boolean manageQty(ArrayList<ProductDetails> details) {

        try{

            for (ProductDetails d:details
            ) {


                String sql = "INSERT `order_details` VALUES(?,?,?,?)";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1,d.getCode());
                statement.setString(2,txtOrderId.getText());
                statement.setDouble(3,d.getUnitPrice());
                statement.setInt(4,d.getQty());

                boolean isOrderDetailsSaved = statement.executeUpdate()>0;

                if (isOrderDetailsSaved){
                    boolean isQtyUpdated = update(d);
                    if (!isQtyUpdated){
                        return false;
                    }
                }else{
                    return false;
                }


            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }


        return true;
    }

    private boolean update(ProductDetails d) {
        try{

            String sql = "UPDATE product SET qtyOnHand=(qtyOnHand-?) WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1,d.getQty());
            statement.setString(2,d.getCode());
            return statement.executeUpdate()>0;
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    private void clearAll() {
        obList.clear();
        calculateTotal();

        txtCustomerId.clear();
        txtCustomerAddress.clear();
        txtCustomerEmail.clear();
        txtCustomerTelNo.clear();

        //=======
        cmbCustomerName.setValue(null);
        cmbPurchasableDesc.setValue(null);
        //========

        clearFieldsOfPurchasable();
        cmbCustomerName.requestFocus();
        setOrderId();
    }

    public void setUi(String location, String title, Boolean resize) throws IOException {
        Stage stage = (Stage) placeOrderContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
        stage.setResizable(resize);
        stage.setTitle(title);
    }


}
