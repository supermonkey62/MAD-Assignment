package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class EditTask extends AppCompatActivity {
    EditText titleEdit;
    TextView taskDate, cancelText, profileback;
    String selectedDate, username, tag, title, collaborators;
    Button deleteButton;
    List<User> selectedUsers;
    private Button editCollaboratorsButton;
    Boolean status;
    DatabaseReference userTask;
    float existingTimeSpent;

    private static final int REQUEST_SELECT_USERS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Log.v("EditTask", "Entered Edit Task");
        selectedDate = getIntent().getStringExtra("DATE");
        title = getIntent().getStringExtra("TITLE");
        username = getIntent().getStringExtra("USERNAME");
        tag = getIntent().getStringExtra("TAG");
        collaborators = getIntent().getStringExtra("COLLABORATORS");
        Log.v("Intented Collab", collaborators);
        status = getIntent().getBooleanExtra("STATUS", false);
        Log.v("TaskCount", username);
        Log.v("TaskCount", tag);
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        Button editTask = findViewById(R.id.edittaskbutton);
        deleteButton = findViewById(R.id.deleteTaskButton);
        cancelText = findViewById(R.id.canceltasktext);
        editCollaboratorsButton = findViewById(R.id.editCollaboratorsButton);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String selectedUsersJson = preferences.getString("SELECTED_USERS", "");
        selectedUsers = new Gson().fromJson(selectedUsersJson, new TypeToken<List<User>>() {}.getType());
        taskDate.setText(selectedDate);
        titleEdit.setText(title);
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
                            Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent,existingsession,category, collaborators);
                            userTask.child(tag).setValue(newTask);
                            finish();

                        }
                        else { Log.v("TaskCount", tag + " does not  exists.");}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
                    }
                });
            }
        });

        editCollaboratorsButton.setOnClickListener(view -> {
            Intent searchUsersIntent = new Intent(EditTask.this, SearchForUsers.class);
            searchUsersIntent.putExtra("USERNAME", username);
            Log.v("Check Collab", collaborators);
            searchUsersIntent.putExtra("COLLABORATORS", collaborators);
            startActivityForResult(searchUsersIntent, REQUEST_SELECT_USERS);
        });
        retrieveSelectedUsers();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_USERS && resultCode == RESULT_OK) {
            // Retrieve the selected user data from SharedPreferences
            retrieveSelectedUsers();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the selected users' data from SharedPreferences when the activity is resumed
        retrieveSelectedUsers();
    }

    private void retrieveSelectedUsers() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String selectedUsersJson = preferences.getString("SELECTED_USERS", "");
        selectedUsers = new Gson().fromJson(selectedUsersJson, new TypeToken<List<User>>() {}.getType());
        Log.v("Retrieve selected users", selectedUsersJson);

        if (selectedUsers == null || selectedUsers.isEmpty()) {
            // If selectedUsers is null or empty, set collaborators to "NIL"
            collaborators = "NIL";
        } else {
            // Combine the usernames of collaborators into a comma-separated string
            StringBuilder collaboratorsStringBuilder = new StringBuilder();
            for (User user : selectedUsers) {
                collaboratorsStringBuilder.append(user.getUsername()).append(",");
            }
            // Remove the trailing comma if there are collaborators
            collaborators = collaboratorsStringBuilder.toString().replaceAll(",$", "");
        }
    }

    private void clearSelectedUsers() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("SELECTED_USERS");
        editor.apply();
    }
}

