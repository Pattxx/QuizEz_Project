package org.example.quizez;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UsersQuiz implements Initializable {
    @FXML
    VBox ListaQuizow;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String DbUsernameCheckQuery = "Select * FROM quizzes WHERE creator_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){
            stmt.setInt(1, SessionController.currentUserId);
            ResultSet rs = stmt.executeQuery();
            ///adds header in every category
            HBox temp = new HBox();
            temp.getChildren().addAll(new Label("title "), new Label("difficulty "));
            ListaQuizow.getChildren().add(temp);
            /// loops through every element iun table and adds it to correct VBox
            while(rs.next()) {
                HBox record = new HBox();
                ///Added Onclick functonality in order to choose the quizz
                record.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });
                record.getChildren().addAll(new Label(rs.getString("title")), new Label(rs.getString("difficulty")));
                ListaQuizow.getChildren().add(record);
            }
        }
        catch (SQLException e){
            System.err.println("Connection failed :(" + e.getMessage());
        }


    }
    public void onReturnClick(ActionEvent event) {

        try{

            Stage stage = SceneManager.getStageFrom(event.getSource());

            SceneManager.switchScene(stage,"/org/example/quizez/main-view.fxml", null);
        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
    public void onProfilClick(ActionEvent event) {

        try{
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(event.getSource());

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage,"/org/example/quizez/profil-view.fxml", null);
        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
}
