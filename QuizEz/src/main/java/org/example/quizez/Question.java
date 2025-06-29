package org.example.quizez;

import java.util.List;
/**
 * Question model class
 */
public class Question {
    private String questionText;
    private int id;

    ///every question has its own answers list
    private List<Answer> answers;

    ///constructors
    public Question(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public Question(int id, String questionText, List<Answer> answers) {
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
    }

    /// getters and setters
    public int getId() { return id; }
    public String getQuestionText() {
        return questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    public void setText(String questionText) { this.questionText = questionText; }
    ///helps with question editing
    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    ///methods
    @Override
    public String toString() {
        return questionText;
    }

}
