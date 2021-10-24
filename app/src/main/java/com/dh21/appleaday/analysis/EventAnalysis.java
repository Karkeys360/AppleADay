package com.dh21.appleaday.analysis;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dh21.appleaday.data.DataUtil;
import com.dh21.appleaday.data.Event;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.Timed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Singleton class
public class EventAnalysis {
    public static int INTERVAL_LENGTH_DAYS = 3;
    public static int HOURS_PER_DAY = 24;
    public static int SECONDS_PER_HOUR = 3600;
    public static int MILLIS_PER_SECOND = 1000;

    private static boolean DEBUG = true;

    private static EventAnalysis instance = new EventAnalysis();
    private List<Timed> times;

    private int numEvents; // total num of events
    private int numFoods; // total num of foods
    private Map<String, Integer> eventFreq; // frequency of events by name
    private Map<String, Integer> foodFreq; // frequency of foods by name
    private Map<String, Map<String, Integer>> foodEventsCaused; // frequency of events caused by food by food name

    private EventAnalysis() {
        this.numEvents = 0;
        this.numFoods = 0;
        this.times = new ArrayList<>();
        this.eventFreq = new HashMap<>();
        this.foodFreq = new HashMap<>();
        this.foodEventsCaused = new HashMap<>();
    }

    public static EventAnalysis getInstance() {
        return instance;
    }

    public Map<String, Integer> getFoodFreq() {
        return Collections.unmodifiableMap(foodFreq);
    }

    public Map<String, Integer> getEventFreq() {
        return Collections.unmodifiableMap(eventFreq);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addTime(Timed time) {
        this.times.add(time);
        if (time instanceof Event) {
            this.numEvents++;
            Event event = (Event) time;
            String name = event.getName();
            this.eventFreq.put(name, eventFreq.getOrDefault(name, 0) + 1);

            // update foodEventsCaused
            int intervalLengthMillis = INTERVAL_LENGTH_DAYS * HOURS_PER_DAY * SECONDS_PER_HOUR * MILLIS_PER_SECOND;
            List<Timed> intervalTimes = DataUtil.getInterval(this.times, event.getTime() - intervalLengthMillis, event.getTime());

            for (Timed intervalTime : intervalTimes) {
                if (intervalTime instanceof Food) {
                    Food food = (Food) intervalTime;

                    Map<String, Integer> foodEvents = foodEventsCaused.getOrDefault(food.getName(), new HashMap<String, Integer>());
                    foodEvents.put(event.getName(), foodEvents.getOrDefault(event.getName(), 0) + 1);
                    foodEventsCaused.put(food.getName(), foodEvents);
                }
            }
        }
        if (time instanceof Food) {
            this.numFoods++;
            Food food = (Food) time;
            String name = food.getName();
            foodFreq.put(name, foodFreq.getOrDefault(name, 0) + 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addTimes(List<Timed> times) {
        for (Timed time : times) {
            this.addTime(time);
        }
    }

    public List<Timed> getTimes() {
        return this.times;
    }

    public double getEventProbability(String eventName) {
        return (double) getNumEventsWithName(eventName) / getTotalEvents();
    }

    public double getFoodProbability(String foodName) {
        return (double) getNumFoodsWithName(foodName) / getTotalFoods();
    }

    public int getNumEventsWithName(String eventName) {
        return this.eventFreq.get(eventName);
    }

    private int getNumEventsWithName(List<Timed> times, String eventName) {
        int numEvents = 0;
        for (Timed time : times) {
            if (time instanceof Event) {
                Event event = (Event) time;
                if (event.getName().equals(eventName)) {
                    numEvents++;
                }
            }
        }
        return numEvents;
    }

    public int getNumFoodsWithName(String foodName) {
        return this.foodFreq.get(foodName);
    }

    private int getNumFoodsWithName(List<Timed> times, String foodName) {
        int numFoods = 0;
        for (Timed time : times) {
            if (time instanceof Food) {
                Food food = (Food) time;
                if (food.getName().equals(foodName)) {
                    numFoods++;
                }
            }
        }
        return numFoods;
    }

    public int getTotalEvents() {
        return this.numEvents;
    }

    public int getTotalFoods() {
        return this.numFoods;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double getFoodGivenEventProbability(String foodName, String eventName) {
        return (double) getNumEventsCausedByFood(foodName, eventName) / getNumEventsWithName(eventName);
    }

    // Computes sensitivity
    @RequiresApi(api = Build.VERSION_CODES.N)
    public double getEventGivenFoodProbability(String eventName, String foodName) {
        return (double) getNumEventsCausedByFood(foodName, eventName) / getNumFoodsWithName(foodName);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getNumEventsCausedByFood(String foodName, String eventName) {
        return this.foodEventsCaused.getOrDefault(foodName, new HashMap<String, Integer>()).getOrDefault(eventName, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        if (DEBUG) {
            // testing
            Food f = new Food("pizza");
            Food f2 = new Food("bread");
            Food f3 = new Food("bread");
            Event e = new Event("gas");
            Event e2 = new Event("vomit");

            List<Timed> times = new ArrayList<>();
            times.add(f);
            times.add(f2);
            times.add(f3);
            times.add(e);
            times.add(e2);

            EventAnalysis ea = EventAnalysis.getInstance();
            ea.addTimes(times);
            System.out.println(ea.getEventProbability("vomit"));
            System.out.println(ea.getFoodProbability("bread"));

            System.out.println(ea.getFoodGivenEventProbability("pizza", "gas"));
            System.out.println(ea.getEventGivenFoodProbability("gas", "pizza"));
        }
    }
}
