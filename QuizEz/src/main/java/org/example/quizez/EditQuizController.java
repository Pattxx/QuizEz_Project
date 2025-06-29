package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.example.quizez.Quiz;
import org.example.quizez.net.sqlitetuorial.Database;
public class EditQuizController {
    @FXML private TextField quizNameField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private TextField durationField;

    private Quiz quiz;
    private Runnable onSavedCallback;

    public void initData(Quiz quiz, Runnable onSavedCallback) {
        this.quiz = quiz;
        this.onSavedCallback = onSavedCallback;

        ///allow omly numbers
        durationField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
        ///pass quizz data from db into fields
        quizNameField.setText(quiz.getName());
        durationField.setText(String.valueOf(quiz.getDuration()));
        categoryComboBox.getItems().addAll("Nieokreślona", "Gry wideo", "Geografia", "Religie świata", "Filmy i seriale", "Historia świata", "Kosmos", "Nauki ścisłe");
        difficultyComboBox.getItems().addAll("Łatwy", "Średni", "Trudny");

        categoryComboBox.setValue(quiz.getCategory());
        difficultyComboBox.setValue(quiz.getDifficulty());
    }

    public void onSaveClick(ActionEvent event) {
        String newName = quizNameField.getText();
        String newCategory = categoryComboBox.getValue();
        String newDifficulty = difficultyComboBox.getValue();
        int newDuration = Integer.parseInt(durationField.getText());

        quiz.setName(newName);
        quiz.setCategory(newCategory);
        quiz.setDifficulty(newDifficulty);
        quiz.setDuration(newDuration);

        boolean success = Database.updateQuiz(quiz.getId(), newName, newCategory, newDifficulty, newDuration);

        if (success) {
            ((Stage) quizNameField.getScene().getWindow()).close();
            //reload main scene
            if (onSavedCallback != null) onSavedCallback.run();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Błąd zapisu");
            alert.setContentText("Nie udało się zaktualizować quizu.");
            alert.showAndWait();
        }
    }

    public void onCancelClick(ActionEvent event) {
        ((Stage) quizNameField.getScene().getWindow()).close();
    }
}
