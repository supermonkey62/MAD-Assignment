package sg.edu.np.mad.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodoList extends AppCompatActivity {
    private RecyclerView taskRecyclerview;
    private TodolistAdaptor taskadapter;
    private ArrayList<TodoModel> tasklist;
    private DatabaseReference taskRef;
    private FloatingActionButton newtaskbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        tasklist = new ArrayList<>();
        newtaskbutton =findViewById(R.id.floatingActionButton2);
        taskadapter = new TodolistAdaptor(tasklist);
        taskRecyclerview = findViewById(R.id.tasksRecyclerView);
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerview.setAdapter(taskadapter);

        taskRef = FirebaseDatabase.getInstance().getReference("tasks");

        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                tasklist.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    TodoModel task = taskSnapshot.getValue(TodoModel.class);
                    tasklist.add(task);
                }
                taskadapter.setTaskList(tasklist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TodoList.this, "Failed to retrieve tasks", Toast.LENGTH_SHORT).show();
            }
        });


        newtaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newtaskintent = new Intent(TodoList.this, NewTaskAdd.class);
                startActivity(newtaskintent);
            }
        });


    }
}


