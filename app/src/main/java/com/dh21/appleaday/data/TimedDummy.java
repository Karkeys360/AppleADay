package com.dh21.appleaday.data;

public class TimedDummy implements Timed {

    private long time;

    public TimedDummy(long time) {
        this.time = time;
    }
    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }
}
