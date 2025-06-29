package org.example.quizez;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(ApplicationExtension.class)
class LoginControllerTest extends ApplicationTest {

    private LoginController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/quizez/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testEmptyFieldsShowsError() {
        clickOn("#LogInButton");
        assertTrue(controller.ErrorLabel.isVisible());
        assertEquals("Wypełnij wszystkie pola!", controller.ErrorLabel.getText());
    }
    @Test
    void shouldShowError(FxRobot robot) {
        // Type in wrong username
        robot.clickOn("#UsernameInputField")
                .write("wrongUser");

        // Type in wrong password
        robot.clickOn("#PasswordInputField")
                .write("wrongPassword");

        // Click login button
        robot.clickOn("#LogInButton");

        // Assert that the error label is now visible
        assertTrue(controller.ErrorLabel.isVisible());
        assertEquals("Nieprawidłowa nazwa użytkownika lub hasło.",controller.ErrorLabel.getText());

    }
}