package org.example.quizez.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.quizez.managers.Database;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.managers.StaticController;
import org.example.quizez.sessionControllers.SessionController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuItem Zaloguj;
    @FXML
    private MenuItem Zarejestruj;
    @FXML
    private MenuItem Wyloguj;
    @FXML
    private Button Kategorie;
    @FXML
    ImageView Icon;

    @Override
    ///handle load
    public void initialize(URL location, ResourceBundle resources) {
        if (SessionController.getCurrentUserId() == null) {
            /// not logged user - impossible
            Zaloguj.setDisable(false);
            Zarejestruj.setDisable(false);
            Wyloguj.setDisable(true);
        } else if (SessionController.isGuest()) {
            ///guest user
            Zaloguj.setDisable(true);
            Zarejestruj.setDisable(true);
            Wyloguj.setDisable(false);
        } else {
            ///normal user
            Zaloguj.setDisable(true);
            Zarejestruj.setDisable(true);
            Wyloguj.setDisable(false);
            ///setting icon
            String DbUsernameCheckQuery = "Select avatar_path FROM user_profile WHERE user_id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){
                stmt.setInt(1, SessionController.currentUserId);
                ResultSet rs = stmt.executeQuery();
                if(rs.getString("avatar_path" )!= null){
                    StaticController.IconPath=rs.getString("avatar_path");
                }
            }
            catch (SQLException e){
                System.err.println("Connection failed :(" + e.getMessage());
            }
            Image image = new Image(StaticController.IconPath);
            Icon.setImage(image);
        }
    }
    ///Handle logout
    public void onWyloguj(ActionEvent event) {
        SessionController.logout();

        Zaloguj.setDisable(false);
        Zarejestruj.setDisable(false);
        Wyloguj.setDisable(true);

        try {
            Stage stage = SceneManager.getStageFrom(Kategorie);
            SceneManager.switchScene(stage, "/org/example/quizez/first-login-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
    ///handle login click
    public void onLoginClick(ActionEvent event) {
        try {
            Stage stage = SceneManager.getStageFrom(Kategorie);
            SceneManager.switchScene(stage, "/org/example/quizez/login-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
    ///handle register click
    public void onRegisterClick(ActionEvent event) {
        try {
            Stage stage = SceneManager.getStageFrom(Kategorie);
            SceneManager.switchScene(stage, "/org/example/quizez/register-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
    ///handle click of kategorie/utworzQuiz/twojeQuizy/Profil
    public void onButtonClick(ActionEvent event) {
        try {
            String temp = "/org/example/quizez/main-view.fxml";
            String path = event.getSource().toString();

            Stage stage = SceneManager.getStageFrom(event.getSource());

            if (path.contains("id=Kategorie")) {
                if (SessionController.getCurrentUserId() != null && !SessionController.isGuest()) {
                    temp = "/org/example/quizez/category-view.fxml";
                } else {
                    // guest or not logged in -> tell them to log in
                    boolean confirmed = showLoginRequiredAlert();
                    if (confirmed) {
                        temp = "/org/example/quizez/login-view.fxml";
                    } else {
                        return; ///do nothing
                    }
                }

            } else if (path.contains("id=utworzQuiz")) {
                if (SessionController.getCurrentUserId() != null && !SessionController.isGuest()) {
                    temp = "/org/example/quizez/createQuiz1-view.fxml";
                } else {
                    // guest or not logged in -> tell them to log in
                    boolean confirmed = showLoginRequiredAlert();
                    if (confirmed) {
                        temp = "/org/example/quizez/login-view.fxml";
                    } else {
                        return; ///do nothing
                    }
                }

            } else if (path.contains("id=twojeQuizy")) {
                if (SessionController.getCurrentUserId() != null && !SessionController.isGuest()) {
                    temp = "/org/example/quizez/your-view.fxml";
                } else {
                    boolean confirmed = showLoginRequiredAlert();
                    if (confirmed) {
                        temp = "/org/example/quizez/login-view.fxml";
                    } else {
                        return;
                    }
                }

            } else if (path.contains("id=Profil")) {
                if (SessionController.getCurrentUserId() != null && !SessionController.isGuest()) {
                    temp = "/org/example/quizez/profil-view.fxml";
                } else {
                    boolean confirmed = showLoginRequiredAlert();
                    if (confirmed) {
                        temp = "/org/example/quizez/login-view.fxml";
                    } else {
                        return;
                    }
                }
            }

            SceneManager.switchScene(stage, temp, null);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

    ///alert used to make guest user to log in
    private boolean showLoginRequiredAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logowanie wymagane");
        alert.setHeaderText(null);
        alert.setContentText("Ta funkcja wymaga zalogowania. Czy chcesz przejść do ekranu logowania?");

        var result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }
}
