package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PC1 on 8/18/2016.
 */
public class CalendarFragment extends Fragment {

    @Bind(R.id.year)
    TextView yearTV;
    @Bind(R.id.month)
    TextView monthTV;
    @Bind(R.id.compactcalendar_view)
    CompactCalendarView calendarView;
    private String[] months = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        yearTV.setText(""+year);
        monthTV.setText(months[month]);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateClicked);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String date = month+"-"+day+"-"+year;
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                ToDoFragment toDoFragment = new ToDoFragment();
                toDoFragment.setArguments(bundle);
                MainActivity.replaceFragment(toDoFragment, "Calendar");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.e("Calendar", "Month was scrolled to: " + firstDayOfNewMonth);
                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                yearTV.setText(""+year);
                monthTV.setText(months[month]);
            }
        });
    }

    public CalendarFragment() {
        super();
    }

}
