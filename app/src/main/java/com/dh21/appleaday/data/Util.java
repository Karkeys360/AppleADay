package com.dh21.appleaday.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Util {

    public static List<Timed> getInterval(List<Timed> times, long start, long end) {
        int startIndex = Collections.binarySearch(times, new TimedDummy(start), new Comparator<Timed>() {
            @Override
            public int compare(Timed t1, Timed t2) {
                if (t1.getTime() < t2.getTime()) {
                    return -1;
                } else if (t1.getTime() > t2.getTime()) {
                    return 1;
                }
                return 0;
            }
        });

        int endIndex = Collections.binarySearch(times, new TimedDummy(end), new Comparator<Timed>() {
            @Override
            public int compare(Timed t1, Timed t2) {
                if (t1.getTime() < t2.getTime()) {
                    return -1;
                } else if (t1.getTime() > t2.getTime()) {
                    return 1;
                }
                return 0;
            }
        });

        if (startIndex < 0) {
            startIndex = -(startIndex + 1);
        }
        if (endIndex < 0) {
            endIndex = -(endIndex + 1) - 1;
        }
        return times.subList(startIndex, endIndex + 1);
    }
}