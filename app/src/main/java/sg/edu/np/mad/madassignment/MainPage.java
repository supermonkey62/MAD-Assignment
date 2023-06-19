package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements TaskDataHolder.TaskDataCallback, UserDataHolder.UserDataCallback {
    String title = "MainPage";
    Button pomodorotimer, normaltimer, Profile, todolist;
    TextView displaynametext;
    ImageView calendarexpand;
    List<Task> taskList;
    String displayname;
    RecyclerView recyclerView,todorecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        pomodorotimer = findViewById(R.id.pomobutton);
        normaltimer = findViewById(R.id.NormalTimerBttn);
        displaynametext = findViewById(R.id.displaynametext);
        Profile = findViewById(R.id.profilepageBttn);
        calendarexpand = findViewById(R.id.calendarexpand);
        recyclerView = findViewById(R.id.calenderrecycler);
        todolist = findViewById(R.id.tasks);
        todorecycler = findViewById(R.id.mainpagetodo);

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

        pomodorotimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Entering Pomodoro Timer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, PomodoroTimer.class);
                startActivity(intent);
            }
        });

        normaltimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Entering Timer", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainPage.this, timer.class);
                startActivity(intent2);
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
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

        calendarexpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Entering Calendar", Toast.LENGTH_SHORT).show();
                Intent toCalendar = new Intent(MainPage.this, TaskCalendar.class);
                toCalendar.putExtra("USERNAME", username);
                startActivity(toCalendar);
            }
        });

        todolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent totodolist = new Intent(MainPage.this,TodolistFragmentholder.class);
                totodolist.putExtra("USERNAME",username);
                startActivity(totodolist);
            }
        });
    }



    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(MainPage.this));
        recyclerView.setAdapter(new Adapter(MainPage.this, taskList));
        int numEntities = taskList.size();
        Log.v("Task Details", "Number of entities: " + numEntities);
    }

    @Override
    public void onUserDataFetched(String displayname) {
        this.displayname = displayname;
        displaynametext.setText(displayname);
    }
}
