package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class UsersQuiz {

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
