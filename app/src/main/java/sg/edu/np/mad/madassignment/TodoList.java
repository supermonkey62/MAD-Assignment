package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TodoList extends AppCompatActivity {
    private RecyclerView taskRecyclerview;
    private TodolistAdaptor taskadapter;
    private ArrayList<TodoModel> tasklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        tasklist = new ArrayList<>();

        taskRecyclerview = findViewById(R.id.tasksRecyclerView);
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        taskadapter = new TodolistAdaptor(this);
        taskRecyclerview.setAdapter(taskadapter);
        TodoModel task = new TodoModel(10,true,"testtask");

        tasklist.add(task);
        tasklist.add(task);
        tasklist.add(task);

        taskadapter.setTask(tasklist);


    }
}