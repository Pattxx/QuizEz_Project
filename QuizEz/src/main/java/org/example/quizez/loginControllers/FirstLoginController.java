package org.example.quizez.loginControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.sessionControllers.SessionController;
import org.example.quizez.managers.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLoginController {
    @FXML private TextField UsernameInputField;
    @FXML private PasswordField PasswordInputField;
    @FXML private Button LogInButton;
    @FXML private Button RegisterViewButton;
    @FXML private CheckBox IsGuestCheckBox;
    @FXML private Label ErrorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        ///login as quest
        if (IsGuestCheckBox.isSelected()) {
            SessionController.loginAsGuest();

            Stage stage = SceneManager.getStageFrom(event.getSource());
            try {
                SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);
            } catch (IOException e) {
                e.printStackTrace();
                ErrorLabel.setVisible(true);
                ErrorLabel.setText("Błąd podczas ładowania głównego widoku.");
            }
            return;
        }

        ///normal log in
        else {
            String username = UsernameInputField.getText();
            String password = PasswordInputField.getText();

            ///if any field is empty
            if(username.isEmpty() || password.isEmpty()){
                ErrorLabel.setVisible(true);
                ErrorLabel.setText("Wypełnij wszystkie pola!");
                return;
            }

            Integer userId = authenticateUser(username, password);

            if (userId != null){
                ///login successful, switch scene, get id
                SessionController.login(userId, username);
                try{
                    Stage stage = SceneManager.getStageFrom(event.getSource());
                    SceneManager.switchScene(stage,"/org/example/quizez/main-view.fxml", null );
                } catch(IOException e) {
                    e.printStackTrace();
                    ErrorLabel.setVisible(true);
                    ErrorLabel.setText("Błąd");
                }
            }
            ///login failed
            else {
                ErrorLabel.setVisible(true);
                ErrorLabel.setText("Nieprawidłowa nazwa użytkownika lub hasło.");
            }
        }
    }

    public void onLogInButtonClick(ActionEvent event) {
        ///Handle Login
        handleLogin(event);
    }

    public void onRegisterViewButtonClick(ActionEvent event) {
        ///handle register scene change

        try{
            ///get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());

            ///now switch active scene to register-view.fxml
            SceneManager.switchScene(stage, "/org/example/quizez/register-view.fxml", null, loader -> {
                RegisterViewController controller = loader.getController();
                controller.initData("/org/example/quizez/first-login-view.fxml");
            });


        }catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

    public void onIsGuestCheckBoxClick(ActionEvent event) {
        boolean isGuest = IsGuestCheckBox.isSelected();

        ///Disable login fields when isGuest
        UsernameInputField.setDisable(isGuest);
        PasswordInputField.setDisable(isGuest);

        ///Also clear fields when isGuest

        if(isGuest){
            UsernameInputField.clear();
            PasswordInputField.clear();
        }

        //handleLogin(event);
    }

    private Integer authenticateUser(String username, String password) {
        String passCheck = "SELECT id, password FROM users WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(passCheck)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                ///simplext comp
                if (storedPassword.equals(password)) {
                    return rs.getInt("id");  /// return user ID
                } else {
                    return null; /// password mismatch
                }
            } else {
                return null; /// no such user
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; /// on error deny login
        }
    }


}
