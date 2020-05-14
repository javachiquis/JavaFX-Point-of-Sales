package com.rafsan.inventory.controller.pos;

import com.rafsan.inventory.entity.Employee;
import com.rafsan.inventory.entity.Item;
import com.rafsan.inventory.entity.Payment;
import com.rafsan.inventory.entity.Product;
import com.rafsan.inventory.interfaces.ProductInterface;
import com.rafsan.inventory.interfaces.TableColumnInterface;
import com.rafsan.inventory.model.ProductModel;
import com.rafsan.inventory.utils.DisplayUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class PosController implements Initializable, ProductInterface, TableColumnInterface<Item> {

    public static final boolean ENABLE_IVA = false;

    public static final double IVA = 0.19;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableView<Item> listTableView;
    @FXML
    private TableColumn<Product, String> productColumn;
    @FXML
    private TableColumn<Item, String> itemColumn;
    @FXML
    private TableColumn<Item, Double> priceColumn, quantityColumn, totalColumn;
    @FXML
    private TextField searchField, productField, priceField, quantityField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField subTotalField, discountField, vatField, netPayableField;
    @FXML
    private Button addButton, removeButton, paymentButton;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label vatLabel;
    @FXML
    private ObservableList<Item> ITEMLIST;
    @FXML
    private ImageView loadedImage;

    private ProductModel productModel;

    private double xOffset = 0;
    private double yOffset = 0;

    private Employee employee;

    public PosController(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ITEMLIST = FXCollections.observableArrayList();
        productModel = new ProductModel();

        loadData();

        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
        productTableView.setItems(PRODUCTLIST);

        filterData();

        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        priceColumn.setCellFactory(getFormattedValue());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(getFormattedValue());
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setCellFactory(getFormattedValue());
        listTableView.setItems(ITEMLIST);

        addButton
                .disableProperty()
                .bind(Bindings.isEmpty(productTableView.getSelectionModel().getSelectedItems()));
        removeButton
                .disableProperty()
                .bind(Bindings.isEmpty(listTableView.getSelectionModel().getSelectedItems()));
    }

    private void filterData() {
        FilteredList<Product> searchedData = new FilteredList<>(PRODUCTLIST, e -> true);

        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (product.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (product.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Product> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(productTableView.comparatorProperty());
            productTableView.setItems(sortedData);
        });
    }

    private void loadData() {
        if (!PRODUCTLIST.isEmpty()) {
            PRODUCTLIST.clear();
        }

        PRODUCTLIST.addAll(productModel.findAll());
    }

    private void showDetails(Product product) {
        if (product != null) {
            quantityField.setDisable(false);
            productField.setText(product.getProductName());
            priceField.setText(decimalFormat.format(product.getPrice()));
            if (product.getImageURL() != null && !product.getImageURL().isEmpty()){
                loadedImage.setImage(new Image(product.getImageURL()));
            } else {
                loadedImage.setImage(new Image("images/default_product_image.jpg"));
            }

            double quantity = product.getQuantity();

            if (quantity > 0) {
                quantityField.setEditable(true);
                quantityField.setStyle(null);
            } else {
                quantityField.setEditable(false);
                quantityField.setStyle("-fx-background-color: red;");
            }
            quantityLabel.setText("Stock: " + decimalFormat.format(quantity));
            descriptionArea.setText(product.getDescription());
        } else {
            productField.setText("");
            priceField.setText("");
            quantityLabel.setText("");
            descriptionArea.setText("");
        }
    }

    private void resetProductTableSelection() {
        productTableView.getSelectionModel().clearSelection();
    }

    private void resetItemTable() {
        ITEMLIST.clear();
    }

    private void resetAdd() {
        productField.setText("");
        priceField.setText("");
        quantityField.setText("");
        resetQuantityField();
        quantityLabel.setText("Disponible: ");
        descriptionArea.setText("");
    }

    private void resetInvoice() {
        subTotalField.setText("0");
        vatField.setText("");
        netPayableField.setText("0");
    }

    private void resetQuantityField() {
        quantityField.setDisable(true);
    }

    private void resetPaymentButton() {
        paymentButton.setDisable(true);
    }

    private void resetInterface() {
        loadData();
        resetPaymentButton();
        resetProductTableSelection();
        resetItemTable();
        resetQuantityField();
        resetAdd();
        resetInvoice();
    }

    @FXML
    public void resetAction(ActionEvent event) {
        resetInterface();
    }

    @FXML
    public void addAction(ActionEvent event) {

        if (validateInput()) {
            String productName = productField.getText();
            double unitPrice = Double.parseDouble(priceField.getText());
            double quantity = Double.parseDouble(quantityField.getText());
            double total = unitPrice * quantity;
            ITEMLIST.add(new Item(productName, unitPrice, quantity, total));
            calculation();

            resetAdd();
            productTableView.getSelectionModel().clearSelection();
        }
    }

    private void calculation() {

        double subTotalPrice = 0.0;
        subTotalPrice = listTableView.getItems().stream().map(
                (item) -> item.getTotal()).reduce(subTotalPrice, (accumulator, _item) -> accumulator + _item);

        if (subTotalPrice > 0) {
            paymentButton.setDisable(false);

            double vat  = 0;

            if(ENABLE_IVA){
                vat = subTotalPrice * IVA;
                vatField.setText(DisplayUtils.getFormattedValue(vat));
                vatField.setDisable(false);
                vatLabel.setVisible(true);
            }

            double netPayablePrice = (Math.abs((subTotalPrice + vat)));

            subTotalField.setText(DisplayUtils.getFormattedValue(subTotalPrice));
            netPayableField.setText(DisplayUtils.getFormattedValue(netPayablePrice));
        }
    }

    @FXML
    public void paymentAction(ActionEvent event) throws Exception {

        Payment payment = new Payment(
                Double.parseDouble(subTotalField.getText().trim()),
                Double.parseDouble(vatField.getText().isEmpty() ? "0.0" : vatField.getText().trim()),
                Double.parseDouble(discountField.getText().isEmpty() ? "0.0" : discountField.getText().trim()),
                Double.parseDouble(netPayableField.getText().trim())
        );

        ObservableList<Item> sold = listTableView.getItems();

        FXMLLoader loader = new FXMLLoader((getClass().getResource("/fxml/Invoice.fxml")));
        InvoiceController controller = new InvoiceController(employee);
        loader.setController(controller);
        controller.setData(Double.parseDouble(netPayableField.getText().trim()), sold, payment);
        Parent root = loader.load();
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pago");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.showAndWait();

        resetInterface();
    }

    @FXML
    public void removeAction(ActionEvent event) {

        int index = listTableView.getSelectionModel().getSelectedIndex();

        if (index > 0) {
            listTableView.getItems().remove(index);
            calculation();
        } else if (index == 0) {
            listTableView.getItems().remove(index);
            resetInvoice();
        }
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "Cantidad no suministrada!\n";
        } else {
            double quantity = Double.parseDouble(quantityField.getText());
            String available = quantityLabel.getText();
            double availableQuantity = Double.parseDouble(available.substring(7));

            String productName = productField.getText();

            int selectedQuantity = ITEMLIST
                    .stream()
                    .filter(item -> item.getItemName().equalsIgnoreCase(productName))
                    .mapToInt(item -> (int) item.getQuantity()).sum();

            long totalQuantity = selectedQuantity + new Double(quantity).longValue();

            if (totalQuantity > availableQuantity) {
                errorMessage += "Agotado!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alerta");
            alert.setHeaderText("Por favor ingrese un número valido de productos");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            quantityField.setText("");

            return false;
        }
    }

    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        stage.setTitle("Inventory:: Version 1.0");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
