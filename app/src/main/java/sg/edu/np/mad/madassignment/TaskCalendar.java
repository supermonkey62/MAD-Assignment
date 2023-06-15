package sg.edu.np.mad.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskCalendar extends AppCompatActivity {

    CalendarView calendarView;
    Calendar calendar;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        // Retrieve the list from the intent
        Intent intent = getIntent();
        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        setDateToToday();
        getDate();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                Toast.makeText(TaskCalendar.this, day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDate() {
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(TaskCalendar.this, selected_date, Toast.LENGTH_SHORT).show();
    }

    public void setDateToToday() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
    }
}
