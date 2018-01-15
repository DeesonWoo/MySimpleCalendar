package com.example.deesonwoo.mysimplecalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.deesonwoo.mysimplecalendar.calendar.MyCalendarBean;
import com.example.deesonwoo.mysimplecalendar.calendar.SimpleCalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        SimpleCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDatePickListener(new SimpleCalendarView.OnDatePickListener() {
            @Override
            public void onDatePick(MyCalendarBean bean) {
                Toast.makeText(MainActivity.this, bean.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
