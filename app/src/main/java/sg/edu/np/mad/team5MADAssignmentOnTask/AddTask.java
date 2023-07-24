package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AddTask extends AppCompatActivity {

    RadioButton radioButton;
    RadioGroup radioGroup;
    EditText titleEdit;

    TextView taskDate, cancelText, selectedOption;

    String selectedDate, username;

    String title = "Add Task";

    DatabaseReference taskRef;

    Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Log.v("AddTask", "Entered Add Task");
        selectedDate = getIntent().getStringExtra("DATE");
        username = getIntent().getStringExtra("USERNAME");
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);

        radioGroup = findViewById(R.id.addtaskradiogroup);
        Button addTaskButton = findViewById(R.id.addtaskbutton);
        cancelText = findViewById(R.id.canceltasktext);
        category = findViewById(R.id.catspinner);
        taskDate.setText(selectedDate);

        String[] categories = {"Personal Tasks", "School Task", "Assignments", "Projects", "Errands and Shopping Tasks", "Health and Fitness Tasks"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("AddTask", "creating task");
                createTask();

            }
        });

        cancelText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the action is UP, which indicates that the touch is released
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Finish the activity when the touch is released (click event)
                    finish();
                }
                return true; // Return true to indicate that the touch event is handled
            }
        });

    }

    private void createTask() {
        String taskTitle = titleEdit.getText().toString();
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String typeTitle = radioButton.getText().toString();
        String selectedCategory = category.getSelectedItem().toString();
        generateTaskId(username, new TaskIdCallback() {
            @Override
            public void onTaskIdGenerated(String taskId) {
                taskRef = FirebaseDatabase.getInstance().getReference("Task");
                taskRef.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.v(title, taskId + "already exists");
                        } else {
                            Task newTask = new Task(username, taskTitle, typeTitle, selectedDate, taskId, false, 0, 0, selectedCategory);
                            taskRef.child(taskId).setValue(newTask);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void generateTaskId(final String username, final TaskIdCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("taskCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int taskCount = dataSnapshot.getValue(Integer.class);
                    String taskId = username + taskCount;
                    callback.onTaskIdGenerated(taskId);

                    // Increment the taskCount and update it back in the database
                    int newTaskCount = taskCount + 1;
                    userRef.setValue(newTaskCount);

                    // Call the callback with the generated taskId
                    callback.onTaskIdGenerated(taskId);
                } else {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AddTask", "Error retrieving task count: " + databaseError.getMessage());
                // Handle the error if needed
            }
        });
    }
    public interface TaskIdCallback {
        void onTaskIdGenerated(String taskId);
    }

}


