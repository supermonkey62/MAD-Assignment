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

                // Set RecyclerView adapter with filtered tasks
                taskRecyclerview.setLayoutManager(new LinearLayoutManager(TodoList.this));
                taskadapter = new TodolistAdaptor(TodoList.this,tasklist);
                taskRecyclerview.setAdapter(taskadapter);
            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoList.this, AddTask.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = tasks;
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        // Initialize taskadapter before setting it to the RecyclerView
        taskadapter = new TodolistAdaptor(TodoList.this, tasklist);
        taskRecyclerview.setAdapter(taskadapter);
        taskadapter.notifyDataSetChanged();
    }

}