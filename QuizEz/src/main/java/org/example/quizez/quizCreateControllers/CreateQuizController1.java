package org.example.quizez.quizCreateControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.managers.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateQuizController1 {

    @FXML public Label QuizDifficultyLabel;
    @FXML public Label QuizDurationLabel;
    @FXML public Button ReturnButton;
    @FXML private Label CreateQuizLabel;
    @FXML private Label QuizNameLabel;
    @FXML private Label QuizCategoryLabel;
    @FXML private ChoiceBox<String> QuizCategoryChoiceBox;
    @FXML private ChoiceBox<String> QuizDifficultyCheckbox;
    @FXML private Button GoToAddQuestionsSceneButton;
    @FXML private TextField QuizNameField;
    @FXML private TextField QuizDurationField;
    @FXML private Label ErrorLabel;

    @FXML
    public void initialize(){
        ///Add existing categories to choicebox
        QuizCategoryChoiceBox.getItems()
                .addAll("Nieokreślona", "Gry wideo", "Geografia", "Religie świata", "Filmy i seriale", "Historia świata", "Kosmos", "Nauki ścisłe");
        ///Set default value of choicebox to show text "nieokreslona"
        QuizCategoryChoiceBox.setValue("Nieokreślona");

        QuizDifficultyCheckbox.getItems().addAll("Łatwy", "Średni", "Trudny");
        QuizDifficultyCheckbox.setValue("Łatwy");

        QuizDurationField.setText("10");

        ///allow only numbers
        QuizDurationField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
    }

    @FXML
    public void onGoToAddQuestionsSceneButtonClick(ActionEvent event){
        String quizName = QuizNameField.getText();
        String categoryChosen = QuizCategoryChoiceBox.getValue();
        boolean success = checkIfCorrect(quizName);

        if(success){
            if (quizExists(quizName)) {
                ErrorLabel.setVisible(true);
                ErrorLabel.setText("Quiz o takiej nazwie już istnieje. Wybierz inną nazwę.");
            } else {
                GrantAccess(event);
            }
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

    ///check if quiz with that name already exists
    private boolean quizExists(String quizName) {
        String sql = "SELECT 1 FROM quizzes WHERE title = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quizName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();  ///if result exist then quiz already exists in db
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void GrantAccess(ActionEvent event){
        try{
            String quizName = QuizNameField.getText();
            String category = QuizCategoryChoiceBox.getValue();
            String quizDifficulty = QuizDifficultyCheckbox.getValue();
            int quizDuration = Integer.parseInt(QuizDurationField.getText());

            Stage stage = SceneManager.getStageFrom(event.getSource());

            ///pass all the quiz data to next scene
            SceneManager.switchScene(stage, "/org/example/quizez/createQuiz2-view.fxml", "Dodawanie pytań", loader -> {
                CreateQuizController2 controller = loader.getController();
                controller.initData(quizName, category, quizDifficulty, quizDuration);
            });


        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

    @FXML
    public void onReturnButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText(null);
        alert.setContentText("Czy na pewno chcesz wrócić?");

        ButtonType buttonYes = new ButtonType("Tak");
        ButtonType buttonNo = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                Stage stage = SceneManager.getStageFrom(event.getSource());;
                try {
                    SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Błąd");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Nie udało się przejść do głównego menu.");
                    errorAlert.showAndWait();
                }
            }
            /// if no - nothing happens
        });
    }
}
