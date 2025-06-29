package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.ResourceBundle;

public class ProfilController  implements Initializable  {
    @FXML private PasswordField Password1;
    @FXML private PasswordField Password2;
    @FXML private Label ErrorLabel;
    @FXML private VBox Quizy;
    @FXML private Label SredniWynik;
    @FXML private Label LiczbaQuiz;

    public void initialize(URL url, ResourceBundle rb) {
        float liczQuiz=0;
        float srednia = 0;
        String DbUsernameCheckQuery = "Select quizzes.title,quizzes.category,quiz_results.percentage,quiz_results.date_taken FROM quiz_results JOIN quizzes ON quiz_results.quiz_id = quizzes.id WHERE creator_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){
             stmt.setInt(1, SessionController.currentUserId);
             ResultSet rs = stmt.executeQuery();
            HBox temp = new HBox();
            temp.getChildren().addAll(new Label("Tytuł"), new Label("Kategoria"), new Label("Wynik"),new Label("data"));
            Quizy.getChildren().add(temp);
            /// loops through every element iun table and adds it to correct VBox

            while(rs.next()) {
                liczQuiz++;
                HBox record = new HBox();
                record.getChildren().addAll(new Label(rs.getString("title")), new Label(rs.getString("category")), new Label(rs.getString("percentage")), new Label(rs.getString("date_taken")));
                Quizy.getChildren().add(record);
                srednia=Float.valueOf(rs.getString("percentage"));
            }
            LiczbaQuiz.setText(Float.toString(liczQuiz));
            SredniWynik.setText(Float.toString(srednia/liczQuiz));
        }
        catch (SQLException e){
            System.err.println("Connection failed :(" + e.getMessage());
        }


    }

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
