package com.example.deesonwoo.mysimplecalendar.calendar;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by deeson.woo
 */

public class MyCalendarUtils {

    private Context mContext;

    public MyCalendarUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 获取具体月份的最大天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfCertainMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取当前月份的日期列表
     *
     * @param year
     * @param month
     * @return
     */
    public List<MyCalendarBean> getDaysListOfMonth(int year, int month) {

        List<MyCalendarBean> list = new ArrayList<>();

        int daysOfMonth = getDaysOfCertainMonth(year, month);

        //找到当前月第一天的星期，计算出前面空缺的上个月的日期个数，填充到当月日期列表中
        int weekDayOfFirstDay = getWeekDayOnCertainDate(year, month, 1);
        int preMonthDays = weekDayOfFirstDay - 1;

        for (int i = preMonthDays; i > 0; i--) {
            MyCalendarBean preMonthBean = generateCalendarBean(year, month, 1 - i);
            preMonthBean.setCurrentMonth(false);
            list.add(preMonthBean);
        }

        for (int i = 0; i < daysOfMonth; i++) {
            MyCalendarBean monthBean = generateCalendarBean(year, month, i + 1);
            monthBean.setCurrentMonth(true);
            list.add(monthBean);
        }
        return list;
    }

    /**
     * 构建具体一天的对象
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public MyCalendarBean generateCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        return new MyCalendarBean(year, month, day);
    }

    /**
     * 获取具体一天对应的星期
     *
     * @param year
     * @param month
     * @param day
     * @return 1-7(周日-周六)
     */
    private int getWeekDayOnCertainDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前月份的周数
     * @param year
     * @param month
     * @return
     */
    public int getWeekCountsOfMonth(int year, int month) {
        int lastDayOfMonth = getDaysOfCertainMonth(year, month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, lastDayOfMonth);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 格式化标题展示
     *
     * @param year
     * @param month
     * @return
     */
    public static String formatYearAndMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        return year + "年" + month + "月";
    }

    /**
     * 获取系统当前年月日
     *
     * @return
     */
    public static int[] getNowDayFromSystem() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }

    /**
     * 判断是否为系统当天
     * @param bean
     * @return
     */
    public static boolean isToday(MyCalendarBean bean) {
        int[] nowDay = getNowDayFromSystem();
        return bean.getYear() == nowDay[0] && bean.getMonth() == nowDay[1] && bean.getDay() == nowDay[2];
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
