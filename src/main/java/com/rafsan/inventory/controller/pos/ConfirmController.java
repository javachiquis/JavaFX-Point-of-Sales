package com.rafsan.inventory.controller.pos;

import com.rafsan.inventory.entity.Item;
import com.rafsan.inventory.interfaces.TableColumnInterface;
import com.rafsan.inventory.pdf.PrintInvoice;
import java.net.URL;
import java.util.ResourceBundle;

import com.rafsan.inventory.utils.DisplayUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConfirmController implements Initializable, TableColumnInterface<Item> {

    @FXML
    private Label retailLabel;
    private double retail;
    private ObservableList<Item> items;
    private String barcode;

    @FXML
    private TableView<Item> soldProducts;

    @FXML
    private TableColumn<Item, String> productColumn;

    @FXML
    private TableColumn<Item, Double> productCost;

    @FXML
    private TableColumn<Item, Double> productQuantity;

    @FXML
    private TableColumn<Item, Double> productTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        retailLabel.setText("Cambio: $" + DisplayUtils.getFormattedValue(retail));

        soldProducts.setItems(items);
        productColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        productCost.setCellFactory(getFormattedValue());
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productQuantity.setCellFactory(getFormattedValue());
        productTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        productTotal.setCellFactory(getFormattedValue());

    }

    public void setData(double retail, ObservableList<Item> items, String barcode) {
        this.retail = retail;
        this.items = FXCollections.observableArrayList(items);
        this.barcode = barcode;
    }

    @FXML
    public void doneAction(ActionEvent event) {
        PrintInvoice pi = new PrintInvoice(items, barcode);
        pi.generateReport();
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
