package org.example.quizez.quizCreateControllers;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.sessionControllers.SessionController;
import org.example.quizez.model.Answer;
import org.example.quizez.model.Question;
import org.example.quizez.managers.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CreateQuizSummaryController {

    @FXML public Button ReturnButton;
    @FXML private Label NameLabel;
    @FXML private Button SaveQuizDataButton;
    @FXML private Label CategoryLabel;
    @FXML private Label DifficultyLabel;
    @FXML private Label TimeLabel;

    @FXML private ListView<Question> questionListView;
    private List<Question> questionList;
    private String quizName, category, difficulty;
    private int duration;


    ///get data regarding quiz (passed from previous controller)
    public void initData(String quizName, String category, String difficulty, int duration, List<Question> questions){
        this.quizName = quizName;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.questionList = questions;
        questionListView.getItems().setAll(questions);
        NameLabel.setText("Nazwa quizu: " + quizName);
        CategoryLabel.setText("Kategoria: "+ category);
        DifficultyLabel.setText("Poziom trudności:" + difficulty);
        TimeLabel.setText("Limit czasu: "+duration);
    }

    ///initialize basic quiz info
    public void initialize(){
        SaveQuizDataButton.setDisable(false);
    }



    ///save data to database
    public void onSaveQuizDataButton(ActionEvent event) {
        String insertQuizSQL = "INSERT INTO quizzes (title, category, difficulty, time_limit, creator_id) VALUES (?, ?, ?, ?, ?)";
        String insertQuestionSQL = "INSERT INTO questions (quiz_id, text) VALUES (?, ?)";
        String insertAnswerSQL = "INSERT INTO answers (question_id, text, is_correct) VALUES (?, ?, ?)";

        Integer creatorId = SessionController.getCurrentUserId();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);  ///transaction start

            int quizId;
            /// save quiz and generate quizID
            try (PreparedStatement quizStmt = conn.prepareStatement(insertQuizSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                quizStmt.setString(1, quizName);
                quizStmt.setString(2, category);
                quizStmt.setString(3, difficulty);
                quizStmt.setInt(4, duration);
                if (creatorId != null) {
                    quizStmt.setInt(5, creatorId);
                } else {
                    quizStmt.setNull(5, java.sql.Types.INTEGER);
                }
                quizStmt.executeUpdate();

                try (var generatedKeys = quizStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        quizId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Nie udało się pobrać ID nowego quizu.");
                    }
                }
            }

            /// question and answers prepared statements to pass into db
            try (PreparedStatement questionStmt = conn.prepareStatement(insertQuestionSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement answerStmt = conn.prepareStatement(insertAnswerSQL)) {

                /// for every question in a quiz
                for (Question question : questionList) {
                    /// save question
                    questionStmt.setInt(1, quizId);
                    questionStmt.setString(2, question.getQuestionText());
                    questionStmt.executeUpdate();

                    int questionId;
                    try (var generatedKeys = questionStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            questionId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Nie udało się pobrać ID pytania.");
                        }
                    }

                    /// save question answers
                    List<Answer> answers = question.getAnswers();
                    for (Answer answer : answers) {
                        answerStmt.setInt(1, questionId);
                        answerStmt.setString(2, answer.getText());
                        answerStmt.setBoolean(3, answer.isCorrect());
                        answerStmt.addBatch();
                    }
                    answerStmt.executeBatch();
                }
            }

            conn.commit();  ///commit changes into db
            SaveQuizDataButton.setDisable(true);
            System.out.println("Quiz i pytania zapisane pomyślnie!");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Quiz zapisany");
            alert.setHeaderText(null);
            alert.setContentText("Quiz został zapisany pomyślnie!");
            alert.getButtonTypes().setAll(ButtonType.OK, new ButtonType("Przejdź do głównego menu"));

            alert.showAndWait().ifPresent(response -> {
                if (response.getText().equals("Przejdź do głównego menu")) {
                    Stage stage = SceneManager.getStageFrom(event.getSource());;
                    try {
                        SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch(SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Błąd");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Nie udało się zapisać quizu.");
                    ButtonType cancelBtn = new ButtonType("Ok");
                    errorAlert.getButtonTypes().setAll(cancelBtn);
                    SaveQuizDataButton.setDisable(false);
                }
            }
            e.printStackTrace();
            ///close connection
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void onReturnButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText(null);
        alert.setContentText("Czy na pewno chcesz wrócić do głównego menu?");

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
