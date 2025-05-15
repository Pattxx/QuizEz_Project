package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {
    @FXML private TextField UsernameInputField;
    @FXML private PasswordField PasswordInputField;
    @FXML private Button LogInButton;
    @FXML private Button RegisterViewButton;
    @FXML private CheckBox IsGuestCheckBox;

    @FXML
    private void handleLogin(ActionEvent event) {
        if(IsGuestCheckBox.isSelected()){
            //nothing now - just make stage change possible
            Stage stage = SceneManager.getStageFrom(event.getSource());

            //TODO add DB quest logic


            //TODO uncomment when adis code is ready
            // SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);
        }

        //normal log in
        else{

        String user = UsernameInputField.getText();
        String pass = PasswordInputField.getText();

        // Prosta walidacja – demo bez bazy for now
        if (user.equals("admin") && pass.equals("1234")) {
            try {
                //load main screen scene if password correct
//                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
//                alert1.setTitle("Log in successful");
//                alert1.setHeaderText(null);
//                alert1.setContentText("Logi in yay");
//                alert1.showAndWait();

                    //get current scene
                    Stage stage = SceneManager.getStageFrom(event.getSource());

                    //change to main view
                    //TODO uncomment when adis code is ready
                    // SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);


            } catch (Exception e) {
                e.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText(null);
                alert1.setContentText("Scene change failed");
                alert1.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Błąd logowania");
            alert2.setHeaderText(null);
            alert2.setContentText("Niepoprawne dane!");
            alert2.showAndWait();
        }
    }
    }

    public void onLogInButtonClick(ActionEvent event) {
        //Handle Login
        handleLogin(event);

    }

    public void onRegisterViewButtonClick(ActionEvent event) {
        //handle register scene change

        try{
            //get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());

            //now switch active scene to register-view.fxml
            SceneManager.switchScene(stage, "/org/example/quizez/register-view.fxml", null);

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

        //Disable login fields when isGuest
        UsernameInputField.setDisable(isGuest);
        PasswordInputField.setDisable(isGuest);

        //Also clear fields when isGuest

        if(isGuest){
            UsernameInputField.clear();
            PasswordInputField.clear();
        }

        handleLogin(event);
    }
}
