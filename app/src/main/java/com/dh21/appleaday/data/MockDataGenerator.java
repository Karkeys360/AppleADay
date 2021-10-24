package com.dh21.appleaday.data;

import com.dh21.appleaday.analysis.EventAnalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MockDataGenerator {

    public static final int DAYS_IN_MONTH = 30;
    public static final int MEALS_IN_DAY = 4;
    public static final long MILLIS_IN_DAY = 86400000;
    public static final long MILLIS_BETWEEN_MEALS = 9000000;  // 2.5 hours
    public static final long MILLIS_TILL_EVENT =7200000;  // 2 hours
    public static final long MILLIS_OF_FIRST_MEAL = 1577901600000L;  // 1/1/2020 11 AM PST

    public static void main(String[] args) {
    }

    public static void generate () {

        // diarrhea - burrito
        // flatulence - milk
        // bloating - hashbrown
        // constipation - burger

        Random rand = new Random();
        List<Timed> times = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_MONTH; i++) {
            for (int j = 0; j < MEALS_IN_DAY; j++) {
                long time = MILLIS_OF_FIRST_MEAL + i * MILLIS_IN_DAY + j * MILLIS_BETWEEN_MEALS;
                int foodAmount = rand.nextInt(3);
                boolean hasDiarrhea = false;
                boolean hasFlatulence = false;
                boolean hasBloating = false;
                boolean hasConstipation = false;
                for (int k = 0; k < foodAmount; k++) {
                    int foodItem = rand.nextInt(5);

                    // Breakfast
                    if (j == 0) {
                        Food food;
                        if (foodItem == 0) {
                            food = new Food("banana", 0, 110, 0, 28, 1, 15, 3, 1, new HashSet<>());
                        } else if (foodItem == 1) {
                            food = new Food("cereal", 1, 105, 1.9, 21, 3.4, 1.2, 2.6, 139, new HashSet<>());
                        } else if (foodItem == 2) {
                            hasFlatulence = true;
                            food = new Food("milk", 0, 149, 8, 12, 8, 12, 0, 107, new HashSet<>());
                        } else if (foodItem == 3) {
                            hasBloating = true;
                            food = new Food("hashbrown", 4, 144, 9, 15, 1.4, 0.3, 1.4, 300, new HashSet<>());
                        } else {
                            food = new Food("egg", 0, 70, 5, 0, 6, 0, 0, 70, new HashSet<>());
                        }
                        food.setTime(time);
                        times.add(food);
                    }

                    // Lunch
                    else if (j == 1) {
                        Food food;
                        if (foodItem == 0) {
                            food = new Food("burger", 3, 480, 27, 45, 16, 6, 3, 640, new HashSet<>());
                        } else if (foodItem == 1) {
                            food = new Food("sandwich", 2, 361, 16.7, 32.5, 19.3, 5.13, 2.3, 1320, new HashSet<>());
                        } else if (foodItem == 2) {
                            food = new Food("hotdog", 3, 314, 18.6, 24.3, 11.4, 4, 0.8, 810, new HashSet<>());
                        } else if (foodItem == 3) {
                            food = new Food("ribs", 2, 762, 57, 23, 39, 18, 0.5, 737, new HashSet<>());
                        } else {
                            food = new Food("philly", 2, 860, 46, 63, 49, 5.9, 4.7, 1072, new HashSet<>());
                        }
                        food.setTime(time);
                        times.add(food);
                    }

                    // Dinner
                    else if (j == 2) {
                        Food food;
                        if (foodItem == 0) {
                            food = new Food("steak", 2, 679, 48, 0, 62, 0, 0, 146, new HashSet<>());
                        } else if (foodItem == 1) {
                            food = new Food("burrito", 1, 970, 25, 134, 56, 9, 18, 2340, new HashSet<>());
                        } else if (foodItem == 2) {
                            food = new Food("salmon", 1, 412, 27, 0, 40, 0, 0, 117, new HashSet<>());
                        } else if (foodItem == 3) {
                            food = new Food("nachos", 4, 346, 19, 36, 9, 4.5, 9.9, 816, new HashSet<>());
                        } else {
                            food = new Food("pizza", 4, 700, 28, 70, 44, 7, 3, 1370, new HashSet<>());
                        }
                        food.setTime(time);
                        times.add(food);
                    }

                    else {
                        Food food;
                        if (foodItem == 0) {
                            food = new Food("proteinbar", 0, 190, 6, 22, 21, 2, 10, 140, new HashSet<>());
                        } else if (foodItem == 1) {
                            food = new Food("yogurt", 0, 150, 2, 27, 6, 19, 0, 90, new HashSet<>());
                        } else if (foodItem == 2) {
                            food = new Food("twix", 4, 291, 14, 38, 2.8, 28, 0.6, 115, new HashSet<>());
                        } else if (foodItem == 3) {
                            food = new Food("watermelon", 0, 85, 0.4, 21, 1.7, 17, 1.1, 3, new HashSet<>());
                        } else {
                            food = new Food("pretzels", 2, 108, 0.7, 23, 2.9, 0.8, 0.9, 359, new HashSet<>());
                        }
                        food.setTime(time);
                        times.add(food);
                    }
                }
                // Add events 2 hours after meal if necessary
                if (hasDiarrhea) {
                    Event event = new Event("diarrhea");
                    event.setSeverity(5);
                    event.setTime(time + MILLIS_TILL_EVENT);
                }
                if (hasFlatulence) {
                    Event event = new Event("flatulence");
                    event.setSeverity(5);
                    event.setTime(time + MILLIS_TILL_EVENT);
                }
                if (hasBloating) {
                    Event event = new Event("bloating");
                    event.setSeverity(5);
                    event.setTime(time + MILLIS_TILL_EVENT);
                }
                if (hasConstipation) {
                    Event event = new Event("constipation");
                    event.setSeverity(5);
                    event.setTime(time + MILLIS_TILL_EVENT);
                }
            }
        }
        EventAnalysis ea = EventAnalysis.getInstance();
        ea.addAll(times);
    }
}
