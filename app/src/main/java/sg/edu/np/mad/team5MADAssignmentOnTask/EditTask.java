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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class EditTask extends AppCompatActivity {

    RadioButton radioButton;
    RadioGroup radioGroup;
    EditText titleEdit;

    TextView taskDate, cancelText, profileback;

    String selectedDate, username, tag;

    Boolean status;

    TextView selectedOption;

    DatabaseReference userTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Log.v("EditTask", "Entered Edit Task");

        selectedDate = getIntent().getStringExtra("DATE");

        username = getIntent().getStringExtra("USERNAME");
        tag = getIntent().getStringExtra("TAG");
        status = getIntent().getBooleanExtra("STATUS", false);
        Log.v("TaskCount", username);
        Log.v("TaskCount", tag);
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        selectedOption = findViewById(R.id.selectedoption);
        radioGroup = findViewById(R.id.addtaskradiogroup);
        Button editTask = findViewById(R.id.edittaskbutton);
        cancelText = findViewById(R.id.canceltasktext);

        taskDate.setText(selectedDate);
        // Load the database



        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskTitle = titleEdit.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String typeTitle = radioButton.getText().toString();
                selectedOption.setText("Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username + " , " + tag);
                userTask = FirebaseDatabase.getInstance().getReference("Task");

                userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.v("Username", tag);
                            Log.v("CreateTask","Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username + " , " + tag + " , " + status);
                            Task newTask = new Task(username, taskTitle, typeTitle, selectedDate, tag, status);
                            userTask.child(tag).setValue(newTask);
                            finish();

                        }
                        else {
                            Log.v("TaskCount", tag + " does not  exists.");
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

//public class EditTask extends AppCompatActivity {
//
//    RadioButton radioButton;
//    RadioGroup radioGroup;
//    EditText titleEdit;
//
//    TextView taskDate, cancelText;
//
//    String selectedDate, username, tag;
//
//    Boolean status;
//
//    TextView selectedOption;
//
//    DatabaseReference userTask;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_task);
//        Log.v("EditTask", "Entered Edit Task");
//
//        selectedDate = getIntent().getStringExtra("DATE");
//
//        username = getIntent().getStringExtra("USERNAME");
//        tag = getIntent().getStringExtra("TAG");
//        status = getIntent().getBooleanExtra("STATUS", false);
//        Log.v("TaskCount", username);
//        Log.v("TaskCount", tag);
//        taskDate = findViewById(R.id.taskdate);
//        titleEdit = findViewById(R.id.titleEdit);
//        selectedOption = findViewById(R.id.selectedoption);
//        radioGroup = findViewById(R.id.addtaskradiogroup);
//        Button editTask = findViewById(R.id.edittaskbutton);
//        cancelText = findViewById(R.id.canceltasktext);
//
//        taskDate.setText(selectedDate);
//        // Load the database
//
//
//
//        editTask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String taskTitle = titleEdit.getText().toString();
//                int radioId = radioGroup.getCheckedRadioButtonId();
//                radioButton = findViewById(radioId);
//                String typeTitle = radioButton.getText().toString();
//                selectedOption.setText("Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username + " , " + tag);
//                userTask = FirebaseDatabase.getInstance().getReference("Task");
//
//                userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Log.v("Username", tag);
//                            Log.v("CreateTask","Task: " + taskTitle + " , " + typeTitle + " , " + selectedDate + " , " + username + " , " + tag + " , " + status);
//                            Task newTask = new Task(username, taskTitle, typeTitle, selectedDate, status,username);
//                            userTask.child(tag).setValue(newTask);
//                            finish();
//
//                        }
//                        else {
//                            Log.v("TaskCount", tag + " does not  exists.");
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
//                    }
//                });
//            }
//        });
//
//
//
//        cancelText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//
//
//    }
//}
