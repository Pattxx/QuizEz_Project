package org.example.quizez;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizEzApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(QuizEzApp.class.getResource("first-login-view.fxml"));

        ///default size set to 1280x720
        Scene scene = new Scene(fxmlLoader.load(), 1280,720);
        stage.setScene(scene);
        stage.setTitle("QuizEz");
        stage.setResizable(false);
        //stage.setMaximized(true); ///leave space for max/min/exit
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}