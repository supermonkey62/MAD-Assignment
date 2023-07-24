package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskCalendar extends AppCompatActivity implements TaskDataHolder.TaskDataCallback, SelectListener {

    CalendarView calendarView;
    Calendar calendar;
    TextView totaltasks;
    String selectedDateString, selectedDateString2;
    String TITLE = "TaskCalendar";
    String username;
    FloatingActionButton addTasks;
    RecyclerView taskshower;

    ImageView gobackButton;

    Calendar selectedDateCalendar;
    private List<Task> taskList;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DATE_SAVED = "10/06/2023";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TITLE, "OnCreate");
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        taskshower = findViewById(R.id.taskshower);
        addTasks = findViewById(R.id.addtask);
        setDateToToday();
        totaltasks = findViewById(R.id.totaltasks);
        gobackButton = findViewById(R.id.gobackButton);
        username = getIntent().getStringExtra("USERNAME");

        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                taskList = tasks;

                // Filter tasks based on the initially selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTimeInMillis(calendarView.getDate());

                Log.v("String3Create", selectedDateString2);

                List<Task> filteredTasks = filterTasksByDate(selectedDateString2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());
                selectedDateCalendar = selectedDate;


                // Set RecyclerView adapter with filtered tasks
                taskshower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
                //taskshower.setAdapter(new EventAdapter(TaskCalendar.this, filteredTasks, this));

                int numEntities = filteredTasks.size();

                // Update the total tasks TextView
                totaltasks.setText("Total Tasks: " + numEntities);
                Log.v("Task Details", "Number of entities: " + numEntities);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TITLE, "On Resume!");
        loadData();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                // Create a Calendar instance for the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());
                selectedDateCalendar = selectedDate;
                selectedDateString2 = String.valueOf(selectedDateCalendar.get(Calendar.YEAR)) + "," +
                        String.valueOf(selectedDateCalendar.get(Calendar.MONTH)) + "," +
                        String.valueOf(selectedDateCalendar.get(Calendar.DAY_OF_MONTH));
                Log.v("String2Create", selectedDateString2);
                List<Task> filteredTasks = filterTasksByDate(selectedDateString2);
                saveData();
                //taskshower.setAdapter(new EventAdapter(TaskCalendar.this, filteredTasks, this));

                int numEntities = filteredTasks.size();

                // Update the total tasks TextView
                totaltasks.setText("Total Tasks: " + numEntities);
            }
        });

        // Log the selected date
        Log.v("OnResume", "Selected Date: " + selectedDateString);
        // Fetch the most recent tasks when the activity resumes
        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                taskList = tasks;

                // Filter tasks based on the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTimeInMillis(calendarView.getDate());
                String calendarviewgetdate = String.valueOf(calendarView.getDate());
                Log.v("Calendarviewgetdate", calendarviewgetdate);
                Log.v("StringRCreate", selectedDateString2);

                List<Task> filteredTasks = filterTasksByDate(selectedDateString2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());




                // Update the RecyclerView adapter with the filtered tasks
                //taskshower.setAdapter(new EventAdapter(TaskCalendar.this, filteredTasks, this));

                int numEntities = filteredTasks.size();

                // Update the total tasks TextView
                totaltasks.setText("Total Tasks: " + numEntities);
                Log.v("Task Details", "Number of entities: " + numEntities);
            }


        });

        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskCalendar.this, AddTask.class);
                intent.putExtra("DATE", selectedDateString);
                Log.v("Date passed", selectedDateString);
                saveData();
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getDate() {
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        return selected_date;

    }

    public void setDateToToday() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String selectedDateString = dateFormat.format(new Date(milli));

        // Log the converted date
        Log.v("SetDate", "Selected date: " + selectedDateString);
    }


    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;

        // Filter tasks based on the initially selected date
        List<Task> filteredTasks = filterTasksByDate(selectedDateString2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Set RecyclerView adapter with filtered tasks
        taskshower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
        taskshower.setAdapter(new MainpagetodoAdaptor(TaskCalendar.this, filteredTasks, this));

        int numEntities = filteredTasks.size();

        // Update the total tasks TextView
        totaltasks.setText("Total Tasks: " + numEntities);
        Log.v("Task Details", "Number of entities: " + numEntities);
    }


    private List<Task> filterTasksByDate(String selectedDate) {
        List<Task> filteredTasks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String[] dateParts = selectedDate.split(",");

        int day = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[0]);



        for (Task task : taskList) {
            try {
                if (task.getDate() != null) {
                    Calendar taskCalendar = Calendar.getInstance();
                    taskCalendar.setTime(dateFormat.parse(task.getDate()));

                    // Check if the task date matches the selected date
                    if (year == taskCalendar.get(Calendar.YEAR) &&
                            month == taskCalendar.get(Calendar.MONTH) &&
                            day == taskCalendar.get(Calendar.DAY_OF_MONTH)) {
                        filteredTasks.add(task);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredTasks;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(DATE_SAVED, selectedDateString2);
        editor.apply();
        Log.v("Saved Date From SP", selectedDateString2);
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        selectedDateString2 = sharedPreferences.getString(DATE_SAVED, "10,6,2023");

        Log.v("Load Date From SP", selectedDateString2);

    }

    @Override
    public void onItemClicked(Event event) {

    }

    @Override
    public void onTaskItemClicked(Task task) {

    }
}