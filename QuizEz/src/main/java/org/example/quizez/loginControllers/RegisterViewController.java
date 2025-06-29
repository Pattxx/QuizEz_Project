package org.example.quizez.loginControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.managers.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterViewController {
    @FXML private TextField UsernameInputField;
    @FXML private PasswordField PasswordInputField;
    @FXML private PasswordField RepeatPasswordInputField;
    @FXML private Button CreateAccButton;
    @FXML private Button GoBackButton;
    @FXML private Label ErrorLabel;

    ///var used to return to prev scene
    private String previousSceneFxml;

    public void initData(String previousSceneFxml) {
        this.previousSceneFxml = previousSceneFxml;
    }

    public void onGoBackAccClick(ActionEvent event) {

        try{
            Stage stage = SceneManager.getStageFrom(event.getSource());
            SceneManager.switchScene(stage, previousSceneFxml, null);
        } catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();

        }

    }

    public void onCreateAccButtonClick() {
        handleAccCreation();
    }


    private void handleAccCreation(){
        String newUsername = UsernameInputField.getText();
        String newPassword = PasswordInputField.getText();
        String newRepeatPassword = PasswordInputField.getText();

        ///any field is empty
        if(newUsername.isEmpty() || newPassword.isEmpty() || newRepeatPassword.isEmpty()){
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Wypełnij wszystkie pola!");
            return;
        }
        ///passwords do not match
        if(!newPassword.equals(newRepeatPassword)){
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Hasła są różne!");
            return;
        }

        ///username is taken
        if(isUsernameTaken(newUsername)){
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Nazwa jest już zajęta.");
            return;
        }

        ///reset errorlabel text
        ErrorLabel.setText("");

        ///Proceed with acc creation
        boolean success = createAccount(newUsername, newPassword);
        if(success){
            ErrorLabel.setVisible(true);
            ErrorLabel.setTextFill(Paint.valueOf("green"));
            ErrorLabel.setText("Utworzono użytkownika, możesz się teraz zalogować");
        } else {
            ErrorLabel.setVisible(true);
            ErrorLabel.setTextFill(Paint.valueOf("red"));
            ErrorLabel.setText("Utworzenie użytkownika nie powiodło się. Spróbuj ponownie.");
        }
    }

    private boolean isUsernameTaken(String usernametoCheck){

        ///here "?" acts like a placeholder value, we set it using stmt.SetString(...)
        String DbUsernameCheckQuery = "Select COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){
            ///using prepared statement, execute query with set value
            ///index "1" - juest refers to first "?" placeholder (can be multiple)
            stmt.setString(1, usernametoCheck);
            ResultSet rs = stmt.executeQuery();
            ///this works like this:
            ///rs.next() moves "cursor" to the first (and only here) result row
            ///rs.getInt(1) gets value of first column - it's the count
            ///now if count is greater than then username exists in DB and returns true
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e){
            System.err.println("Connection failed :(" + e.getMessage());
            return true; ///fail-safe
        }
    }
    ///handle cuser creation
    private boolean createAccount(String username, String password){
        /// insert username into user_profile table
        String insertNewUserDb = "INSERT INTO user_profile (display_name) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertNewUserDb)){
                stmt.setString(1, username);
                stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Failed to create acc" + e.getMessage());
        }
        /// insert data into users table
        insertNewUserDb = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertNewUserDb)){

                stmt.setString(1, username);
                stmt.setString(2, password);

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0; ///if true then it's a success

        }catch(SQLException e){
            System.err.println("Failed to create acc" + e.getMessage());
            return false; ///fail-safe
        }

    }

}
