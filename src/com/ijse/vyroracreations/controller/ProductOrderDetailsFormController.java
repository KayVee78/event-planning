package com.ijse.vyroracreations.controller;

import com.ijse.vyroracreations.db.DBConnection;
import com.ijse.vyroracreations.view.tm.ProductOrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ProductOrderDetailsFormController {
    public AnchorPane productOrderDetailsContext;
    public TableColumn colOrderId;
    public TableColumn colCustomerId;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colOption;
    public TableView<ProductOrderTm> tblProductDetails;

    public void initialize() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadOrders();
    }

    private void loadOrders() {
        try {
            String sql = "SELECT * FROM `order`";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            ObservableList<ProductOrderTm> tmList = FXCollections.observableArrayList();

            while (set.next()) {
                Button btn = new Button("View More");
                ProductOrderTm tm = new ProductOrderTm(
                        set.getString(1),
                        set.getString(4),
                        new Date(),
                        set.getDouble(3), btn);
                tmList.add(tm);

                btn.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MoreProductDetails.fxml"));
                        Parent parent = loader.load();
                        MoreProductDetailsController controller = loader.getController();
                        controller.loadOrderDetails(tm.getOrderId());
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.setResizable(false);
                        stage.show();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                });
            }
            tblProductDetails.setItems(tmList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void getSalesReport(MouseEvent mouseEvent) {
        try {
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/com/ijse/vyroracreations/view/reports/SalesReport.jrxml"));
            Connection connection = DBConnection.getInstance().getConnection();
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
