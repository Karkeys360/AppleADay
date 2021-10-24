package com.dh21.appleaday.data;

import java.util.Calendar;
import java.util.List;

public class IntervalIterator {

    private static final long MILLIS_IN_A_DAY = 86400000;
    private static final long MILLIS_IN_A_WEEK = 604800000;

    public static enum Interval {
        Day, Week, Month
    }

    private List<Timed> times;
    private int index;
    private Interval interval;

    public IntervalIterator(List<Timed> times, Interval interval) {
        this.times = times;
        this.index = times.size() - 1;
        this.interval = interval;
    }

    public List<Timed> next() {
        int endIndex = this.index;
        int startIndex = this.index;

        long endMilli = times.get(endIndex).getTime() / MILLIS_IN_A_DAY;
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(endMilli);

        long startMilli = times.get(startIndex).getTime() / MILLIS_IN_A_DAY;
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(startMilli);

        switch (interval) {
            case Day:
                while (startMilli >= endMilli - MILLIS_IN_A_DAY &&
                       startIndex != -1) {
                    startIndex -= 1;
                    startMilli = times.get(startIndex).getTime() / MILLIS_IN_A_DAY;
                }
                break;

            case Week:
                while (startMilli >= endMilli - MILLIS_IN_A_WEEK &&
                        startIndex != -1) {
                    startIndex -= 1;
                    startMilli = times.get(startIndex).getTime() / MILLIS_IN_A_DAY;
                }
                break;

            case Month:
                int month = endDate.get(Calendar.MONTH);
                if (month == 0) {
                    while (startDate.get(Calendar.MONTH) == 0 ||
                           startDate.get(Calendar.MONTH) == 12 ||
                           startIndex == -1) {
                        startIndex -= 1;
                        startMilli = times.get(startIndex).getTime() / MILLIS_IN_A_DAY;
                        startDate.setTimeInMillis(startMilli);
                    }
                } else {
                    while (startDate.get(Calendar.MONTH) >= month + 1 || startIndex == -1) {
                        startIndex -= 1;
                        startMilli = times.get(startIndex).getTime() / MILLIS_IN_A_DAY;
                        startDate.setTimeInMillis(startMilli);
                    }
                }
                break;
        }
        return times.subList(startIndex + 1, endIndex + 1);
    }

}
