package com.dh21.appleaday.data;

import java.util.Set;
import java.util.HashSet;

public class Food implements Timed {

    private String name;
    private long time;
    private String grade;
    private int calories;
    private int fats;
    private int carbs;
    private int proteins;
    private Set<String> ingredients;

    public Food(String name) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.grade = "Z";
        this.calories = -1;
        this.fats = -1;
        this.carbs = -1;
        this.proteins = -1;
        this.ingredients = new HashSet<>();
    }

    public Food(String name, String grade, int calories, int fats,
                int carbs, int proteins, Set<String> ingredients) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.grade = grade;
        this.calories = calories;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.ingredients = ingredients;
    }

    public long getTime() {
        return this.time;
    }

    public String getGrade() {
        return this.grade;
    }

    public int getCalories() {
        return this.calories;
    }

    public int getFat() {
        return this.fats;
    }

    public int getCarbs() {
        return this.carbs;
    }

    public int getProteins() {
        return this.proteins;
    }

    public Set<String> getIngredients() {
        return this.ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getFats() {
        return fats;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public void setTime(long time) {
        this.time = time;
    }
}
