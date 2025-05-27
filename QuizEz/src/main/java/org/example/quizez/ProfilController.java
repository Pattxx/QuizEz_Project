package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EventObject;

public class ProfilController {
    @FXML private PasswordField Password1;
    @FXML private PasswordField Password2;
    @FXML private Label ErrorLabel;

    public void onReturnClick(ActionEvent event) {

        try{
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(event.getSource());

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage,"/org/example/quizez/main-view.fxml", null);
        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
    public void onWyloguj(ActionEvent event) {

        SessionController.logout();
        try{
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(event.getSource());

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage,"/org/example/quizez/main-view.fxml", null);
        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }

    }
    public void onChangePasswordClick(ActionEvent event){
        String newPassword = Password1.getText();
        String newRepeatPassword = Password2.getText();

        ///any field is empty
        if(newPassword.isEmpty() || newRepeatPassword.isEmpty()){
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Wypełnij wszystkie pola!");
            return;
        }
        ///passwords do not match
        if(!newPassword.equals(newRepeatPassword)){
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Hasła są różne!");
            return;
        }


        ErrorLabel.setText("");
        boolean success = ChangePassword(SessionController.getCurrentUserId().toString(), newPassword);
        if(success){
            ErrorLabel.setVisible(true);
            ErrorLabel.setTextFill(Paint.valueOf("green"));
            ErrorLabel.setText("Hasło zmienione pomyślnie");
        } else {
            ErrorLabel.setVisible(true);
            ErrorLabel.setTextFill(Paint.valueOf("red"));
            ErrorLabel.setText("Nie udało się zmienić hasła");
        }
    }

    private boolean ChangePassword(String id, String password){
        String insertNewUserDb = "UPDATE users SET password = ? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertNewUserDb)){

            stmt.setString(1, password);
            stmt.setString(2, id);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; ///if true then it's a success

        }catch(SQLException e){
            System.err.println("Failed to create acc" + e.getMessage());
            return false; ///fail-safe
        }
    }
}
