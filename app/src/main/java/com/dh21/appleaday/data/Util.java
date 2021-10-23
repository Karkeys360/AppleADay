package com.dh21.appleaday.data;

import java.util.List;

public class Util {

    public static List<Long> getInterval(List<Long> times, long start, long end) {
        // Binary search to find index of start time
        int lower = 0;
        int upper = times.size() - 1;
        int startIndex = 0;
        while (lower <= upper) {
            startIndex = lower + (upper - lower) / 2;
            if (start == times.get(startIndex)) {
                break;
            } else if (start < times.get(startIndex)) {
                upper = startIndex - 1;
            } else {
                lower = startIndex + 1;
            }
        }

        // Binary search to find index of end time
        lower = 0;
        upper = times.size() - 1;
        int endIndex = 0;
        while (lower <= upper) {
            endIndex = lower + (upper - lower) / 2;
            if (end == times.get(endIndex)) {
                break;
            } else if (end < times.get(endIndex)) {
                upper = endIndex - 1;
            } else {
                lower = endIndex + 1;
            }
        }
        return times.subList(startIndex, endIndex + 1);
    }
}