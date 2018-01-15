package com.example.deesonwoo.mysimplecalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.deesonwoo.mysimplecalendar.R;


public class SimpleCalendarView extends LinearLayout implements View.OnClickListener, MonthCalendarView.OnDatePickUpListener {

    private MonthCalendarView monthCalendarView;// 月历
    private OnDatePickListener onDatePickListener;

    private TextView title;

    public SimpleCalendarView(Context context) {
        this(context, null);
    }

    public SimpleCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        setBackgroundColor(context.getResources().getColor(R.color.white));

        // 年月标题、翻页按钮
        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, MyCalendarUtils.dp2px(context, 50));
        RelativeLayout titleLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.title_layout, null);
        title = titleLayout.findViewById(R.id.title);
        ImageView preMonth = titleLayout.findViewById(R.id.pre_month);
        ImageView nextMonth = titleLayout.findViewById(R.id.next_month);
        preMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        addView(titleLayout, titleParams);

        //星期布局
        LayoutParams weekParams = new LayoutParams(LayoutParams.MATCH_PARENT, MyCalendarUtils.dp2px(context, 40));
        LinearLayout weekLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.week_layout, null);
        addView(weekLayout, weekParams);

        //月历视图
        LayoutParams monthParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        monthCalendarView = new MonthCalendarView(context);
        initCalendarDate();
        monthCalendarView.setOnDatePickUpListener(this);
        addView(monthCalendarView, monthParams);
    }

    private void initCalendarDate() {
        int[] nowDay = MyCalendarUtils.getNowDayFromSystem();
        monthCalendarView.setMonth(nowDay[0], nowDay[1]);
        updateTitle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_month:
                if (null != monthCalendarView) {
                    monthCalendarView.moveToPreMonth();
                }
                updateTitle();
                break;
            case R.id.next_month:
                if (null != monthCalendarView) {
                    monthCalendarView.moveToNextMonth();
                }
                updateTitle();
                break;
        }
    }

    private void updateTitle() {
        if (null != title && null != monthCalendarView) {
            title.setText(monthCalendarView.getCurrentYearAndMonth());
        }
    }

    @Override
    public void onDatePickUp(MyCalendarBean bean) {
        if (null != onDatePickListener) {
            onDatePickListener.onDatePick(bean);
        }
    }

    public void setOnDatePickListener(OnDatePickListener onDatePickListener) {
        this.onDatePickListener = onDatePickListener;
    }

    public interface OnDatePickListener {
        void onDatePick(MyCalendarBean bean);
    }
}
