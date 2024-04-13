package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.view.tm.ProductDetailsTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MoreProductDetailsController {
    public AnchorPane itemDetailsContext;
    public TableView<ProductDetailsTm> tblItems;
    public TableColumn colCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;


    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }


    public void loadOrderDetails(String id) {
        try{
            String sql = "SELECT o.orderId, d.itemCode, d.orderId, d.unitPrice, d.qty" +
                    " FROM `order` o INNER JOIN `order_details` d ON o.orderId=d.orderId AND o.orderId=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,id);
            ResultSet set = statement.executeQuery();
            ObservableList<ProductDetailsTm> tmList= FXCollections.observableArrayList();
            while(set.next()){
                double tempUnitPrice=set.getDouble(4);
                int tempQtyOnHand=set.getInt(5);
                double tempTotal=tempQtyOnHand*tempUnitPrice;
                tmList.add(new ProductDetailsTm(
                        set.getString(2),tempUnitPrice,tempQtyOnHand,tempTotal
                ));

            }
            tblItems.setItems(tmList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
