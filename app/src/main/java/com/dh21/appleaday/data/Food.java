package com.dh21.appleaday.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Food implements Timed {

    private String name;
    private long time;
    private double grade;
    private double calories;
    private double fats;
    private double carbs;
    private double proteins;
    private Set<String> ingredients;

    public Food(String name) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.grade = -1;
        this.calories = -1;
        this.fats = -1;
        this.carbs = -1;
        this.proteins = -1;
        this.ingredients = new HashSet<>();
    }

    public Food(String name, double grade, double calories, double fats,
                double carbs, double proteins, Set<String> ingredients) {
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

    public double getGrade() {
        return this.grade;
    }

    public double getCalories() {
        return this.calories;
    }

    public double getFat() {
        return this.fats;
    }

    public double getCarbs() {
        return this.carbs;
    }

    public double getProteins() {
        return this.proteins;
    }

    public Set<String> getIngredients() {
        return this.ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public Map<String, Double> getMap() {
        Map<String, Double> map = new HashMap<>();
        if (this.grade != -1) {
            map.put("grade", this.grade);
        }
        if (this.calories != -1) {
            map.put("calories", this.calories);
        }
        if (this.fats != -1) {
            map.put("fats", this.fats);
        }
        if (this.carbs != -1) {
            map.put("carbs", this.carbs);
        }
        if (this.proteins != -1) {
            map.put("proteins", this.proteins);
        }
        return map;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
