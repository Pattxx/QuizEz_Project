package org.example.quizez;

public class Quiz {
    private int id;
    private String name;
    private String category;
    private String difficulty;
    private int duration;

    public Quiz(int id, String name, String category, String difficulty, int duration) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
