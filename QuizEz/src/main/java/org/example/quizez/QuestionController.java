package org.example.quizez;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {
    @FXML
    private ToggleGroup Quest;
    @FXML
    private ProgressBar ProgressPytania;
    @FXML
    private Text Question;
    @FXML
    private VBox AnswerContainer;
    @FXML
    private Label QuestionXY;
    private int answerAmount;
    private int control;
    private int offset=1;
    private String correctAnswer;

    /// code activates on intialization to make sure first question is loaded
    public void initialize(URL location, ResourceBundle resources) {
        ///getting the number of all questions
        String questionQuery = "SELECT COUNT(*) FROM questions WHERE quiz_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setString(1, StaticController.QuizzId);
            ResultSet questionSet = stmt.executeQuery();
            StaticController.questionAmount=questionSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /// displaying first question
        questionQuery = "SELECT id, text FROM questions WHERE quiz_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setString(1, StaticController.QuizzId);
            ResultSet questionSet = stmt.executeQuery();
            questionSet.getFetchSize();
            Question.setText(questionSet.getString("text"));
            control = questionSet.getInt(1);
            handleAnswer();
            control++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        QuestionXY.setText("Pytanie"+ 1+" z "+  StaticController.questionAmount);
    }
    /// handling generation of answers
    private void handleAnswer() {
        String conStr = Integer.toString(control);
        ///getting the number of answers
        String questionQuery = "SELECT COUNT(*) FROM answers WHERE question_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setString(1, conStr);
            ResultSet questionSet = stmt.executeQuery();
            answerAmount=questionSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ///displaying answers
        String answerQuery = "SELECT text, is_correct FROM Answers WHERE question_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(answerQuery)) {

            stmt.setString(1, conStr);
            ResultSet answerRs = stmt.executeQuery();
            answerRs.next();
            ///making sure toggle group is clear
            Quest.getToggles().clear();
            for(int i=0;i<answerAmount;i++){
                addAnswerBlock(answerRs.getString("text"));
                if(answerRs.getInt("is_correct")==1){
                    correctAnswer=answerRs.getString("text");
                }
                answerRs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /// taking care of next question
    public void onNext(ActionEvent event) {

        RadioButton selected=(RadioButton) Quest.getSelectedToggle();
        ///checking if an answer has been selected
        if(selected!=null) {
            ///checking if the answer is correct
            if (Objects.equals(selected.getText(), correctAnswer)) {
                StaticController.correctAnswers++;
            }
            /// clearing previous answers and updating displays
            AnswerContainer.getChildren().clear();
            ProgressPytania.setProgress((1f/answerAmount)*offset);
            handleQuestion();
            QuestionXY.setText("Pytanie"+offset+" z "+  StaticController.questionAmount);

        }

    }
    /// handling new questions
    private void handleQuestion() {
        String questionQuery = "SELECT id, text FROM questions WHERE quiz_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setString(1, StaticController.QuizzId);
            ResultSet questionSet = stmt.executeQuery();
            /// question offset
            for(int i=0; i<offset;i++){
                questionSet.next();
            }
            if(!questionSet.next()){
                try{
                    ///Get stage from clicked button - needed as argument for next
                    Stage stage = SceneManager.getStageFrom(AnswerContainer);
                    ///Now change scene to previous: result-view.fxml
                    SceneManager.switchScene(stage,"/org/example/quizez/result-view.fxml", null);
                } catch(IOException e){
                    e.printStackTrace();
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setHeaderText(null);
                    alert1.setContentText("Scene change failed");
                    alert1.showAndWait();

                }
            }
            Question.setText(questionSet.getString("text"));
            control = questionSet.getInt(1);
            handleAnswer();
            control++;
            offset++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /// generating answers
    private void addAnswerBlock(String answer) {

        RadioButton answerCorrectRadio = new RadioButton();
        answerCorrectRadio.setToggleGroup(Quest);
        answerCorrectRadio.setText(answer);

        ///add radio button to the container
        AnswerContainer.getChildren().add(answerCorrectRadio);
    }

    public void onMainMenuClick(ActionEvent event) {

        try {
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(AnswerContainer);

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
}
