package org.example.quizez;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
public class CreateQuizController2 {

    @FXML
    private TextArea QuestionTextArea;
    @FXML
    private VBox answersContainer;
    @FXML
    private Button AddMoreAnswsersButton;
    @FXML
    private Button SaveQuizButton;

    ///ToggleGroup used to determine which one of the answers is correct
    private ToggleGroup AnswserCorrectToggle = new ToggleGroup();

    ///MIN and MAX values used to generate answer containers
    private final int MAX_ANSWERS = 4;
    private final int MIN_ANSWERS = 2;

    ///On load add 2 answer containers
    public void initialize() {
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
    private void onSaveQuizButtonClick() {
        //GrantAccess();
    }

    ///method handles adding new answer blocks dynamically based on current answer block count
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
            if(AnswserCorrectToggle
                    .getSelectedToggle() == null && answersCount > 0){
                AnswserCorrectToggle.selectToggle((Toggle) ((HBox) answersContainer.getChildren().get(0)).getChildren().get(0));
            }
        }



    }
}
