package com.rafsan.inventory.interfaces;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.io.Serializable;
import java.text.DecimalFormat;

public interface TableColumnInterface<T> {

    DecimalFormat decimalFormat = new DecimalFormat("###");

    default Callback<TableColumn<T, Double>, TableCell<T, Double>> getFormattedValue() {

        //DecimalFormat decimalFormat = new DecimalFormat("###");

        return tc -> new TableCell<T, Double>(){
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if(empty){
                    setText(null);
                } else {
                    setText(decimalFormat.format(value));
                }
            }
        };
    }

}
