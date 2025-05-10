package org.example.quizez;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

    /**
     * Przełącza scenę w aktualnym oknie (Stage).
     * @param stage obecny stage (okno)
     * @param fxmlPath ścieżka do pliku FXML (np. "main.fxml")
     * @param title tytuł okna (może być null)
     * @throws IOException jeśli FXML nie zostanie załadowany
     */
    public static void switchScene(Stage stage, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        if (title != null) {
            stage.setTitle(title);
        }
        stage.show();
    }

    /**
     * Pomocnicza metoda do pobrania Stage z kontrolera.
     * @param source dowolny element kontrolki (np. przycisk)
     * @return aktualny Stage
     */
    public static Stage getStageFrom(Object source) {
        return (Stage) ((javafx.scene.Node) source).getScene().getWindow();
    }
}
