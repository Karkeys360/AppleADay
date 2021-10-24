package com.dh21.appleaday.data;

import java.util.Calendar;
import java.util.List;

public class IntervalIterator {

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
        if (this.index == -1) {
            return null;
        }

        int endIndex = this.index;
        int startIndex = this.index;

        long endMilli = times.get(endIndex).getTime();
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(endMilli);

        long startMilli = endMilli;
        Calendar startDate = (Calendar) endDate.clone();

        switch (interval) {
            case Day:
                int day = endDate.get(Calendar.DAY_OF_YEAR);
                while (startDate.get(Calendar.DAY_OF_YEAR) == day && startIndex != -1) {
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                    startIndex -= 1;
                }
                break;

            case Week:
                int week = endDate.get(Calendar.WEEK_OF_MONTH);
                while (startDate.get(Calendar.WEEK_OF_MONTH) == week && startIndex != -1) {
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                    startIndex -= 1;
                }
                break;

            case Month:
                int month = endDate.get(Calendar.MONTH);
                while (startDate.get(Calendar.MONTH) == month && startIndex != -1) {
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                    startIndex -= 1;
                }
                break;
        }
        this.index = startIndex;
        return times.subList(startIndex + 1, endIndex + 1);
    }
}
