package sg.edu.np.mad.team5MADAssignmentOnTask;

import static sg.edu.np.mad.team5MADAssignmentOnTask.CalendarUtils.daysInWeekArray;
import static sg.edu.np.mad.team5MADAssignmentOnTask.CalendarUtils.selectedDate;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements CalendarAdapter.OnItemListener, TaskDataHolder.TaskDataCallback, EventDataHolder.EventDataCallback, UserDataHolder.UserDataCallback, SelectListener{
    private TextView monthYearText, greetingText, displaynametext;

    private RecyclerView calendarRecyclerView, eventShower, taskShower;
    List<Task> taskList;
    List<Event> eventList;
    RecyclerView recyclerView;
    String title = "HomeFragment";

    String selectedDateString, username;
    
    private OnDateSelectedListener dateSelectedListener;

    String selectedDateString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        selectedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateString = dateFormat.format(selectedDate);
        Log.v("Selected Date", selectedDateString);
        initWidgets(view);

        username = getArguments().getString("USERNAME");
        Log.v("UsernameHome", username);
        TaskDataHolder.getInstance().fetchUserTasks(username, this);
        UserDataHolder.getInstance().fetchUserData(username, this);
        EventDataHolder.getInstance().fetchUserEvents(username, this);

        setWeekView();
        user_greeting(view);

        return view;
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        greetingText = view.findViewById(R.id.greeting_text);
        displaynametext = view.findViewById(R.id.displayNameText);
        recyclerView = view.findViewById(R.id.upcomingEventRecycler);
        eventShower = view.findViewById(R.id.eventshower);
        taskShower = view.findViewById(R.id.taskshower);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDateSelectedListener) {
            dateSelectedListener = (OnDateSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDateSelectedListener");
        }
    }

    private void setWeekView() {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(monthYearFormat.format(selectedDate));
        ArrayList<Date> days = daysInWeekArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousWeekAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        selectedDate = calendar.getTime();
        updateViewsForSelectedDate(selectedDate);
    }

    public void nextWeekAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        selectedDate = calendar.getTime();
        updateViewsForSelectedDate(selectedDate);
    }

    private void updateViewsForSelectedDate(Date selectedDate) {
        setWeekView();
        onItemClick(-1, selectedDate); // Manually call onItemClick with the new selected date
    }

    @Override
    public void onItemClick(int position, Date date) {
        selectedDate = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateString = dateFormat.format(date);
        Log.v("OnClickDate", selectedDateString);
        setWeekView();
        List<Task> filteredTasks = filterTasksByDate(selectedDateString);
        List<Event> filteredEvents = filterEventsByDate(selectedDateString);
        eventShower.setAdapter(new EventAdapter(getActivity(), filteredEvents, this));
        taskShower.setAdapter(new MainpagetodoAdaptor(getActivity(), filteredTasks));

        // Send the selected date back to Stage2MainPage using the callback interface
        dateSelectedListener.onDateSelected(selectedDateString);
    }


    private void user_greeting(View view) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            greetingText.setText("Good morning");
        } else if (hour < 18) {
            greetingText.setText("Good afternoon");
        } else {
            greetingText.setText("Good evening");
        }
    }

    public void onUserDataFetched(String displayname) {
        displaynametext.setText(displayname);
    }

    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;

        List<String> dateList = new ArrayList<>();
        List<String> taskCountList = new ArrayList<>();

        // Create a HashMap to store the date and task count
        HashMap<String, HashMap<String, Integer>> dateTaskStatusCountMap = new HashMap<>();

        for (Task task : taskList) {
            String date = task.getDate(); // Replace this with the appropriate getter method for the date in your Task class
            String status = String.valueOf(task.getStatus()); // Replace this with the appropriate getter method for the status in your Task class

            if (date != null) { // Check if the date is not null before proceeding
                if (!dateTaskStatusCountMap.containsKey(date)) {
                    // Date doesn't exist in the HashMap, create a new inner HashMap for the status counts
                    dateTaskStatusCountMap.put(date, new HashMap<>());
                }

                HashMap<String, Integer> statusCountMap = dateTaskStatusCountMap.get(date);

                if (statusCountMap.containsKey(status)) {
                    // Status already exists in the inner HashMap, increment the task count for that status
                    int taskCount = statusCountMap.get(status);
                    statusCountMap.put(status, taskCount + 1);
                } else {
                    // Status doesn't exist in the inner HashMap, add it with task count 1
                    statusCountMap.put(status, 1);
                }
            }
        }


        // Iterate over the HashMap entries and construct the task count strings
        for (Map.Entry<String, HashMap<String, Integer>> dateEntry : dateTaskStatusCountMap.entrySet()) {
            String date = dateEntry.getKey();
            HashMap<String, Integer> statusCountMap = dateEntry.getValue();

            StringBuilder taskCountStringBuilder = new StringBuilder();
            for (Map.Entry<String, Integer> statusEntry : statusCountMap.entrySet()) {
                String status = statusEntry.getKey();
                int taskCount = statusEntry.getValue();
                taskCountStringBuilder.append(taskCount).append(" ").append(status).append(", ");
            }

            // Remove the trailing comma and space
            if (taskCountStringBuilder.length() > 2) {
                taskCountStringBuilder.setLength(taskCountStringBuilder.length() - 2);
            }

            // Add the date and task count string to the respective lists
            dateList.add(date);
            taskCountList.add(taskCountStringBuilder.toString());
        }

        // Sort the dateList in ascending order
        Collections.sort(dateList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MainPageCalendarAdapter adapter = new MainPageCalendarAdapter(getActivity(), dateList, taskCountList);
        recyclerView.setAdapter(adapter);

        Log.v(title, "Number of tasks in taskCountList: " + taskCountList.size());

        List<Task> filteredTasks = filterTasksByDate(selectedDateString);

        taskShower.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskShower.setAdapter(new MainpagetodoAdaptor(getActivity(), filteredTasks));
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

    @Override
    public void onItemClicked(Event event) {
        // Handle the click event for the task items in the RecyclerView
        // For example, you can display a dialog or navigate to a new activity
        Intent intent = new Intent(getActivity(), EditTask.class);
        // Pass the task data to the EditTaskActivity using intent extras
        intent.putExtra("TAG", event.getId());
        intent.putExtra("USERNAME", event.getUsername());
        intent.putExtra("START_DATE", event.getStartDate());
        intent.putExtra("END_DATE", event.getEndDate());
        intent.putExtra("START_TIME", event.getStartTime());
        intent.putExtra("END_TIME", event.getEndTime());
        startActivity(intent);
    }

    @Override
    public void onEventDataFetched(List<Event> events) {
        eventList = events;

        List<Event> filteredEvents = filterEventsByDate(selectedDateString);
        eventShower.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventShower.setAdapter(new EventAdapter(getActivity(), filteredEvents, this));

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

    public interface OnDateSelectedListener {
        void onDateSelected(String selectedDate);
    }

    public void openAddTaskActivity() {
        Intent addTaskIntent = new Intent(getActivity(), AddTask.class);
        addTaskIntent.putExtra("DATE", selectedDateString);
        addTaskIntent.putExtra("USERNAME", username);
        startActivity(addTaskIntent);
    }
}







