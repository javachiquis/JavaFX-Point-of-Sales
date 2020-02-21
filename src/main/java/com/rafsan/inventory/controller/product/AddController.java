package com.rafsan.inventory.controller.product;

import com.rafsan.inventory.interfaces.ProductInterface;
import com.rafsan.inventory.entity.Category;
import com.rafsan.inventory.entity.Product;
import com.rafsan.inventory.entity.Supplier;
import com.rafsan.inventory.model.CategoryModel;
import com.rafsan.inventory.model.ProductModel;
import com.rafsan.inventory.model.SupplierModel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddController implements Initializable, ProductInterface {

    @FXML
    private TextField nameField, priceField, quantityField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox categoryBox, supplierBox;
    @FXML
    public ImageView loadImage;
    @FXML
    private Button saveButton;
    private ProductModel productModel;
    private CategoryModel categoryModel;
    private SupplierModel supplierModel;
    private String imageURL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productModel = new ProductModel();
        categoryModel = new CategoryModel();
        supplierModel = new SupplierModel();
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categoryModel.getCategories());
        ObservableList<Supplier> supplierList = FXCollections.observableArrayList(supplierModel.getSuppliers());
        categoryBox.setItems(categoryList);
        supplierBox.setItems(supplierList);
        loadImage.setImage(new Image("images/default_product_image.jpg"));
    }

    @FXML
    public void handleSave(ActionEvent event) {

        try{
            if (validateInput()) {

                Category category = (Category)categoryBox.getSelectionModel().getSelectedItem();
                Supplier supplier = (Supplier)supplierBox.getSelectionModel().getSelectedItem();
                Product product = new Product(
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Double.parseDouble(quantityField.getText()),
                        descriptionArea.getText(),
                        category,
                        supplier,
                        imageURL
                );

                productModel.saveProduct(product);
                PRODUCTLIST.clear();
                PRODUCTLIST.addAll(productModel.getProducts());

                ((Stage) saveButton.getScene().getWindow()).close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setHeaderText("Product is added");
                alert.setContentText("Product is added successfully");
                alert.showAndWait();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleImage(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
            fileChooser.setTitle("Seleccione Imagen...");
            fileChooser.getExtensionFilters().add(imageFilter);

            File selectedImage = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (selectedImage != null) {
                imageURL = selectedImage.toURI().toURL().toExternalForm();
            } else {
                imageURL = "images/default_product_image.jpg";
            }

            loadImage.setImage(new Image(imageURL));

        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void handleCancel(ActionEvent event) {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        descriptionArea.setText("");
        categoryBox.valueProperty().setValue(null);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }

        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "No valid price!\n";
        }

        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "No valid quantity!\n";
        }

        if (descriptionArea.getText() == null || descriptionArea.getText().length() == 0) {
            errorMessage += "No description!\n";
        }

        if (categoryBox.getSelectionModel().isEmpty()) {
            errorMessage += "Please select the category!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
