package com.rafsan.inventory.controller.admin;

import com.rafsan.inventory.constants.MonthEnum;
import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.entity.Product;
import com.rafsan.inventory.model.InvoiceModel;
import com.rafsan.inventory.model.ProductModel;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.util.*;

public class AdminController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button menu;
    @FXML
    private VBox drawer;

    @FXML
    private LineChart<String, Number> invoiceChart;
    @FXML
    CategoryAxis ixAxis;
    @FXML
    private BarChart<String, Double> productsChart;
    @FXML
    CategoryAxis pxAxis;

    @FXML
    private PieChart stockChart;

    @FXML
    private ComboBox<String> monthMenu;

    @FXML
    private ComboBox<Integer> yearMenu;

    private ProductModel productModel;
    private InvoiceModel invoiceModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productModel = new ProductModel();
        invoiceModel = new InvoiceModel();

        drawerAction();
        loadMonthMenu();
        loadYearMenu();
        loadInvoiceChart();
        loadProductsChart();
        loadStockChart();
    }

    private void drawerAction() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        menu.setOnAction((ActionEvent evt) -> {
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                menu.getStyleClass().remove("hamburger-button");
                menu.getStyleClass().add("open-menu");
            } else {
                closeNav.setToX(-(drawer.getWidth()));
                closeNav.play();
                menu.getStyleClass().remove("open-menu");
                menu.getStyleClass().add("hamburger-button");
            }
        });
    }

    private void loadMonthMenu() {
        String[] months = DateFormatSymbols.getInstance(Locale.getDefault()).getMonths();
        ObservableList<String> monthList = FXCollections.observableArrayList(months);
        monthList.add(0, "Reporte General");
        monthMenu.setItems(monthList);
    }

    private void loadYearMenu() {
        String selectedMonth = monthMenu.getSelectionModel().getSelectedItem();
        int monthIndex = MonthEnum.getIndexByName(selectedMonth);

        ObservableList<Integer> years = invoiceModel.getInvoiceYearsByMonth(monthIndex);
        yearMenu.setItems(years);

        /*for (String year : years){
            System.out.println(year);
        }*/
    }

    private void loadMonthlyChart() {

        try {
            invoiceChart.getData().clear();

            String monthName = monthMenu.getSelectionModel().getSelectedItem();
            Integer month = MonthEnum.getIndexByName(monthName);
            Integer year = yearMenu.getSelectionModel().getSelectedItem();
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();

            List<String> dayList = new ArrayList<>();
            for (int i = 1; i <= daysInMonth; i++) {
                dayList.add(String.valueOf(i));
            }

            ObservableList<String> days = FXCollections.observableArrayList(dayList);

            XYChart.Series series = new XYChart.Series();

            Map<Integer, Double> payableMap = createDaysMap(invoiceModel.findAllInvoicesByMonth(month));

            for (Integer key : payableMap.keySet()) {
                series.getData().add(new XYChart.Data(key.toString(), payableMap.get(key)));
            }

            /*for (Invoice i : invoiceModel.findAllInvoicesByMonth(month)) {
                String dayOfMonth = convertDayOfMonth(i.getDate());
                series.getData().add(new XYChart.Data(dayOfMonth, i.getPayable()));
            }*/

            series.setName("Ventas en " + monthName);
            invoiceChart.setTitle("Reporte de Ventas " + monthName + " " + year);
            invoiceChart.getData().add(series);
            ixAxis.setCategories(days);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Map<Integer, Double> createDaysMap(ObservableList<Invoice> invoiceList) {
        Map<Integer, Double> payableMap = new HashMap<>();
        for (Invoice i : invoiceList) {
            Integer dayOfMonth = convertDayOfMonth(i.getDate());
            if (payableMap.containsKey(dayOfMonth)) {
                double payableSum = payableMap.get(dayOfMonth) + i.getPayable();
                payableMap.put(dayOfMonth, payableSum);
            } else {
                payableMap.put(dayOfMonth, i.getPayable());
            }
        }

        return payableMap;
    }

    private void loadInvoiceChart() {

        try {
            invoiceChart.getData().clear();
            String[] months = DateFormatSymbols.getInstance(Locale.getDefault()).getMonths();
            ObservableList lists = FXCollections.observableArrayList(months);
            XYChart.Series series = new XYChart.Series();

            Map<String, Double> payableMap = createMonthMap(invoiceModel.findAll());

            for (String key : payableMap.keySet()) {
                series.getData().add(new XYChart.Data(key, payableMap.get(key)));
            }

            /*for (Invoice i : invoiceModel.findAll()) {
                String month = convertDate(i.getDate());
                series.getData().add(new XYChart.Data(month, i.getPayable()));
            }*/

            series.setName("Ventas generales");
            invoiceChart.setTitle("Reporte de Ventas");
            invoiceChart.getData().add(series);
            ixAxis.setCategories(lists);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Map<String, Double> createMonthMap(List<Invoice> invoiceList) {
        Map<String, Double> payableMap = new HashMap<>();
        for (Invoice i : invoiceList) {
            String dayOfMonth = convertDate(i.getDate());
            if (payableMap.containsKey(dayOfMonth)) {
                double payableSum = payableMap.get(dayOfMonth) + i.getPayable();
                payableMap.put(dayOfMonth, payableSum);
            } else {
                payableMap.put(dayOfMonth, i.getPayable());
            }
        }

        return payableMap;
    }

    private void loadProductsChart() {

        try {
            ObservableList lists = FXCollections.observableArrayList();
            XYChart.Series<String, Double> series = new XYChart.Series<>();

            for (Product p : productModel.findAll()) {
                series.getData().add(new XYChart.Data(p.getProductName(), p.getQuantity()));
                lists.add(p.getProductName());
            }

            series.setName("Products");
            pxAxis.setCategories(lists);
            productsChart.getData().add(series);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private Integer convertDayOfMonth(String date) {
        return Integer.parseInt(date.substring(8, 10));
    }

    private String convertDate(String date) {

        int d = Integer.parseInt(date.substring(5, 7));
        return new DateFormatSymbols().getMonths()[d - 1];
    }

    private void loadStockChart() {

        ObservableList<PieChart.Data> lists = FXCollections.observableArrayList();

        for (Product p : productModel.findAll()) {

            lists.add(new PieChart.Data(p.getProductName(), p.getQuantity()));
        }

        stockChart.getData().addAll(lists);
    }

    @FXML
    public void productAction(ActionEvent event) throws Exception {

        windows("/fxml/Product.fxml", "Product", event);
    }

    @FXML
    public void categoryAction(ActionEvent event) throws Exception {

        windows("/fxml/Category.fxml", "Category", event);
    }

    @FXML
    public void purchaseAction(ActionEvent event) throws Exception {

        windows("/fxml/Purchase.fxml", "Purchase", event);
    }

    @FXML
    public void salesAction(ActionEvent event) throws Exception {

        windows("/fxml/Sales.fxml", "Sales", event);
    }

    @FXML
    public void supplierAction(ActionEvent event) throws Exception {
        windows("/fxml/Supplier.fxml", "Supplier", event);
    }

    @FXML
    public void reportAction(ActionEvent event) throws Exception {
        windows("/fxml/Report.fxml", "Report", event);
    }

    @FXML
    public void staffAction(ActionEvent event) throws Exception {
        windows("/fxml/Employee.fxml", "Employee", event);
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

    private void windows(String path, String title, ActionEvent event) throws Exception {

        double width = ((Node) event.getSource()).getScene().getWidth();
        double height = ((Node) event.getSource()).getScene().getHeight();

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    public void handleMonthChange(ActionEvent actionEvent) {
        if ("Reporte general".equalsIgnoreCase(monthMenu.getSelectionModel().getSelectedItem())) {
            loadInvoiceChart();
        } else {
            loadYearMenu();
        }
    }

    @FXML
    public void handleShowChart(ActionEvent actionEvent) {
        loadMonthlyChart();
    }
}
