package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodoList extends AppCompatActivity implements TaskDataHolder.TaskDataCallback {
    private RecyclerView taskRecyclerview;
    private TodolistAdaptor taskadapter;
    private List<Task> tasklist;

    private String username;

    private FloatingActionButton addtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_list);
        Intent mainpageintent = getIntent();
        if (mainpageintent.hasExtra("USERNAME")) {
            username = mainpageintent.getStringExtra("USERNAME");
        }
        tasklist = new ArrayList<>();
        addtask = findViewById(R.id.floatingActionButton2);
        taskRecyclerview = findViewById(R.id.tasksRecyclerView);


        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                tasklist = tasks;
                // Initialize taskadapter before setting it to the RecyclerView
                taskRecyclerview.setLayoutManager(new LinearLayoutManager(TodoList.this));
                taskRecyclerview.setAdapter(new TodolistAdaptor(TodoList.this, tasklist));


            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoList.this, AddTask.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("DATE", "18/06/2023");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = tasks;
        // Initialize taskadapter before setting it to the RecyclerView
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(TodoList.this));
        taskRecyclerview.setAdapter(new TodolistAdaptor(TodoList.this, tasklist));
        taskadapter.notifyDataSetChanged();
    }

}