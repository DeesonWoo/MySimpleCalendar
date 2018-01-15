package com.example.deesonwoo.mysimplecalendar.calendar;

/**
 * Created by deeson.woo
 */
public class MyCalendarBean {

    private int year;
    private int month;//1-12
    private int day;//1-31
    private boolean isCurrentMonth = true;//是否为当前月份的日期

    public MyCalendarBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    @Override
    public String toString() {
        return year + "/" + month + "/" + day;
    }
}
