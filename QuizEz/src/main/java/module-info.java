module org.example.quizez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.quizez to javafx.fxml;
    exports org.example.quizez;
}