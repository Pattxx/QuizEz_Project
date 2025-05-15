package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

public class MainController {

    public void onButtonClick(ActionEvent event) {
        try{
            String temp="/org/example/quizez/main-view.fxml",path=event.getSource().toString();
            //get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());
            if(path.contains("id=Kategorie")){
                temp="/org/example/quizez/category-view.fxml";
            }
            else if(path.contains("id=utworzQuiz")){
                temp="/org/example/quizez/creator-view.fxml";
            }
            else if(path.contains("id=twojeQuizy")){
                temp="/org/example/quizez/login-view.fxml";
            }
            else if(path.contains("id=Profil")){
                temp="/org/example/quizez/profil-view.fxml";
            }
            //now switch active scene to register-view.fxml
            SceneManager.switchScene(stage, temp, null);

        }catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
}
