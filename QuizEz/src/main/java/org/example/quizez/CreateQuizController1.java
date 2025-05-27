package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateQuizController1 {

    @FXML private Label CreateQuizLabel;
    @FXML private Label QuizNameLabel;
    @FXML private Label QuizCategoryLabel;
    @FXML private ChoiceBox<String> QuizCategoryChoiceBox;
    @FXML private Button GoToAddQuestionsSceneButton;
    @FXML private TextField QuizNameField;
    @FXML private Label ErrorLabel;

    @FXML
    public void initialize(){
        ///Add existing categories to choicebox
        QuizCategoryChoiceBox.getItems()
                .addAll("Nieokreślona", "Gry wideo", "Geografia", "Religie świata", "Filmy i seriale", "Historia świata", "Kosmos", "Nauki ścisłe");
        ///Set default value of choicebox to show text "nieokreslona"
        QuizCategoryChoiceBox.setValue("Nieokreślona");
    }

    @FXML
    public void onGoToAddQuestionsSceneButtonClick(ActionEvent event){
        String quizName = QuizNameField.getText();
        String categoryChosen = QuizCategoryChoiceBox.getValue();
        boolean success = checkIfCorrect(quizName);
        if(success){
            ///TODO Make interface from this
            GrantAccess(event);
        }
        else{
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Proszę wprowadzić nazwę quizu :)");
        }
    }

    private boolean checkIfCorrect(String quizName){
        ///Check if quiz name is valid
        if (QuizNameField.getText().isEmpty()){
            return false;
        }
        else return true;
    }

    private void GrantAccess(ActionEvent event){
        try{
            Stage stage = SceneManager.getStageFrom(event.getSource());

            SceneManager.switchScene(stage, "/org/example/quizez/createQuiz2-view.fxml", null);

        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
}
