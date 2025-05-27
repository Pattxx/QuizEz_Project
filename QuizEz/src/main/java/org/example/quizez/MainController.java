package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
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

    public void initialize(URL location, ResourceBundle resources) {
        if(SessionController.getCurrentUserId()!= null){
            Zaloguj.setDisable(true);
            Zarejestruj.setDisable(true);
        }
        else{
            Wyloguj.setDisable(true);
        }
    }

    public void onWyloguj(ActionEvent event) {

        SessionController.logout();

            Zaloguj.setDisable(false);
            Zarejestruj.setDisable(false);
            Wyloguj.setDisable(true);
    }

    public void onLoginClick(ActionEvent event) {

        try {
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(Kategorie);

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage, "/org/example/quizez/login-view.fxml", null);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
        public void onRegisterClick(ActionEvent event) {

            try {
                ///Get stage from clicked button - needed as argument for next
                Stage stage = SceneManager.getStageFrom(Kategorie);

                ///Now change scene to previous: login-view.fxml
                SceneManager.switchScene(stage, "/org/example/quizez/register-view.fxml", null);

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText(null);
                alert1.setContentText("Scene change failed");
                alert1.showAndWait();

            }
        }


    public void onButtonClick(ActionEvent event) {
        try{
            String temp="/org/example/quizez/main-view.fxml",path=event.getSource().toString();
            ///get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());
            ///Changes temp to proper path depending on needs
            if(path.contains("id=Kategorie")){
                temp="/org/example/quizez/category-view.fxml";
            }
            else if(path.contains("id=utworzQuiz")){
                if(SessionController.getCurrentUserId()!= null){
                    temp="/org/example/quizez/creator-view.fxml";
                }
                else temp="/org/example/quizez/login-view.fxml";
            }
            else if(path.contains("id=twojeQuizy")){
                if(SessionController.getCurrentUserId()!= null){
                    temp="/org/example/quizez/your-view.fxml";
                }
                else temp="/org/example/quizez/login-view.fxml";
            }
            else if(path.contains("id=Profil")){
                if(SessionController.getCurrentUserId()!= null){
                    temp="/org/example/quizez/profil-view.fxml";
                }
                else temp="/org/example/quizez/login-view.fxml";
            }
            //now switch active scene to temp
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
