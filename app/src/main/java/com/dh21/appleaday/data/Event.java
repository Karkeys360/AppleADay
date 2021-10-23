package com.dh21.appleaday.data;

public class Event implements Time {

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

}
