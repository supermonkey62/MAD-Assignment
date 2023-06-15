package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewTaskAdd extends AppCompatActivity {
    private EditText newtask;
    private Button savebttn;
    private Button donebttn;
    private ArrayList<TodoModel> tasklist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_add);
        newtask = findViewById(R.id.newTasktext2);
        savebttn =findViewById(R.id.newtaskbutton);
        donebttn = findViewById(R.id.done);
        tasklist = new ArrayList<>();
        userRef = database.getReference("tasks");
        String taskName = newtask.getText().toString();
        savebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = tasklist.size()+1;

                TodoModel newTask = new TodoModel(id,false,taskName);
                String taskId = userRef.push().getKey();
                userRef.child(String.valueOf(id)).setValue(newTask);

                Toast.makeText(NewTaskAdd.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        donebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}