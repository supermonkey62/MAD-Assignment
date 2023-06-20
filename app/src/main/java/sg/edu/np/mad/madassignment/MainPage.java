package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity implements TaskDataHolder.TaskDataCallback, UserDataHolder.UserDataCallback {
    String title = "MainPage";
    Button pomodorotimer;
    FloatingActionButton normalTimer, profilePage, calendarOverview, toDoList;
    TextView displaynametext;
    List<Task> taskList;
    String displayname;
    RecyclerView recyclerView,todorecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        normalTimer = findViewById(R.id.normalTimer);
        profilePage = findViewById(R.id.profilePage);
        calendarOverview = findViewById(R.id.calendarOverview);
        displaynametext = findViewById(R.id.displaynametext);
        recyclerView = findViewById(R.id.upcomingEventRecycler);
        toDoList = findViewById(R.id.toDoList);
        todorecycler = findViewById(R.id.mainpagetodo);

        user_greeting();

        String password = getIntent().getStringExtra("PASSWORD");
        String username = getIntent().getStringExtra("USERNAME");

        UserDataHolder.getInstance().fetchUserData(username, this);
        TaskDataHolder.getInstance().fetchUserTasks(username, this);

        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                taskList = tasks;
                todorecycler.setLayoutManager(new LinearLayoutManager(MainPage.this));
                todorecycler.setAdapter(new MainpagetodoAdaptor(MainPage.this, taskList));
            }
        });


        normalTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
            private void showOptionsDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setTitle("Options")
                        .setItems(new CharSequence[]{"Pomodoro Timer", "Countdown Timer", "Cancel"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        startActivity1();
                                        break;
                                    case 1:
                                        startActivity2();
                                        break;
                                    case 2:

                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }

            private void startActivity1() {


                Intent intent = new Intent(MainPage.this, PomodoroTimer.class);

                startActivity(intent);
            }

            private void startActivity2() {


                Intent intent = new Intent(MainPage.this, timer.class);

                startActivity(intent);
            }

        });

        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Entering Profile Page", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(MainPage.this, ProfilePage.class);
                intent3.putExtra("DISPLAYNAME", displayname);
                intent3.putExtra("USERNAME", username);
                Log.v("Main User","+" + username);
                intent3.putExtra("Password", password);
                startActivity(intent3);
            }
        });

        calendarOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Entering Calendar", Toast.LENGTH_SHORT).show();
                Intent toCalendar = new Intent(MainPage.this, TaskCalendar.class);
                toCalendar.putExtra("USERNAME", username);
                startActivity(toCalendar);
            }
        });

        toDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent totodolist = new Intent(MainPage.this,TodolistFragmentholder.class);
                totodolist.putExtra("USERNAME",username);
                startActivity(totodolist);
            }
        });
    }

    private void user_greeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting = "Hello there";
        int imageId = R.drawable.morning;

        if (hour >= 0 && hour < 12) {
            greeting = "Good morning";
            imageId = R.drawable.morning;
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good afternoon";
            imageId = R.drawable.afternoon;
        } else{
            greeting = "Good evening";
            imageId = R.drawable.evening;
        }

        TextView greetingText = findViewById(R.id.greeting_text);
        greetingText.setText(greeting);

        ImageView greetingImage = findViewById(R.id.greenbox);
        greetingImage.setImageResource(imageId);
    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;

        List<String> dateList = new ArrayList<>();
        List<String> taskCountList = new ArrayList<>();
        int numEntities = taskList.size();
        Log.v(title, "Number of entities: " + numEntities);

        // Create a HashMap to store the date and task count
        HashMap<String, HashMap<String, Integer>> dateTaskStatusCountMap = new HashMap<>();

        for (Task task : taskList) {
            String date = task.getDate(); // Replace this with the appropriate getter method for the date in your Task class
            String status = String.valueOf(task.getStatus()); // Replace this with the appropriate getter method for the status in your Task class

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

        recyclerView.setLayoutManager(new LinearLayoutManager(MainPage.this));
        MainPageCalendarAdapter adapter = new MainPageCalendarAdapter(MainPage.this, dateList, taskCountList);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onUserDataFetched(String displayname) {
        this.displayname = displayname;
        displaynametext.setText(displayname);

    }
}
