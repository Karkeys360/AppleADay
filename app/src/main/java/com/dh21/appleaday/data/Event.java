package com.dh21.appleaday.data;

import java.util.Objects;

public class Event implements Timed {

    private String name;
    private long time;
    private int severity;

    public Event(String name) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.severity = -1;
    }

    public Event(String name, int severity) {
        this.name = name;
        this.time = System.currentTimeMillis();
        this.severity = severity;
    }

    public String getName() {
        return this.name;
    }

    public long getTime() {
        return this.time;
    }

    public int getSeverity() {
        return this.severity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return time == event.time &&
                name.equals(event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time);
    }
}
