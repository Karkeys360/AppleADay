package com.dh21.appleaday.data;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class Day {

    private Calendar date;
    private List<Food> foods;
    private List<Event> events;

    public Day(Calendar date) {
        this.date = date;
        this.foods = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public void addFood(Food food) {
        this.foods.add(food);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

}
