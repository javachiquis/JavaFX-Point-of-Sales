package com.rafsan.inventory.controller.login;

import com.rafsan.inventory.controller.admin.AdminController;
import com.rafsan.inventory.controller.pos.PosController;
import com.rafsan.inventory.entity.Employee;
import com.rafsan.inventory.model.EmployeeModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Label errorLabel;
    private EmployeeModel model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new EmployeeModel();
        enterPressed();
    }

    private void enterPressed() {

        usernameField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    authenticate(ke);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        passwordField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    authenticate(ke);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    @FXML
    public void loginAction(ActionEvent event) throws Exception {

        authenticate(event);
    }

    private void authenticate(Event event) throws Exception {
        if (validateInput()) {

            String username = usernameField.getText().trim();
            String password = DigestUtils.sha1Hex((passwordField.getText().trim()));

            if (model.checkUser(username)) {

                if (model.checkPassword(username, password)) {

                    ((Node) (event.getSource())).getScene().getWindow().hide();

                    Employee employee = model.getEmployee(username);

                    switch (employee.getType()) {
                        case "admin":
                            //AdminController adminController = new AdminController(employee);
                            windows("/fxml/Admin.fxml", "Admin Panel", null);
                            break;

                        case "employee":
                            PosController posController = new PosController(employee);
                            windows("/fxml/Pos.fxml", "Punto de ventas", posController);
                            break;
                    }
                } else {
                    passwordField.setText("");
                    errorLabel.setText("Contraseña erronea!");
                }
            } else {
                resetFields();
                errorLabel.setText("Usuario no existente!");
            }
        }
    }

    /*private void windows(String path, String title) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title + " - " + usernameField.getText().trim());
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }*/

    private void windows(String path, String title, Initializable controller) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

        if (controller != null) {
            loader.setController(controller);
        }

        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title + " - " + usernameField.getText().trim());
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        resetFields();
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void minusAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (usernameField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Por favor ingrese los datos!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorLabel.setText(errorMessage);
            return false;
        }
    }
}
