package org.example.quizez.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.quizez.managers.Database;
import javafx.scene.control.*;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.managers.StaticController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML VBox gry;
    @FXML VBox geografia;
    @FXML VBox religie;
    @FXML VBox filmy;
    @FXML VBox historia;
    @FXML VBox kosmos;
    @FXML VBox nauki;
    @FXML VBox nieokreslone;
    /// Array lists will be needed for automation
    Map<String, String> hboxMap = new HashMap<String, String>();
    Map<String, VBox> categMap = new HashMap<String, VBox>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categMap.put("Gry wideo", gry);
        categMap.put("Geografia", geografia);
        categMap.put("Religie świata", religie);
        categMap.put("Filmy i seriale", filmy);
        categMap.put("Historia świata", historia);
        categMap.put("Kosmos", kosmos);
        categMap.put("Nauki ścisłe", nauki);
        //categMap.put("Nieokreślone", nieokreslone);
        /// loops thorugh every Category
        for (var entry : categMap.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            String DbUsernameCheckQuery = "Select * FROM quizzes WHERE category = ?";
            try (Connection conn = Database.getConnection();
                PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){

                stmt.setString(1, entry.getKey());
                ResultSet rs = stmt.executeQuery();
                    ///adds header in every category
                HBox temp = new HBox();
                temp.getChildren().addAll(new Label("Tytuł"),new Label("|"),new Label("Id Twórcy") , new Label("|"),
                        new Label("Poziom trudności") );
                entry.getValue().getChildren().add(temp);
                /// loops through every element iun table and adds it to correct VBox
                while(rs.next()) {
                    HBox record = new HBox();
                    hboxMap.put(record.toString(),rs.getString("id"));
                    ///Added Onclick functonality in order to choose the quizz
                    record.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try{
                                StaticController.QuizzId=hboxMap.get(mouseEvent.getSource().toString());
                                ///get stage from button
                                Stage stage = SceneManager.getStageFrom(mouseEvent.getSource());
                                //now switch active scene to temp
                                SceneManager.switchScene(stage, "/org/example/quizez/Question-view.fxml", null);

                            }catch(IOException e){
                                e.printStackTrace();
                                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                alert1.setHeaderText(null);
                                alert1.setContentText("Scene change failed");
                                alert1.showAndWait();
                            }
                        }
                    });
                    record.getChildren().addAll(new Label(rs.getString("title")), new Label("|"),
                            new Label(rs.getString("difficulty")), new Label("|"), new Label(rs.getString("creator_id")));
                    entry.getValue().getChildren().add(record);
                }
            }
            catch (SQLException e){
                System.err.println("Connection failed :(" + e.getMessage());
            }
        }
    }
    public void onButtonClick(ActionEvent event) {
        try{
            ///get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());
            //now switch active scene to temp
            SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);

        }catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

}
