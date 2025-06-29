package org.example.quizez;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;

public class UsersQuiz implements Initializable {
    @FXML private ListView<String> ListaQuizow;
    @FXML private VBox buttonContainer;

    private List<Quiz> quizzes = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize called");
        System.out.println("curr userID: "+SessionController.currentUserId);
        ///moved logic to method
        loadQuizzes();

        ListaQuizow.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedTitle = ListaQuizow.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    Quiz selectedQuiz = quizzes.stream()
                            .filter(q -> q.getName().equals(selectedTitle))
                            .findFirst()
                            .orElse(null);

                    if (selectedQuiz != null) {
                        showEditButtons(selectedQuiz);
                    }
                }
            }
        });
    }


    ///changed from VBox to ObservableList
    private void loadQuizzes() {
        quizzes.clear();
        ListaQuizow.getItems().clear();
        buttonContainer.getChildren().clear();

        ///load all user quizzez based on their id
        String query = "SELECT * FROM quizzes WHERE creator_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ///get current user id
            stmt.setInt(1, SessionController.currentUserId);
            ResultSet rs = stmt.executeQuery();

            ///
            while (rs.next()) {
                int quizId = rs.getInt("id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                String difficulty = rs.getString("difficulty");
                int duration = rs.getInt("time_limit");

                Quiz quiz = new Quiz(quizId, title, category, difficulty, duration);
                quizzes.add(quiz);
                ListaQuizow.getItems().add(title);
                System.out.println("Loaded quiz: " + title);
            }

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    private void showEditButtons(Quiz quiz) {
        buttonContainer.getChildren().clear();

        Button editQuizBtn = new Button("Edytuj quiz");
        Button editQuestionsBtn = new Button("Edytuj pytania");

        editQuizBtn.setOnAction(e -> openEditQuizDialog(
                quiz.getId(), quiz.getName(), quiz.getCategory(), quiz.getDifficulty(), quiz.getDuration()));

        editQuestionsBtn.setOnAction(e -> openEditQuestionsScene(
                quiz.getId(), quiz.getName(), quiz.getCategory(), quiz.getDifficulty(), quiz.getDuration()));

        buttonContainer.getChildren().addAll(editQuizBtn, editQuestionsBtn);
    }

    ///this method opens small window where user can update basic info about chosen quiz
    private void openEditQuizDialog(int id, String title, String category, String difficulty, int duration) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/example/quizez/editQuiz-view.fxml"));
            //VBox root = loader.load();
            Parent root = loader.load();

            Quiz quiz = new Quiz(id, title, category, difficulty, duration);
            EditQuizController controller = loader.getController();
            controller.initData(quiz, this::loadQuizzes);

            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditQuestionsScene(int id, String title, String category, String difficulty, int duration) {
        System.err.println("edit questions");
        try {
            Stage stage = (Stage) ListaQuizow.getScene().getWindow();
            SceneManager.switchScene(stage, "/org/example/quizez/editQuestion-view.fxml", "Edytuj pytania", loader -> {
                EditQuestionController controller = loader.getController();
                controller.initData(id, title, category, difficulty, duration);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReturnClick(ActionEvent event) {
        try {
            Stage stage = SceneManager.getStageFrom(event.getSource());
            SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

    public void onProfilClick(ActionEvent event) {
        try {
            Stage stage = SceneManager.getStageFrom(event.getSource());
            SceneManager.switchScene(stage, "/org/example/quizez/profil-view.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }
}

