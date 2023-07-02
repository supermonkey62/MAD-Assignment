package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    TextView taskDate, cancelText,selectedOption, profilepageback;

    String selectedDate, username, taskCountString, taskObjectName, tag;

    DatabaseReference userTask, userTaskCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Log.v("AddTask", "Entered Add Task");
        selectedDate = getIntent().getStringExtra("DATE");
        username = getIntent().getStringExtra("USERNAME");
        tag = getIntent().getStringExtra("TAG");
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        selectedOption = findViewById(R.id.selectedoption);
        radioGroup = findViewById(R.id.addtaskradiogroup);
        Button addTask = findViewById(R.id.addtaskbutton);
        cancelText = findViewById(R.id.canceltasktext);
        profilepageback = findViewById(R.id.profilepageback2);

        taskDate.setText(selectedDate);
        // Load the database


        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = titleEdit.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String typeTitle = radioButton.getText().toString();
                selectedOption.setText("Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username);
                userTaskCount = FirebaseDatabase.getInstance().getReference("TaskCount");

                userTaskCount.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Long savedTaskCount = dataSnapshot.child("count").getValue(Long.class);
                            taskCountString = String.valueOf(savedTaskCount);
                            int taskCountIncrease = Integer.parseInt(taskCountString);
                            taskCountIncrease++;
                            taskObjectName = username + taskCountIncrease;
                            Log.v("TaskCount", taskCountString);
                            TaskCount newTaskCount = new TaskCount(username, taskCountIncrease);
                            userTaskCount.child(username).setValue(newTaskCount);

                            userTask = FirebaseDatabase.getInstance().getReference("Task");
                            userTask.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Log.v("TaskCount", taskObjectName + " already exists.");
                                    }
                                    else {
                                        Log.v("Username", taskObjectName);
                                        Log.v("CreateTask","Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username + " , " + taskObjectName);
                                        Task newTask = new Task(username, taskTitle, typeTitle, selectedDate, taskObjectName, false);
                                        userTask.child(taskObjectName).setValue(newTask);
                                        finish();

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.v("LoginPage", "Error: " + databaseError.getMessage());
                                }
                            });
                        }
                        else {
                            Log.v("TaskCount", "Fail");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
                    }
                });




            }
        });



        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}