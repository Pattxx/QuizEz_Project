package org.example.quizez;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.quizez.net.sqlitetuorial.Database;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML VBox gry;
    @FXML VBox geografia;
    @FXML VBox religie;
    @FXML VBox filmy;
    @FXML VBox historia;
    @FXML VBox kosmos;
    @FXML VBox nauki;
    @FXML VBox nieokreslone;
    /// Array lists will be needed for automation
    ArrayList<String> categ = new ArrayList<String>();
    ArrayList<VBox> contain = new ArrayList<VBox>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categ.add("Gry wideo");     contain.add(gry);
        categ.add("Geografia");     contain.add(geografia);
        categ.add("Religie świata");contain.add(religie);
        categ.add("Filmy i seriale");contain.add(filmy);
        categ.add("Historia świata");contain.add(historia);
        categ.add("Kosmos");        contain.add(kosmos);
        categ.add("Nauki ścisłe");  contain.add(nauki);
       // categ.add("Nieokreślone"); contain.add(nieokreslone);
        /// loops thorugh every Category
        for(int i=0;i<categ.size();i++){
            String DbUsernameCheckQuery = "Select * FROM quizzes WHERE category = ?";
            try (Connection conn = Database.getConnection();
                PreparedStatement stmt =conn.prepareStatement(DbUsernameCheckQuery)){

                stmt.setString(1, categ.get(i));
                ResultSet rs = stmt.executeQuery();
                    ///adds header in every category
                HBox temp = new HBox();
                temp.getChildren().addAll(new Label("title "), new Label("difficulty "), new Label("creator_id "));
                contain.get(i).getChildren().add(temp);
                /// loops through every element iun table and adds it to correct VBox
                while(rs.next()) {
                    HBox record = new HBox();
                    record.getChildren().addAll(new Label(rs.getString("title")), new Label(rs.getString("difficulty"), new Label(rs.getString("creator_id"))));
                    contain.get(i).getChildren().add(record);
                }
            }
            catch (SQLException e){
                System.err.println("Connection failed :(" + e.getMessage());
            }
        }
    }
    public void onButtonClick(ActionEvent event) {
        try{
            ///get stage from button
            Stage stage = SceneManager.getStageFrom(event.getSource());
            //now switch active scene to temp
            SceneManager.switchScene(stage, "/org/example/quizez/main-view.fxml", null);

        }catch(IOException e){
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(null);
            alert1.setContentText("Scene change failed");
            alert1.showAndWait();
        }
    }

}
