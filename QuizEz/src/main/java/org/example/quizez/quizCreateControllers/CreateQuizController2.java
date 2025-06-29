package org.example.quizez.quizCreateControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.quizez.managers.SceneManager;
import org.example.quizez.model.Answer;
import org.example.quizez.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateQuizController2 {

    private List<Question> questionList = new ArrayList<>();
    private String quizName;
    private String category;
    private String difficulty;
    private int duration;

    ///initialize passed data from previous view
    public void initData(String quizName, String category, String difficulty, int duration){
        this.quizName = quizName;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
    }


    @FXML private TextArea QuestionTextArea;
    @FXML private VBox answersContainer;
    @FXML private Button AddMoreAnswsersButton;
    @FXML private Button SaveQuizButton;
    @FXML private Button GoToSummaryButton;
    @FXML private Label WarningLabel;


    ///ToggleGroup used to determine which one of the answers is correct
    private ToggleGroup AnswserCorrectToggle = new ToggleGroup();

    ///MIN and MAX values used to generate answer containers
    private final int MAX_ANSWERS = 4;
    private final int MIN_ANSWERS = 2;

    ///On load add 2 answer containers
    public void initialize() {
        WarningLabel.setVisible(true);
        GoToSummaryButton.setDisable(true);

        for (int i = 0; i < MIN_ANSWERS; i++) {
            addAnswerBlock();
        }
        updateButtons();
    }

    ///handles adding more answer blocks to scene
    @FXML
    private void onAddMoreAnswsersButtonClick() {
        ///Check if answersContainer has 4 children
        if (answersContainer.getChildren().size() < MAX_ANSWERS) {
            addAnswerBlock();
            updateButtons();
        }
    }

    @FXML
    private void onSaveQuestionButtonClick() {
        ///By default, quiz will be saved with only one question
        /// question textfield must not be empty, answer (child) must not be empty
        if(validateInputs()){
            /// get quiz question
            String questionText = QuestionTextArea.getText();

            /// get quiz answers
            List<Answer> answers = new ArrayList<>();
            for (Node node : answersContainer.getChildren()) {
                if (node instanceof HBox hBox) {
                    RadioButton radio = (RadioButton) hBox.getChildren().get(0);
                    TextField answerField = (TextField) hBox.getChildren().get(1);

                    String answerText = answerField.getText();
                    boolean isCorrect = radio.isSelected();

                    answers.add(new Answer(answerText, isCorrect));
                }
            }
            ///add that question to questions list
            Question q = new Question(questionText, answers);
            questionList.add(q);

            ///clean all inputs for next question
            QuestionTextArea.clear();
            answersContainer.getChildren().clear();
            for (int i = 0; i < MIN_ANSWERS; i++) {
                addAnswerBlock();
            }
            AnswserCorrectToggle.selectToggle(null);

            if (questionList.size() >= 1) {
                GoToSummaryButton.setDisable(false);
                WarningLabel.setVisible(false);
            }

            ///confirm question was added
            //TODO: change to label lol
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Pytanie dodane pomyślnie");
            alert.showAndWait();
        }
        else{
            Alert errAlert = new Alert(Alert.AlertType.ERROR);
            errAlert.setHeaderText(null);
            errAlert.setContentText("Uzupełnij tekst pytania przed zapisem");
            errAlert.showAndWait();
        }
    }


    private boolean validateInputs(){
        ///Method makes it possible to validte if textfields of questions and answers are not empty
        String questionTextArea = QuestionTextArea.getText();
        
        if (QuestionTextArea.getText().isEmpty()){
            return false;
        }
        else return true;

    }

    ///if answers limit is reached then block AddMoreButtons btn
    private void addAnswerBlock() {
        HBox answerBlock = new HBox(10);
        RadioButton answerCorrectRadio = new RadioButton();
        answerCorrectRadio.setToggleGroup(AnswserCorrectToggle);

        TextField answerField = new TextField();
        answerField.setPromptText("Wpisz odpowiedź");

        ///Button "x" that handles deleting answer
        Button deleteAnswer = new Button("x");
        deleteAnswer.setOnAction(event -> {
            answersContainer.getChildren().remove(answerBlock);
            updateButtons();
        });

        ///add children to answer block
        answerBlock.getChildren()
                .addAll(answerCorrectRadio, answerField, deleteAnswer);
        ///then add answer block with children to AnswerContainer
        answersContainer.getChildren().add(answerBlock);

        updateButtons();
    }

    private void updateButtons() {
        ///get current amount of answers
        int answersCount = answersContainer.getChildren().size();

        ///Handle diabling and enabling button that handles adding more answers to question when it reaches MAX limit
        AddMoreAnswsersButton.setDisable(answersCount >= MAX_ANSWERS);

        ///AnswersContainer(VBOX) ->  answersBlock(HBOX) -> (HBOX) answerCorrectCheck, answerTextField, deleteAnswerButton

        ///Node represents each child of AnswerContainer (basically for-each loop but smarter)
        for (Node tmpNodeChildren : answersContainer.getChildren()) {
            ///check if node is HBOX
            if (tmpNodeChildren instanceof HBox) {
                ///if yes, then cats that code to HBOX, so it can acces its children: radioButton, txtField and button
                HBox hBox = (HBox) tmpNodeChildren;

                ///get children of that hBox (second level depth)
                for (Node child : hBox.getChildren()) {
                    ///Now, identify if child is a button -> delete button inside answer block
                    if (child instanceof Button) {
                        ///enable or disable delete btn based on number of current answers to quiz
                        child.setDisable(answersCount <= MIN_ANSWERS);
                    }
                }
            }
            ///ensure one of radio btns is selected
            ///same logic as before answersContainer -> first answer -> select that as correct
            if(AnswserCorrectToggle
                    .getSelectedToggle() == null && answersCount > 0){
                AnswserCorrectToggle.selectToggle((Toggle) ((HBox) answersContainer.getChildren().get(0)).getChildren().get(0));
            }
        }
    }

    @FXML
    public void onGoToSummaryButtonClick(ActionEvent event) throws IOException {
        Stage stage = SceneManager.getStageFrom(event.getSource());

        SceneManager.switchScene(stage, "/org/example/quizez/createQuizSummary-view.fxml", "Podsumowanie quizu", loader -> {
            CreateQuizSummaryController controller = loader.getController();
            controller.initData(quizName, category, difficulty, duration, questionList);
        });
    }
}
