package com.dh21.appleaday.data;

import java.util.Calendar;
import java.util.List;

public class IntervalIterator {

    public enum Interval {
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

    public Interval getInterval() {
        return interval;
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
        int year = endDate.get(Calendar.YEAR);

        switch (interval) {
            case Day:
                int day = endDate.get(Calendar.DAY_OF_YEAR);
                while (startDate.get(Calendar.DAY_OF_YEAR) == day &&
                        startDate.get(Calendar.YEAR) == year && startIndex != 0) {
                    startIndex -= 1;
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                }
                if (startIndex == 0 && startDate.get(Calendar.DAY_OF_YEAR) == day &&
                    startDate.get(Calendar.YEAR) == year) {
                    startIndex -= 1;
                }
                break;

            case Week:
                int week = endDate.get(Calendar.WEEK_OF_YEAR);
                while (startDate.get(Calendar.WEEK_OF_YEAR) == week &&
                        startDate.get(Calendar.YEAR) == year && startIndex != 0) {
                    startIndex -= 1;
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                }
                if (startIndex == 0 && startDate.get(Calendar.WEEK_OF_YEAR) == week &&
                    startDate.get(Calendar.YEAR) == year) {
                    startIndex -= 1;
                }
                break;

            case Month:
                int month = endDate.get(Calendar.MONTH);
                while (startDate.get(Calendar.MONTH) == month &&
                        startDate.get(Calendar.YEAR) == year && startIndex != 0) {
                    startIndex -= 1;
                    startMilli = times.get(startIndex).getTime();
                    startDate.setTimeInMillis(startMilli);
                }
                if (startIndex == 0 && startDate.get(Calendar.MONTH) == month &&
                    startDate.get(Calendar.YEAR) == year) {
                    startIndex -= 1;
                }
                break;
        }
        this.index = startIndex;
        return times.subList(startIndex + 1, endIndex + 1);
    }
}
