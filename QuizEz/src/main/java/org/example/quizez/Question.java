package org.example.quizez;

import java.util.List;

public class Question {
    private String questionText;
    private int id;
    private List<Answer> answers;

    public Question(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public Question(int id, String questionText, List<Answer> answers) {
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
    }
    public int getId() { return id; }
    public String getQuestionText() {
        return questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return questionText;
    }
    public void setText(String questionText) { this.questionText = questionText; }
    ///helps with question editing
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
}
