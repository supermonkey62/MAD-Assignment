package sg.edu.np.mad.team5MADAssignmentOnTask;

import static sg.edu.np.mad.team5MADAssignmentOnTask.CalendarUtils.selectedDate;

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

public class TaskCalendar extends AppCompatActivity implements TaskDataHolder.TaskDataCallback, SelectListener, EventDataHolder.EventDataCallback {
    CalendarView calendarView;
    String selectedDateString;
    String TITLE = "TaskCalendar";
    String username;
    FloatingActionButton add, addtask, addevent;
    RecyclerView eventShower; // RecyclerView for events
    RecyclerView taskShower; // RecyclerView for tasks
    ImageView gobackButton;
    Calendar selectedDateCalendar;
    List<Task> taskList;
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TITLE, "OnCreate");

        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        eventShower = findViewById(R.id.eventshower);
        taskShower = findViewById(R.id.taskshower);
        gobackButton = findViewById(R.id.gobackButton);
        add = findViewById(R.id.add);
        addtask = findViewById(R.id.addtask);
        addevent = findViewById(R.id.addevent);
        username = getIntent().getStringExtra("USERNAME");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateString = dateFormat.format(selectedDate);

        // Fetch the most recent tasks when the activity starts
        TaskDataHolder.getInstance().fetchUserTasks(username, this);
        // Fetch the most recent events when the activity starts
        EventDataHolder.getInstance().fetchUserEvents(username, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TITLE, "On Resume!");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                // Create a Calendar instance for the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDateString = dateFormat.format(selectedDate.getTime());
                selectedDateCalendar = selectedDate;
                List<Event> filteredEvents = filterEventsByDate(selectedDateString);
                List<Task> filteredTasks = filterTasksByDate(selectedDateString);
                taskShower.setAdapter(new MainpagetodoAdaptor(TaskCalendar.this, filteredTasks, TaskCalendar.this));
                eventShower.setAdapter(new EventAdapter(TaskCalendar.this, filteredEvents, TaskCalendar.this));

            }
        });
        // Assuming 'add' is the "Add" button, 'addtask' is the view for adding tasks, and 'addevent' is the view for adding events.

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addtask.getVisibility() == View.VISIBLE && addevent.getVisibility() == View.VISIBLE) {
                    addtask.setVisibility(View.GONE);
                    addevent.setVisibility(View.GONE);
                } else {
                    addtask.setVisibility(View.VISIBLE);
                    addevent.setVisibility(View.VISIBLE);
                }
            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddTask activity
                Intent intent = new Intent(TaskCalendar.this, AddTask.class);
                intent.putExtra("DATE", selectedDateString); // Pass any required data to the AddTask activity
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch AddEvent activity
                Intent intent = new Intent(TaskCalendar.this, AddEvent.class);
                intent.putExtra("DATE", selectedDateString); // Pass any required data to the AddEvent activity
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



    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;

        List<Task> filteredTasks = filterTasksByDate(selectedDateString);
        taskShower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
        taskShower.setAdapter(new MainpagetodoAdaptor(TaskCalendar.this, filteredTasks, this));
    }

    @Override
    public void onEventDataFetched(List<Event> events) {
        eventList = events;

        List<Event> filteredEvents = filterEventsByDate(selectedDateString);
        eventShower.setLayoutManager(new LinearLayoutManager(TaskCalendar.this));
        eventShower.setAdapter(new EventAdapter(TaskCalendar.this, filteredEvents, this));
    }


    private List<Task> filterTasksByDate(String selectedDate) {
        List<Task> filteredTasks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String[] dateParts = selectedDate.split("/");

        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Log.v("filter Tasks", String.valueOf(day + ", " +  month + ", " + year));

        for (Task task : taskList) {
            try {
                if (task.getDate() != null && !task.getStatus()) {
                    java.util.Calendar taskCalendar = java.util.Calendar.getInstance();
                    taskCalendar.setTime(dateFormat.parse(task.getDate()));
                    // Log the year, month, and date of the task
                    int taskYear = taskCalendar.get(java.util.Calendar.YEAR);
                    int taskMonth = taskCalendar.get(java.util.Calendar.MONTH);
                    int taskDay = taskCalendar.get(java.util.Calendar.DAY_OF_MONTH);
                    Log.v("TaskDate", "Year: " + taskYear + ", Month: " + taskMonth + ", Day: " + taskDay);
                    // Check if the task date matches the selected date
                    if (year == taskCalendar.get(java.util.Calendar.YEAR) &&
                            month - 1 == taskCalendar.get(java.util.Calendar.MONTH) &&
                            day == taskCalendar.get(java.util.Calendar.DAY_OF_MONTH)) {
                        filteredTasks.add(task);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int numEntities = filteredTasks.size();
        Log.v("FilteredTasksSize", "Number of entities: " + numEntities);
        return filteredTasks;
    }

    private List<Event> filterEventsByDate(String selectedDate) {
        List<Event> filteredEvents = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String[] dateParts = selectedDate.split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        Log.v("filter Events", String.valueOf(day + ", " +  month + ", " + year));
        for (Event event : eventList) {
            try {
                if (event.getStartDate() != null) {
                    java.util.Calendar Calendar = java.util.Calendar.getInstance();
                    Calendar.setTime(dateFormat.parse(event.getStartDate()));
                    // Log the year, month, and date of the task
                    int eventYear = Calendar.get(java.util.Calendar.YEAR);
                    int eventMonth = Calendar.get(java.util.Calendar.MONTH);
                    int eventDay = Calendar.get(java.util.Calendar.DAY_OF_MONTH);
                    // Check if the task date matches the selected date
                    if (year == Calendar.get(java.util.Calendar.YEAR) &&
                            month - 1 == Calendar.get(java.util.Calendar.MONTH) &&
                            day == Calendar.get(java.util.Calendar.DAY_OF_MONTH)) {
                        filteredEvents.add(event);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int numEntities = filteredEvents.size();
        Log.v("FilteredEventsSize", "Number of entities: " + numEntities);
        return filteredEvents;
    }

    @Override
    public void onItemClicked(Event event) {
        // Handle the click event for the task items in the RecyclerView
        // For example, you can display a dialog or navigate to a new activity
        Intent intent = new Intent(TaskCalendar.this, EditEvent.class);
        // Pass the task data to the EditTaskActivity using intent extras
        intent.putExtra("ID", event.getId());
        intent.putExtra("USERNAME", event.getUsername());
        intent.putExtra("START_DATE", event.getStartDate());
        intent.putExtra("END_DATE", event.getEndDate());
        intent.putExtra("START_TIME", event.getStartTime());
        intent.putExtra("END_TIME", event.getEndTime());
        intent.putExtra("DESCRIPTION", event.getDescription());
        intent.putExtra("TITLE", event.getTitle());
        startActivity(intent);
    }

    public void onTaskItemClicked(Task task) {
        Intent intent = new Intent(TaskCalendar.this, EditTask.class);
        intent.putExtra("USERNAME", task.getUsername());
        intent.putExtra("TITLE", task.getTitle());
        intent.putExtra("DATE", task.getDate());
        intent.putExtra("TAG", task.getTag());
        intent.putExtra("STATUS", task.getStatus());
        intent.putExtra("TIMESPENT", task.getTimespent());
        intent.putExtra("SESSIONS", task.getSessions());
        intent.putExtra("CATEGORY", task.getCategory());
        intent.putExtra("COLLABORATORS", task.getCollaborators());
        startActivity(intent);
    }

}