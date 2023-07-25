package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTask extends AppCompatActivity {
    EditText titleEdit;

    TextView taskDate, cancelText, profileback;

    String selectedDate, username, tag, title;

    Button deleteButton;

    Boolean status;

    DatabaseReference userTask;
    float existingTimeSpent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Log.v("EditTask", "Entered Edit Task");

        selectedDate = getIntent().getStringExtra("DATE");
        title = getIntent().getStringExtra("TITLE");
        username = getIntent().getStringExtra("USERNAME");
        tag = getIntent().getStringExtra("TAG");
        status = getIntent().getBooleanExtra("STATUS", false);
        Log.v("TaskCount", username);
        Log.v("TaskCount", tag);
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        Button editTask = findViewById(R.id.edittaskbutton);
        deleteButton = findViewById(R.id.deleteTaskButton);
        cancelText = findViewById(R.id.canceltasktext);

        taskDate.setText(selectedDate);
        titleEdit.setText(title);
        // Load the database



        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskTitle = titleEdit.getText().toString();
                userTask = FirebaseDatabase.getInstance().getReference("Task");


                userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Task existingtask = dataSnapshot.getValue(Task.class);
                            existingTimeSpent = existingtask.getTimespent();
                            int existingsession = existingtask.getSessions();
                            String category = existingtask.getCategory();
                            Log.v("Username", tag);
                            Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent,existingsession,category,false);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    private void deleteTask() {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Task");
        eventRef.child(tag).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditTask.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after successful deletion
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditTask.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

