package org.example.quizez;
/**
 * Answer model class
 */
public class Answer {
    private int id; ///Answer id from Database
    private String text; ///Answer text
    private boolean isCorrect; ///is anwser selected as correct

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    ///setters and getters
    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setText(String text) { this.text = text; }
    public void setCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }

    ///toString helper method
    @Override
    public String toString() {
        return (isCorrect ? "yes " : "no") + text;
    }
}
