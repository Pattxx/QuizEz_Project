package org.example.quizez;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ResultController implements Initializable{
    @FXML
    private Label answered;
    @FXML
    private Label answeredPercent;

    public void initialize(URL location, ResourceBundle resources) {
        ///getting the number of all questions

        answered.setText( Static.correctAnswers+"/"+ Static.questionAmount);
        Float percent= ((float)Static.correctAnswers/ Static.questionAmount)*100;
        answeredPercent.setText(String.format("%.00f", percent)+"%");

        String questionQuery = "INSERT INTO quiz_results (user_id, quiz_id, score, total_questions, correct_answers, percentage, date_taken) VALUES (?,?,?,?,?,?,?);";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionQuery)) {

            stmt.setString(1, SessionController.currentUserId.toString());
            stmt.setString(2, Static.QuizzId);
            stmt.setInt(3, Static.correctAnswers);
            stmt.setInt(4, Static.questionAmount);
            stmt.setInt(5, Static.correctAnswers);
            double perc = 100.0 * Static.correctAnswers / Static.questionAmount;
            stmt.setDouble(6, perc);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            stmt.setString(7, timeStamp);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }



    ///Repeat the quizz
    public void onRepeatClick(ActionEvent event) {

        try {
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(answered);

            ///Now change scene to previous: login-view.fxml
            SceneManager.switchScene(stage, "/org/example/quizez/question-view.fxml", null);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }
    }
    ///Go back to main menu
    public void onMainMenuClick(ActionEvent event) {

        try {
            ///Get stage from clicked button - needed as argument for next
            Stage stage = SceneManager.getStageFrom(answered);

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
