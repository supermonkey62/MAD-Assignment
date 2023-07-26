package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskDataHolder {
    private static TaskDataHolder instance;
    private List<Task> taskList;
    private DatabaseReference tasktRef, userRef;

    private TaskDataHolder() {
        taskList = new ArrayList<>();
        tasktRef = FirebaseDatabase.getInstance().getReference("Task");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public static synchronized TaskDataHolder getInstance() {
        if (instance == null) {
            instance = new TaskDataHolder();
        }
        return instance;
    }

    public void fetchUserTasks(String username, final TaskDataCallback callback) {
        taskList.clear();
        tasktRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Task task = eventSnapshot.getValue(Task.class);
                    if (task != null  && task.getUsername() != null && task.getUsername().equals(username)) {
                        taskList.add(task);
                    }
                }

                if (taskList.isEmpty()) {
                    Log.v("Task Details", "No items in the list");
                }
                // Invoke the callback method with the retrieved taskList
                callback.onTaskDataFetched(taskList);

                // Fetch collaborator tasks and add them to the taskList
                fetchCollaboratorTasks(username, callback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    private void fetchCollaboratorTasks(String username, TaskDataCallback callback) {
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String collaborators = user.getCollaboratedtasks();
                        if (!"NIL".equals(collaborators)) {
                            String[] collaboratorsArray = collaborators.split(",");
                            for (String taskId : collaboratorsArray) {
                                tasktRef.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Task collaboratorTask = snapshot.getValue(Task.class);
                                        if (collaboratorTask != null) {
                                            taskList.add(collaboratorTask);
                                        }
                                        // Check if all collaborator tasks have been fetched and invoke the callback
                                        if (taskList.size() == collaboratorsArray.length) {
                                            callback.onTaskDataFetched(taskList);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle the error case if the listener is canceled or fails to retrieve data
                                        // You can show an error message or handle it as per your requirements
                                    }
                                });
                            }
                        } else {
                            // If there are no collaborator tasks, invoke the callback
                            callback.onTaskDataFetched(taskList);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public interface TaskDataCallback {
        void onTaskDataFetched(List<Task> tasks);
    }
}



