package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {

    private Context context;
    private List<Task> taskList;
    private DatabaseReference userTask;
    private Task removedTask;

    float existingTimeSpent;

    public TodolistAdaptor(Context context, List<Task> eventList) {
        this.context = context;
        this.taskList = eventList;
        userTask = FirebaseDatabase.getInstance().getReference("Task");
    }

    @Override
    public int getItemViewType(int position) {
        Task task = taskList.get(position);
        String taskTitle = task.getTitle();
        return R.layout.item_view;
    }
    public void removeItem(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist, parent, false);
        TodoViewHolder holder = new TodoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.task.setText(task.getTitle());
        holder.username = task.getUsername();
        holder.dateoftask.setText(task.getDate());
        holder.date = task.getDate();
        holder.tag = task.getTag();
        holder.status = task.getStatus();
        holder.title = task.getTitle();
        holder.sessions = task.getSessions();
        holder.category = task.getCategory();
        holder.collaborators = task.getCollaborators();
        holder.collaboratorsTextView.setVisibility(View.INVISIBLE);
        holder.categoryTextView.setText(holder.category);

        if (task.getStatus() == false) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.task.setText(task.getTitle());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        if (task.getCollaborators() != null && !task.getCollaborators().equals("NIL")) {
            List<String> collaboratorsList = Arrays.asList(task.getCollaborators().split(","));
            StringBuilder collaboratorsStringBuilder = new StringBuilder();
            collaboratorsStringBuilder.append("Collab between " + holder.username + ", ");
            for (String collaborator : collaboratorsList) {
                collaboratorsStringBuilder.append(collaborator).append(", ");
            }

            // Remove the trailing ", " from the end
            collaboratorsStringBuilder.setLength(collaboratorsStringBuilder.length() - 2);

            // Set the text for the collaborators TextView
            holder.collaboratorsTextView.setText(collaboratorsStringBuilder.toString());
            holder.collaboratorsTextView.setVisibility(View.VISIBLE);
        } else {
            // No collaborators, hide the collaborators TextView
            holder.collaboratorsTextView.setVisibility(View.GONE);
        }





        holder.task.setOnCheckedChangeListener(null);
        holder.task.setChecked(task.getStatus());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setStatus(true);
                removedTask = task;
                userTask.child(holder.tag).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Task existingtask = dataSnapshot.getValue(Task.class);
                            existingTimeSpent = existingtask.getTimespent();
                            int existingsession = existingtask.getSessions();
                            String category = existingtask.getCategory();
                            Task updateTask = new Task(holder.username, holder.title, holder.date, holder.tag, true,existingTimeSpent,existingsession,category, holder.collaborators, false);
                            userTask.child(holder.tag).setValue(updateTask);
                            UpdateCount(holder.username);
                        } else {
                            Log.v("TaskCount", holder.tag + " does not exist.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
                    }
                });

                showUndoPopup();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateData(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }


    private void showUndoPopup() {
        Snackbar snackbar = Snackbar.make(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content),
                        "Task Completed", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (removedTask != null) {
                            removedTask.setStatus(false);
                            userTask.child(removedTask.getTag()).setValue(removedTask);
                            removedTask = null;
                        }
                    }
                });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT ||
                        event == Snackbar.Callback.DISMISS_EVENT_SWIPE ||
                        event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    if (removedTask != null) {
                        removedTask = null;
                    }
                }
            }
        });

        snackbar.show();
    }

    private void UpdateCount(String username){
        DatabaseReference CountRef;
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {

                    int first = dataSnapshot.child("completedtaskcount").getValue(Integer.class);
                    CountRef.child("completedtaskcount").setValue(first + 1);
                    Log.v("Count","+" + first +1);
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