package com.example.deesonwoo.mysimplecalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deesonwoo.mysimplecalendar.R;

import java.util.List;

/**
 * Created by deeson.woo
 * 月份视图
 */

public class MonthCalendarView extends ViewGroup {

    private MyCalendarUtils calendarUtils;
    private int mYear, mMonth;
    private int column = 7;
    private List<MyCalendarBean> mList;
    private int pickUpPosition = -1;
    private OnDatePickUpListener onDatePickUpListener;

    public MonthCalendarView(Context context) {
        super(context);
        calendarUtils = new MyCalendarUtils(context);
    }

    public MonthCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        calendarUtils = new MyCalendarUtils(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));

        //将宽度平均分成七分，每个item的宽高都等于它
        int itemWidth = parentWidth / column;
        int itemHeight = itemWidth;

        int parentHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));

            //计算控件所需的高度
            if (i % column == 0) {
                parentHeight += childView.getMeasuredHeight();
            }
        }

        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View itemView = getChildAt(i);
            int columnCount = i % column;
            int rowCount = i / column;

            int itemWidth = itemView.getMeasuredWidth();
            int itemHeight = itemView.getMeasuredHeight();

            left = columnCount * itemWidth;
            top = rowCount * itemHeight;
            right = left + itemWidth;
            bottom = top + itemHeight;
            itemView.layout(left, top, right, bottom);
        }
    }

    public void setMonth(int year, int month) {
        this.mYear = year;
        this.mMonth = month;
        invalidateMonth();
    }

    private void invalidateMonth() {
        if (null != mList) {
            mList.clear();
        }
        pickUpPosition = -1;
        mList = calendarUtils.getDaysListOfMonth(mYear, mMonth);
        removeAllViews();
        addAllItem();
        requestLayout();
    }

    private void addAllItem() {
        for (int i = 0; i < mList.size(); i++) {
            final MyCalendarBean bean = mList.get(i);

            final View itemView = generateDateView(bean);
            itemView.setSelected(i == pickUpPosition);
            addViewInLayout(itemView, i, itemView.getLayoutParams(), true);
            final int position = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (pickUpPosition == position) {
                        return;
                    }

                    if (pickUpPosition != -1) {
                        getChildAt(pickUpPosition).setSelected(false);
                    }
                    itemView.setSelected(true);

                    if (null != onDatePickUpListener) {
                        onDatePickUpListener.onDatePickUp(bean);
                    }

                    pickUpPosition = position;
                }
            });
        }
    }

    private View generateDateView(MyCalendarBean bean) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_date_view, null);
        if (bean.isCurrentMonth()) {
            TextView date = itemView.findViewById(R.id.date);
            if (MyCalendarUtils.isToday(bean)) {
                date.setBackgroundResource(R.drawable.item_today_bg);
            } else {
                date.setBackgroundResource(R.drawable.item_pick_up);
            }
            date.setText(String.valueOf(bean.getDay()));
        }
        return itemView;
    }

    /**
     * 展示上一个月
     */
    public void moveToPreMonth() {
        mMonth -= 1;
        invalidateMonth();
    }

    /**
     * 展示下一个月
     */
    public void moveToNextMonth() {
        mMonth += 1;
        invalidateMonth();
    }

    public String getCurrentYearAndMonth() {
        return MyCalendarUtils.formatYearAndMonth(mYear, mMonth);
    }

    public void setOnDatePickUpListener(OnDatePickUpListener onDatePickUpListener) {
        this.onDatePickUpListener = onDatePickUpListener;
    }

    public interface OnDatePickUpListener {
        void onDatePickUp(MyCalendarBean bean);
    }
}
