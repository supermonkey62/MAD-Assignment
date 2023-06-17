package sg.edu.np.mad.madassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
    private List<Task> todolist;
    private TodoList activity;

    public TodolistAdaptor(TodoList activity,List<Task> todolist){
        this.activity = activity;
        this.todolist = todolist;
    }
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist,parent,false);
        TodoViewHolder holder = new TodoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Task item = todolist.get(position);
        String taskname = item.getTitle();
        holder.task.setText(item.getTitle());
        holder.task.setChecked(item.getStatus());
        holder.tasktimerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Timer Options")
                        .setMessage("Choose an option:")
                        .setPositiveButton("Countdown Timer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform action for Option 1
                                Intent intent = new Intent(activity,timer.class);
                                intent.putExtra("Task Name" ,taskname);
                                activity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Pomodoro Timer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform action for Option 2 (e.g., start Activity 2)
                                Intent intent = new Intent(activity, PomodoroTimer.class);
                                activity.startActivity(intent);
                            }
                        })
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })

                        .setCancelable(true)
                        .show();

            }
        });

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setStatus(isChecked);

            }
        });

    }


    @Override
    public int getItemCount() {
        return todolist.size();
    }


}
