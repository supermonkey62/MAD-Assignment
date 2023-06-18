package sg.edu.np.mad.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
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
import java.util.List;
import java.util.Locale;

public class TaskCalendar extends AppCompatActivity implements TaskDataHolder.TaskDataCallback {

    CalendarView calendarView;
    Calendar calendar;

    TextView totaltasks;

    String selectedDateString;
    String TITLE = "TaskCalendar";
    String username;
    FloatingActionButton addTasks;
    RecyclerView taskshower;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        taskshower = findViewById(R.id.taskshower);
        addTasks = findViewById(R.id.addtask);
        setDateToToday();

        totaltasks = findViewById(R.id.totaltasks);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                // Create a Calendar instance for the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());


                // Filter the task list based on the selected date
                List<Task> filteredTasks = filterTasksByDate(selectedDate);

                // Update the RecyclerView adapter with the filtered task list
                taskshower.setAdapter(new Adapter(TaskCalendar.this, filteredTasks));

                int numEntities = filteredTasks.size();

                // Update the total tasks TextView
                totaltasks.setText("Total Tasks: " + numEntities);
            }
        });

        username = getIntent().getStringExtra("USERNAME");
        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                taskList = tasks;

                // Filter tasks based on the initially selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTimeInMillis(calendarView.getDate());
                List<Task> filteredTasks = filterTasksByDate(selectedDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());

                // Set RecyclerView adapter with filtered tasks
                taskshower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
                taskshower.setAdapter(new Adapter(TaskCalendar.this, filteredTasks));

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
                intent.putExtra("USERNAME", username);
                startActivity(intent);
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
    }


    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;

        // Filter tasks based on the initially selected date
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(calendarView.getDate());
        List<Task> filteredTasks = filterTasksByDate(selectedDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateString = dateFormat.format(selectedDate.getTime());

        // Set RecyclerView adapter with filtered tasks
        taskshower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
        taskshower.setAdapter(new Adapter(TaskCalendar.this, filteredTasks));

        int numEntities = filteredTasks.size();

        // Update the total tasks TextView
        totaltasks.setText("Total Tasks: " + numEntities);
        Log.v("Task Details", "Number of entities: " + numEntities);
    }


    private List<Task> filterTasksByDate(Calendar selectedDate) {
        List<Task> filteredTasks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

        for (Task task : taskList) {
            try {
                if (task.getDate() != null) {
                    Calendar taskCalendar = Calendar.getInstance();
                    taskCalendar.setTime(dateFormat.parse(task.getDate()));

                    // Check if the task date matches the selected date
                    if (selectedDate.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR) &&
                            selectedDate.get(Calendar.MONTH) == taskCalendar.get(Calendar.MONTH) &&
                            selectedDate.get(Calendar.DAY_OF_MONTH) == taskCalendar.get(Calendar.DAY_OF_MONTH)) {
                        filteredTasks.add(task);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredTasks;
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.v(TITLE, "On Start!");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TITLE, "On Pause!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TITLE, "On Resume!");

        // Fetch the most recent tasks when the activity resumes
        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {


            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                taskList = tasks;

                // Filter tasks based on the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTimeInMillis(calendarView.getDate());
                List<Task> filteredTasks = filterTasksByDate(selectedDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());
                Log.v("OnResume", selectedDateString);

                // Update the RecyclerView adapter with the filtered tasks
                taskshower.setAdapter(new Adapter(TaskCalendar.this, filteredTasks));

                int numEntities = filteredTasks.size();

                // Update the total tasks TextView
                totaltasks.setText("Total Tasks: " + numEntities);
                Log.v("Task Details", "Number of entities: " + numEntities);
            }
        });
    }


    @Override
    protected void onStop(){
        super.onStop();
        Log.v(TITLE, "On Stop!");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.v(TITLE, "On Destroy!");
    }

}