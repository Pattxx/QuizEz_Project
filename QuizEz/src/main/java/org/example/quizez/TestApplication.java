package org.example.quizez;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< Updated upstream
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("category-view.fxml"));
=======
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("login-view.fxml"));
>>>>>>> Stashed changes
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("QuizEz");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}