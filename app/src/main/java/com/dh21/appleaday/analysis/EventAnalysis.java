package com.dh21.appleaday.analysis;

import com.dh21.appleaday.data.Event;
import com.dh21.appleaday.data.Food;
import com.dh21.appleaday.data.Timed;
import com.dh21.appleaday.data.Util;

import java.util.ArrayList;
import java.util.List;

public class EventAnalysis {
    public static int INTERVAL_LENGTH_DAYS = 3;
    public static int HOURS_PER_DAY = 24;
    public static int SECONDS_PER_HOUR = 3600;
    public static int MILLIS_PER_SECOND = 1000;

    private static boolean DEBUG = true;

    private List<Timed> times;

    public EventAnalysis(List<Timed> times) {
        this.times = times;
    }

    public double getEventProbability(String eventName) {
        return 1.0 * getNumEventsWithName(eventName) / getTotalEvents();
    }

    public double getFoodProbability(String foodName) {
        return 1.0 * getNumFoodsWithName(foodName) / getTotalFoods();
    }

    public int getNumEventsWithName(String eventName) {
        int numEvents = 0;
        for (Timed time : this.times) {
            try {
                Event event = (Event) time;
                if (event.getName().equals(eventName)) {
                    numEvents++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numEvents;
    }

    public int getNumFoodsWithName(String foodName) {
        int numFoods = 0;
        for (Timed time : this.times) {
            try {
                Food food = (Food) time;
                if (food.getName().equals(foodName)) {
                    numFoods++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numFoods;
    }

    private int getNumFoodsWithName(List<Timed> times, String foodName) {
        int numFoods = 0;
        for (Timed time : times) {
            try {
                Food food = (Food) time;
                if (food.getName().equals(foodName)) {
                    numFoods++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numFoods;
    }

    public int getTotalEvents() {
        int numEvents = 0;
        for (Timed time : this.times) {
            try {
                Event event = (Event) time;
                numEvents++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numEvents;
    }

    public int getTotalFoods() {
        int numFoods = 0;
        for (Timed time : this.times) {
            try {
                Food food = (Food) time;
                numFoods++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numFoods;
    }

    public double getFoodGivenEventProbability(String foodName, String eventName) {
        return 1.0 * getNumIntervalsWithFoodAndEvent(foodName, eventName) / getNumEventsWithName(eventName);
    }

    public int getNumIntervalsWithFoodAndEvent(String foodName, String eventName) {
        // number of intervals in which food with `foodName` was eaten and event with `eventName` happened
        int cnt = 0;

        int intervalLengthMillis = INTERVAL_LENGTH_DAYS * HOURS_PER_DAY * SECONDS_PER_HOUR * MILLIS_PER_SECOND;
        for (Timed time : times) {
            try {
                Event event = (Event) time;
                List<Timed> interval = Util.getInterval(times, event.getTime() - intervalLengthMillis, event.getTime());

                // check if food was eaten recently before the event
                if (event.getName().equals(eventName) && getNumFoodsWithName(interval, foodName) > 0) {
                    cnt++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cnt;
    }

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

            EventAnalysis ea = new EventAnalysis(times);
            System.out.println(ea.getEventProbability("vomit"));
            System.out.println(ea.getFoodProbability("bread"));

            System.out.println(ea.getFoodGivenEventProbability("pizza", "gas"));
        }
    }
}
