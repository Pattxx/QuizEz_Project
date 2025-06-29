package org.example.quizez.editControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.model.Answer;
import org.example.quizez.model.Question;
import org.example.quizez.managers.Database;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditQuestionController {

    private int quizId;
    private String quizTitle;
    private String quizCategory;
    private String quizDifficulty;
    private int quizDuration;

    @FXML private ListView<Question> questionsListView;
    @FXML private TextArea questionTextArea;
    @FXML private VBox answersContainer;
    private ToggleGroup answersToggleGroup = new ToggleGroup();

    private List<Question> questionList = new ArrayList<>();


    ///initialize passed data from prev window (UsersQuiz) and load questions
    public void initData(int id, String title, String category, String difficulty, int duration) {
        this.quizId = id;
        this.quizTitle = title;
        this.quizCategory = category;
        this.quizDifficulty = difficulty;
        this.quizDuration = duration;

        loadQuestionsFromDB();
        setupQuestionsListView();
    }


    ///connect to database and load questions coresponding to selected previously quiz
    private void loadQuestionsFromDB() {
        questionList.clear();

        String queryQuestions = "SELECT id, text FROM questions WHERE quiz_id = ?";
        String queryAnswers = "SELECT id, text, is_correct FROM answers WHERE question_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement psQ = conn.prepareStatement(queryQuestions);
             PreparedStatement psA = conn.prepareStatement(queryAnswers)) {

            psQ.setInt(1, quizId);
            ResultSet rsQ = psQ.executeQuery();

            ///while next record exists - return questions with coresponding anwsers
            while (rsQ.next()) {
                int questionId = rsQ.getInt("id");
                String questionText = rsQ.getString("text");

                psA.setInt(1, questionId);
                ResultSet rsA = psA.executeQuery();

                List<Answer> answers = new ArrayList<>();
                while (rsA.next()) {
                    answers.add(new Answer(
                            rsA.getString("text"),
                            rsA.getInt("is_correct") == 1
                    ));
                }
                questionList.add(new Question(questionId, questionText, answers));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ///listView that is made of available quiz questions from db
    private void setupQuestionsListView() {
        questionsListView.getItems().setAll(questionList);
        questionsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getQuestionText());
            }
        });

        questionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldQ, newQ) -> {
            if (newQ != null) {
                displayQuestion(newQ);
            }
        });

        if (!questionList.isEmpty()) {
            questionsListView.getSelectionModel().selectFirst();
        }
    }

    private void displayQuestion(Question question) {
        questionTextArea.setText(question.getQuestionText());
        answersContainer.getChildren().clear();
        answersToggleGroup.getToggles().clear();

        for (Answer answer : question.getAnswers()) {
            HBox answerBox = createAnswerBox(answer);
            answersContainer.getChildren().add(answerBox);
        }
    }


    ///similar usage from CreateQuizController2
    private HBox createAnswerBox(Answer answer) {
        RadioButton radio = new RadioButton();
        radio.setToggleGroup(answersToggleGroup);
        radio.setSelected(answer.isCorrect());

        TextField answerField = new TextField(answer.getText());

        Button deleteBtn = new Button("X");
        deleteBtn.setOnAction(e -> {
            answersContainer.getChildren().remove(radio.getParent());
            if (answersToggleGroup.getSelectedToggle() == null && !answersToggleGroup.getToggles().isEmpty()) {
                answersToggleGroup.selectToggle(answersToggleGroup.getToggles().get(0));
            }
        });

        HBox box = new HBox(10, radio, answerField, deleteBtn);
        return box;
    }

    ///add @FXML anwser
    @FXML private void onAddAnswer() {
        RadioButton radio = new RadioButton();
        radio.setToggleGroup(answersToggleGroup);

        TextField answerField = new TextField();
        answerField.setPromptText("Nowa odpowiedź");

        Button deleteBtn = new Button("X");
        deleteBtn.setOnAction(e -> {
            answersContainer.getChildren().remove(radio.getParent());
            if (answersToggleGroup.getSelectedToggle() == null && !answersToggleGroup.getToggles().isEmpty()) {
                answersToggleGroup.selectToggle(answersToggleGroup.getToggles().get(0));
            }
        });

        HBox box = new HBox(10, radio, answerField, deleteBtn);
        answersContainer.getChildren().add(box);
    }

    ///Save changes - contains validation
    @FXML
    private void onSaveQuestion() {
        Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            alertError("Wybierz pytanie do edycji");
            return;
        }

        ///check if question field is empty
        String newQuestionText = questionTextArea.getText();
        if (newQuestionText.isEmpty()) {
            alertError("Treść pytania nie może być pusta");
            return;
        }

        List<Answer> newAnswers = new ArrayList<>();
        boolean correctSelected = false;

        for (var node : answersContainer.getChildren()) {
            if (node instanceof HBox hBox) {
                RadioButton radio = (RadioButton) hBox.getChildren().get(0);
                TextField answerField = (TextField) hBox.getChildren().get(1);

                ///check if anwser is empty
                String answerText = answerField.getText().trim();
                if (answerText.isEmpty()) {
                    alertError("Odpowiedzi nie mogą być puste");
                    return;
                }

                if (radio.isSelected()) {
                    correctSelected = true;
                }

                newAnswers.add(new Answer(answerText, radio.isSelected()));
            }
        }

        if (!correctSelected) {
            alertError("Wybierz poprawną odpowiedź");
            return;
        }
        ///prevent user from saving quiz that only has one qnwser
        if (newAnswers.size() < 2) {
            alertError("Pytanie musi mieć co najmniej 2 odpowiedzi");
            return;
        }
        /// update the database with new changes
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            String updateQuestion = "UPDATE questions SET text = ? WHERE id = ?";
            try (PreparedStatement psQ = conn.prepareStatement(updateQuestion)) {
                psQ.setString(1, newQuestionText);
                psQ.setInt(2, selectedQuestion.getId());
                psQ.executeUpdate();
            }

            /// delete old anwsers
            try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM answers WHERE question_id = ?")) {
                psDel.setInt(1, selectedQuestion.getId());
                psDel.executeUpdate();
            }

            ///add new anwsers
            try (PreparedStatement psIns = conn.prepareStatement(
                    "INSERT INTO answers (question_id, text, is_correct) VALUES (?, ?, ?)")) {
                for (Answer answer : newAnswers) {
                    psIns.setInt(1, selectedQuestion.getId());
                    psIns.setString(2, answer.getText());
                    psIns.setInt(3, answer.isCorrect() ? 1 : 0);
                    psIns.executeUpdate();
                }
            }

            conn.commit();

            ///refresh question list
            selectedQuestion.setText(newQuestionText);
            selectedQuestion.setAnswers(newAnswers);
            questionsListView.refresh();

            alertInfo("Pytanie zostało zaktualizowane pomyślnie");

        } catch (SQLException e) {
            e.printStackTrace();
            alertError("Błąd podczas zapisu do bazy danych");
        }
    }

    private void alertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void alertInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
