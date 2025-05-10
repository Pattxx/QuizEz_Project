package org.example.quizez;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {
    @FXML private TextField UsernameInputField;
    @FXML private PasswordField PasswordInputField;

    @FXML
    private void handleLogin() {
        String user = UsernameInputField.getText();
        String pass = PasswordInputField.getText();

        // Prosta walidacja – demo bez bazy for now
        if (user.equals("admin") && pass.equals("1234")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) UsernameInputField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Ekran główny");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd logowania");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawne dane!");
            alert.showAndWait();
        }
    }
}
