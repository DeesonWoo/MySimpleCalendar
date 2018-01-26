package com.example.deesonwoo.mysimplecalendar.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

import com.example.deesonwoo.mysimplecalendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * MonthCalendarView2
 */
public class MonthCalendarView2 extends View {

    private MyCalendarUtils calendarUtils;

    protected Paint mPaint = new Paint();
    protected Paint mTodayBGPaint = new Paint();
    protected Paint mPickUpCirclePaint = new Paint();

    private int column = 7;

    private int currentYear, currentMonth;
    private int parentWidth, parentHeight;
    private int itemWidth, itemHeight;
    private int circleRadius;
    private int textSize;
    private List<MyCalendarBean> currentMonthDays;
    private List<Region> tempRegions = new ArrayList<>();
    private Region pickRegion = new Region();
    private float lastX, lastY;
    private float moveLimit = 25F;
    private boolean cancelClick = false;
    private OnDatePickUpListener onDatePickUpListener;

    public MonthCalendarView2(Context context) {
        super(context);
        calendarUtils = new MyCalendarUtils(context);
        textSize = MyCalendarUtils.dp2px(context, 15);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(textSize);
        mPaint.setColor(getResources().getColor(R.color.text_black));
        mTodayBGPaint.setColor(getResources().getColor(R.color.theme_color));
        mPickUpCirclePaint.setColor(getResources().getColor(R.color.theme_color));
        mPickUpCirclePaint.setStyle(Paint.Style.STROKE);
        mPickUpCirclePaint.setStrokeWidth(MyCalendarUtils.dp2px(context, 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, measureWidth * 6 / column);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        parentWidth = w;
        parentHeight = h;
        itemWidth = w / column;
        circleRadius = itemWidth * 3 / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.white));

        draw(canvas, currentYear, currentMonth);
    }

    private void draw(Canvas canvas, int year, int month) {

        tempRegions.clear();
        int weekCounts = calendarUtils.getWeekCountsOfMonth(year, month);
        itemHeight = parentHeight / weekCounts;

        currentMonthDays = calendarUtils.getDaysListOfMonth(year, month);

        for (int i = 0; i < currentMonthDays.size(); i++) {
            int columnCount = i % column;
            int rowCount = i / column;

            MyCalendarBean bean = currentMonthDays.get(i);

            Region region = new Region();
            region.set(columnCount * itemWidth, rowCount * itemHeight, (columnCount * itemWidth) + itemWidth, (rowCount * itemHeight) + itemHeight);
            tempRegions.add(region);

            if (bean.isCurrentMonth()) {
                drawTodayBG(canvas, region.getBounds(), bean);
                drawText(canvas, region.getBounds(), bean);
            }
        }
        drawPickUpCircle(canvas);
        pickRegion.setEmpty();

    }

    private void drawText(Canvas canvas, Rect rect, MyCalendarBean bean) {
        canvas.drawText(String.valueOf(bean.getDay()), rect.centerX(), rect.centerY() + (textSize / 4), mPaint);
    }

    private void drawTodayBG(Canvas canvas, Rect rect, MyCalendarBean bean) {
        if (MyCalendarUtils.isToday(bean)) {
            canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius, mTodayBGPaint);
        }
    }

    private void drawPickUpCircle(Canvas canvas) {
        if(!pickRegion.isEmpty()){
            canvas.drawCircle(pickRegion.getBounds().centerX(), pickRegion.getBounds().centerY(), circleRadius, mPickUpCirclePaint);
        }
    }

    public void setMonth(int year, int month) {
        currentYear = year;
        currentMonth = month;
        invalidate();
    }

    public String getCurrentYearAndMonth() {
        return MyCalendarUtils.formatYearAndMonth(currentYear, currentMonth);
    }

    /**
     * 展示上一个月
     */
    public void moveToPreMonth() {
        currentMonth -= 1;
        invalidate();
    }

    /**
     * 展示下一个月
     */
    public void moveToNextMonth() {
        currentMonth += 1;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelClick = false;
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((Math.abs(lastX - event.getX()) >= moveLimit) || (Math.abs(lastY - event.getY()) >= moveLimit)) {
                    cancelClick = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!cancelClick) {
                    for (int i = 0; i < tempRegions.size(); i++) {
                        Region region = tempRegions.get(i);
                        if (region.contains((int) event.getX(), (int) event.getY())) {
                            MyCalendarBean bean = currentMonthDays.get(i);
                            if (bean.isCurrentMonth()) {
                                pickRegion.set(region);
                                invalidate();
                                if (null != onDatePickUpListener) {
                                    onDatePickUpListener.onDatePickUp2(bean);
                                }
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void setOnDatePickUpListener(OnDatePickUpListener onDatePickUpListener) {
        this.onDatePickUpListener = onDatePickUpListener;
    }

    public interface OnDatePickUpListener {
        void onDatePickUp2(MyCalendarBean bean);
    }
}
