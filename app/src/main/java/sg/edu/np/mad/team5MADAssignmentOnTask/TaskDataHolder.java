package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;

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
    private DatabaseReference tasktRef;

    private TaskDataHolder() {
        taskList = new ArrayList<>();
        tasktRef = FirebaseDatabase.getInstance().getReference("Task");
    }

    public static synchronized TaskDataHolder getInstance() {
        if (instance == null) {
            instance = new TaskDataHolder();
        }
        return instance;
    }

    public void fetchUserTasks(String username, final TaskDataCallback callback) {
        tasktRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Task task = eventSnapshot.getValue(Task.class);
                    if (task != null  && task.getUsername() != null && task.getUsername().equals(username)) {
                        taskList.add(task);
                    }
                    else{
                        Log.v("TaskDataHolderError", "Task is null or username is null");
                    }
                }

                if (taskList.isEmpty()) {
                    Log.v("Task Details", "No items in the list");
                }
                // Invoke the callback method with the retrieved taskList
                callback.onTaskDataFetched(taskList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public interface TaskDataCallback {
        void onTaskDataFetched(List<Task> tasks);
    }
}


