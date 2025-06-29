module org.example.quizez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.quizez to javafx.fxml;
    exports org.example.quizez;
    exports org.example.quizez.model;
    opens org.example.quizez.model to javafx.fxml;
    exports org.example.quizez.quizCreateControllers;
    opens org.example.quizez.quizCreateControllers to javafx.fxml;
    exports org.example.quizez.editControllers;
    opens org.example.quizez.editControllers to javafx.fxml;
    exports org.example.quizez.loginControllers;
    opens org.example.quizez.loginControllers to javafx.fxml;
    exports org.example.quizez.managers;
    opens org.example.quizez.managers to javafx.fxml;
    exports org.example.quizez.sessionControllers;
    opens org.example.quizez.sessionControllers to javafx.fxml;
    exports org.example.quizez.controllers;
    opens org.example.quizez.controllers to javafx.fxml;
}