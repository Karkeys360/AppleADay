package com.dh21.appleaday.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private double sugars;
    private double fiber;
    private double sodium;
    private Set<String> ingredients;

    public Food(String name) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.grade = -1;
        this.calories = -1;
        this.fats = -1;
        this.carbs = -1;
        this.proteins = -1;
        this.fiber = -1;
        this.sugars = -1;
        this.sodium = -1;
        this.ingredients = new HashSet<>();
    }

    public Food(String name, double grade, double calories, double fats,
                double carbs, double proteins, double sugars, double fiber, double sodium, Set<String> ingredients) {
        this.name = name ;
        this.time = System.currentTimeMillis();
        this.grade = grade;
        this.calories = calories;
        this.fats = fats;
        this.carbs = carbs;
        this.proteins = proteins;
        this.sugars = sugars;
        this.fiber = fiber;
        this.sodium = sodium;
        this.ingredients = ingredients;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public long getTime() {
        return this.time;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
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

    public String getName() {
        return name;
    }

    public double getFats() {
        return fats;
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
        if (this.sodium != -1) {
            map.put("sodium", this.sodium);
        }
        if (this.fiber != -1) {
            map.put("fiber", this.fiber);
        }
        if (this.sugars != -1) {
            map.put("sugars", this.sugars);
        }
        return map;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return time == food.time &&
                Double.compare(food.grade, grade) == 0 &&
                Double.compare(food.calories, calories) == 0 &&
                Double.compare(food.fats, fats) == 0 &&
                Double.compare(food.carbs, carbs) == 0 &&
                Double.compare(food.proteins, proteins) == 0 &&
                Double.compare(food.sugars, sugars) == 0 &&
                Double.compare(food.fiber, fiber) == 0 &&
                Double.compare(food.sodium, sodium) == 0 &&
                name.equals(food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time, grade, calories, fats, carbs, proteins, sugars, fiber, sodium);
    }
}
