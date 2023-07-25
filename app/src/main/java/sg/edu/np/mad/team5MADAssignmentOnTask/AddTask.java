package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class AddTask extends AppCompatActivity {
    EditText titleEdit;
    TextView taskDate, cancelText;
    Button searchUsersButton;
    String selectedDate, username;
    String title = "Add Task";
    DatabaseReference taskRef;
    Spinner category;
    List<User> selectedUsers;
    private static final int REQUEST_SELECT_USERS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Clear the selected users from SharedPreferences on activity create
        clearSelectedUsers();
        Log.v("AddTask", "Entered Add Task");
        selectedDate = getIntent().getStringExtra("DATE");
        username = getIntent().getStringExtra("USERNAME");
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        Button addTaskButton = findViewById(R.id.addtaskbutton);
        cancelText = findViewById(R.id.canceltasktext);
        category = findViewById(R.id.catspinner);
        taskDate.setText(selectedDate);
        searchUsersButton = findViewById(R.id.searchUsersButton);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String selectedUsersJson = preferences.getString("SELECTED_USERS", "");
        selectedUsers = new Gson().fromJson(selectedUsersJson, new TypeToken<List<User>>() {}.getType());
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

        searchUsersButton.setOnClickListener(view -> {
            Intent searchUsersIntent = new Intent(AddTask.this, SearchForUsers.class);
            searchUsersIntent.putExtra("USERNAME", username);
            searchUsersIntent.putExtra("COLLABORATORS", "NA");
            startActivityForResult(searchUsersIntent, REQUEST_SELECT_USERS);
        });
        retrieveSelectedUsers();
    }

    private void createTask() {
        String taskTitle = titleEdit.getText().toString();
        String selectedCategory = category.getSelectedItem().toString();
        String collaborators; // String to store the collaborators' usernames

        if (selectedUsers == null) {
            collaborators = "NIL"; // If no selected users, set to "NIL"
        } else {
            // Combine the usernames of collaborators into a comma-separated string
            StringBuilder collaboratorsStringBuilder = new StringBuilder();
            for (User user : selectedUsers) {
                collaboratorsStringBuilder.append(user.getUsername()).append(",");
            }
            // Remove the trailing comma if there are collaborators
            collaborators = collaboratorsStringBuilder.toString().replaceAll(",$", "");
        }
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
                            Task newTask = new Task(username, taskTitle, selectedDate, taskId, false, 0, 0, selectedCategory, collaborators, false);
                            taskRef.child(taskId).setValue(newTask);
                            UpdateCount(username);
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
            }
        });
    }
    public interface TaskIdCallback {
        void onTaskIdGenerated(String taskId);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_USERS && resultCode == RESULT_OK) {
            // Retrieve the selected user data from SharedPreferences
            retrieveSelectedUsers();
        }
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
    }
    private void clearSelectedUsers() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("SELECTED_USERS");
        editor.apply();
    }



    private void UpdateCount(String username){
        DatabaseReference CountRef;
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {

                    int first = dataSnapshot.child("taskcount").getValue(Integer.class);
                    CountRef.child("taskcount").setValue(first + 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while fetching the data
                // ...
            }
        });

    }

}


