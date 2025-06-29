package org.example.quizez;

public class Answer {
    private int id;
    private String text;
    private boolean isCorrect;

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }
    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setText(String text) { this.text = text; }
    public void setCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }
    @Override
    public String toString() {
        return (isCorrect ? "yes " : "no") + text;
    }
}
