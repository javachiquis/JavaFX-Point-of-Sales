package com.rafsan.inventory.controller.report;

import com.rafsan.inventory.entity.Invoice;
import java.net.URL;
import java.util.ResourceBundle;

import com.rafsan.inventory.utils.DisplayUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewController implements Initializable {
    
    @FXML
    private TextField employeeField, totalField, vatField, discountField, payableField, paidField, returnedField;
    @FXML
    private Label serialLabel, dateLabel;
    private Invoice invoice;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setReport(Invoice invoice){
        this.invoice = invoice;
        setData();
    }
    
    private void setData() {
        serialLabel.setText("Transcation ID# " + invoice.getId());
        dateLabel.setText("Date: " + invoice.getDate());
        employeeField.setText(invoice.getEmployee().getUserName());
        totalField.setText(DisplayUtils.getFormattedValue(invoice.getTotal()));
        vatField.setText(DisplayUtils.getFormattedValue(invoice.getVat()));
        discountField.setText(DisplayUtils.getFormattedValue(invoice.getDiscount()));
        payableField.setText(DisplayUtils.getFormattedValue(invoice.getPayable()));
        paidField.setText(DisplayUtils.getFormattedValue(invoice.getPaid()));
        returnedField.setText(DisplayUtils.getFormattedValue(invoice.getReturned()));
    }
    
    @FXML
    public void handlePrint(ActionEvent event) {

        
    }
    
    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
