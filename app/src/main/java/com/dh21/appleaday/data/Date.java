package com.dh21.appleaday.data;

public class Date {

    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public String getFullDate() {
        String date = "";
        if (month < 10) {
            date += "0";
        }
        date += String.valueOf(month);
        if (day < 10) {
            date += "0";
        }
        date += String.valueOf(day);
        date += String.valueOf(year);
        return date;
    }
}
