module org.example.quizez {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.quizez to javafx.fxml;
    exports org.example.quizez;
}