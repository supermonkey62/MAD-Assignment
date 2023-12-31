package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EditTask extends AppCompatActivity {
    EditText titleEdit;
    TextView taskDate, cancelText;
    String selectedDate, username, tag, title, collaborators, newCollaborators;
    Button deleteButton;
    List<User> selectedUsers;
    Boolean status;
    Spinner category;
    DatabaseReference userTask;
    float existingTimeSpent;
    private static final int REQUEST_SELECT_USERS = 1;

    private List<User> collaboratorUsersList = new ArrayList<>();

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
        newCollaborators = "";
        Log.v("Intented Collab", collaborators);
        status = getIntent().getBooleanExtra("STATUS", false);
        Log.v("TaskCount", username);
        Log.v("TaskCount", tag);
        taskDate = findViewById(R.id.taskdate);
        titleEdit = findViewById(R.id.titleEdit);
        Button editTask = findViewById(R.id.edittaskbutton);
        deleteButton = findViewById(R.id.deleteTaskButton);
        cancelText = findViewById(R.id.canceltasktext);
        Button editCollaboratorsButton = findViewById(R.id.editCollaboratorsButton);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String selectedUsersJson = preferences.getString("SELECTED_USERS", "");
        selectedUsers = new Gson().fromJson(selectedUsersJson, new TypeToken<List<User>>() {}.getType());
        category = findViewById(R.id.catspinneredit);
        taskDate.setText(selectedDate);
        titleEdit.setText(title);


        fetchCollaboratorUsers();

        String[] categories = {"Personal Tasks", "School Task", "Assignments", "Projects", "Errands and Shopping Tasks", "Health and Fitness Tasks"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        editTask.setOnClickListener(v -> {
            String selectedCategory = category.getSelectedItem().toString();
            String taskTitle = titleEdit.getText().toString();
            userTask = FirebaseDatabase.getInstance().getReference("Task");

            userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Task existingTask = dataSnapshot.getValue(Task.class);
                        existingTimeSpent = existingTask.getTimespent();
                        int existingSession = existingTask.getSessions();
                        String category = existingTask.getCategory();
                        Log.v("Username", tag);

                        if (collaboratorUsersList.isEmpty() && selectedUsers == null) {
                            // If there are no collaborators and no selected users,
                            // directly update the task with new data and empty collaborators
                            Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent, existingSession, selectedCategory, "NIL", false);
                            userTask.child(tag).setValue(newTask);
                            finish();
                        }
                        else {
                            if (newCollaborators.equals("NIL")) {
                                if (selectedUsers != null) {
                                    updateCollaboratorsTasks();
                                }

                                Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent, existingSession, selectedCategory, collaborators, false);
                                userTask.child(tag).setValue(newTask);
                                finish();
                            }
                            else {
                                Log.v("newCollaber", newCollaborators);
                                updateCollaboratorsTasks();
                                Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent, existingSession, category, newCollaborators, false);
                                userTask.child(tag).setValue(newTask);
                                Log.v("newCollab", newCollaborators);
                                finish();
                            }
                        }
                    } else {
                        Log.v("TaskCount", tag + " does not exist.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
                // ... Rest of the code ...
            });
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
        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Task");
        taskRef.child(tag).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditTask.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        // Delete the taskId from collaborators' collaboratedtasks
                        updateCollaboratorsTasksOnDelete(tag);
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
        Log.d("Selected Users", selectedUsers != null ? selectedUsers.toString() : "null");

        if (selectedUsers == null || selectedUsers.isEmpty()) {
            // If selectedUsers is null or empty, set collaborators to "NIL"
            newCollaborators = "NIL";
        } else {
            // Combine the usernames of collaborators into a comma-separated string
            StringBuilder collaboratorsStringBuilder = new StringBuilder();
            for (User user : selectedUsers) {
                collaboratorsStringBuilder.append(user.getUsername()).append(",");
            }
            // Remove the trailing comma if there are collaborators
            newCollaborators = collaboratorsStringBuilder.toString().replaceAll(",$", "");
        }
    }

    private void clearSelectedUsers() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("SELECTED_USERS");
        editor.apply();
    }

    private void updateCollaboratorsTasks() {
        Log.v("updateCollaborators", "Entering updateCollaboratorsTasks()");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        if (!collaboratorUsersList.isEmpty()) {
            Log.v("updateCollaborators", "Entering if portion");
            for (User collaboratorUser : collaboratorUsersList) {
                String collaboratorUsername = collaboratorUser.getUsername();
                Log.v("Collaborator username", collaboratorUsername);

                // Check if the collaborator exists in selectedUsers list
                if (!selectedUsers.contains(collaboratorUser)) {
                    // If the collaborator is not in selectedUsers, remove the taskId from the collaboratedtasks
                    String currentCollaboratedTasks = collaboratorUser.getCollaboratedtasks();
                    List<String> collaboratedTasksList = new ArrayList<>(Arrays.asList(currentCollaboratedTasks.split(",")));
                    collaboratedTasksList.remove(tag);
                    String updatedCollaboratedTasks = TextUtils.join(",", collaboratedTasksList);
                    if (updatedCollaboratedTasks.isEmpty()) {
                        updatedCollaboratedTasks = "NIL";
                    }

                    // Update the collaborator's collaborated tasks in the database
                    collaboratorUser.setCollaboratedtasks(updatedCollaboratedTasks);
                    usersRef.child(collaboratorUsername).child("collaboratedtasks").setValue(updatedCollaboratedTasks);

                    // Update the collaborators field of the Task object
                    collaborators = TextUtils.join(",", selectedUsers);
                }
            }
        }

        // Now, we need to handle selectedUsers regardless of collaboratorUsersList size
        for (User selectedUser : selectedUsers) {
            String currentCollaboratedTasks = selectedUser.getCollaboratedtasks();
            Log.v("collab string", selectedUser.getUsername() + ", " + currentCollaboratedTasks);

            // If the current collaborated tasks are "NIL," set to the new tag
            if ("NIL".equals(currentCollaboratedTasks)) {
                // Update the collaborator's collaborated tasks in the database
                selectedUser.setCollaboratedtasks(tag);
                usersRef.child(selectedUser.getUsername()).child("collaboratedtasks").setValue(tag);
            } else {
                List<String> collaboratedTasksList = new ArrayList<>(Arrays.asList(currentCollaboratedTasks.split(",")));

                // Check if the tag already exists in the collaboratedTasksList
                if (!collaboratedTasksList.contains(tag)) {
                    // If the tag doesn't exist, add it to the list
                    collaboratedTasksList.add(tag);
                    // Convert the list back to a comma-separated string
                    String updatedCollaboratedTasks = TextUtils.join(",", collaboratedTasksList);
                    // Update the collaborator's collaborated tasks in the database
                    selectedUser.setCollaboratedtasks(updatedCollaboratedTasks);
                    usersRef.child(selectedUser.getUsername()).child("collaboratedtasks").setValue(updatedCollaboratedTasks);
                }
                // If the tag already exists in the collaboratedTasksList, no changes needed
            }
        }

        // Finally, update the collaborators field of the Task object
        if (selectedUsers.isEmpty()) {
            collaborators = "NIL";
        } else {
            collaborators = TextUtils.join(",", selectedUsers);
        }
    }


    private void fetchCollaboratorUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the collaborators string is "NIL"
        if (!"NIL".equals(collaborators)) {
            String[] collaboratorsArray = collaborators.split(","); // Split the collaborators string

            // Loop through each collaborator username and fetch the corresponding user object
            for (String collaboratorUsername : collaboratorsArray) {
                usersRef.child(collaboratorUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User collaboratorUser = dataSnapshot.getValue(User.class);
                            if (collaboratorUser != null) {
                                // Add the collaborator user to the list
                                collaboratorUsersList.add(collaboratorUser);
                                Log.v("Checking initial collaborating users", collaboratorUsername);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("EditTask", "Error fetching collaborator user data: " + databaseError.getMessage());
                    }
                });
            }
        }

        else {
            Log.v("fetchCollaboratorUsers", collaborators);
        }
    }

    private void updateCollaboratorsTasksOnDelete(String taskId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        for (User collaboratorUser : collaboratorUsersList) {
            String currentCollaboratedTasks = collaboratorUser.getCollaboratedtasks();
            List<String> collaboratedTasksList = new ArrayList<>(Arrays.asList(currentCollaboratedTasks.split(",")));
            collaboratedTasksList.remove(taskId);
            String updatedCollaboratedTasks = TextUtils.join(",", collaboratedTasksList);
            if (updatedCollaboratedTasks.isEmpty()) {
                updatedCollaboratedTasks = "NIL";
            }

            collaboratorUser.setCollaboratedtasks(updatedCollaboratedTasks);
            usersRef.child(collaboratorUser.getUsername()).child("collaboratedtasks").setValue(updatedCollaboratedTasks);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("EditTask", "OnDestroy");
        clearSelectedUsers();
    }

}

