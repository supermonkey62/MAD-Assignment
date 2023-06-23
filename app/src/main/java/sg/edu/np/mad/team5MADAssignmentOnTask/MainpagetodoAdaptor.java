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

import java.util.List;

public class MainpagetodoAdaptor extends RecyclerView.Adapter<MainpageViewHolder> {
    private Context context;
    private List<Task> taskList;
    private DatabaseReference userTask;
    private Task removedTask;

    public MainpagetodoAdaptor(Context context, List<Task> eventList) {
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


    @Override
    public MainpageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainpagetodo, parent, false);
        MainpageViewHolder holder = new MainpageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainpageViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task.setText(task.getTitle());
        holder.username = task.getUsername();
        holder.dateoftask.setText(task.getDate());
        holder.date = task.getDate();
        holder.tag = task.getTag();
        holder.status = task.getStatus();
        holder.title = task.getTitle();
        holder.type = task.getType();
        if (task.getStatus() == false) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.task.setText(task.getTitle());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
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
                            Task updateTask = new Task(holder.username, holder.title, holder.type, holder.date, holder.tag, true);
                            userTask.child(holder.tag).setValue(updateTask);
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

    private void showUndoPopup() {
        Snackbar snackbar = Snackbar.make(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content),
                        "Task removed", Snackbar.LENGTH_LONG)
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


    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
