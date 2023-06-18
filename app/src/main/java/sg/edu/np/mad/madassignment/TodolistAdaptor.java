package sg.edu.np.mad.madassignment;

import static java.lang.Boolean.TRUE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {

    private Context context;
    private List<Task> taskList;
    private DatabaseReference userTask;
    private Task removedTask;

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

    @Override
    public int getItemCount() {
        return taskList.size();
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
}


//public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
//
//    private Context context;
//    private List<Task> taskList;
//
//    DatabaseReference userTask;
//
//    private Task removedTask;
//
//
//    public TodolistAdaptor(Context context, List<Task> eventList) {
//        this.context = context;
//        this.taskList = eventList;
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Task task = taskList.get(position);
//        String tasktitle = task.getTitle();
//        return R.layout.item_view;
//    }
//
//    @Override
//    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist,parent,false);
//        TodoViewHolder holder = new TodoViewHolder(view);
//        return holder;
//    }
//
//
//    @Override
//    public void onBindViewHolder(TodoViewHolder holder, int position) {
//        Task task = taskList.get(position);
//        holder.task.setText(task.getTitle());
//        holder.username = task.getUsername();
//        holder.dateoftask.setText(task.getDate());
//        holder.date = task.getDate();
//        holder.tag = task.getTag();
//        holder.status = task.getStatus();
//        holder.title = task.getTitle();
//        holder.type = task.getType();
//
//       userTask = FirebaseDatabase.getInstance().getReference("Task");
//       if (task.getStatus() == false) {
//            holder.itemView.setVisibility(View.VISIBLE);
//            holder.task.setText(task.getTitle());
//       } else {
//            holder.itemView.setVisibility(View.GONE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        }
//        holder.task.setOnCheckedChangeListener(null);
//        holder.task.setChecked(task.getStatus());
//       holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                task.setStatus(true);
//                removedTask = task;
//                showUndoPopup(removedTask);
//                userTask.child(holder.tag).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                   public void onDataChange(DataSnapshot dataSnapshot) {
//                       if (dataSnapshot.exists()) {
//                           Task updateTask = new Task(holder.username, holder.title, holder.type, holder.date, holder.tag, true);
//                            userTask.child(holder.tag).setValue(updateTask);
//                      }
//                       else {
//                          Log.v("TaskCount", holder.tag + " does not  exists.");
//                      }
//                   }
//                 @Override
//                   public void onCancelled(DatabaseError databaseError) {
//                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
//                   }
//               });
//
//          }
//        });
//    }
//
//
//
//
//    @Override
//    public int getItemCount() {
//        return taskList.size();
//    }
//
//    private void showUndoPopup(Task removedTask) {
//
//        Snackbar snackbar = Snackbar.make(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Task removed", Snackbar.LENGTH_LONG)
//                .setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        removedTask.setStatus(false);
//                        notifyDataSetChanged();
//                    }
//                });
//
//        snackbar.show();
//    }
//
//}


//public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
//    private List<Task> todolist;
//    private Context context;
//
//    public TodolistAdaptor(Context context,List<Task> todolist){
//        this.context = context;
//        this.todolist = todolist;
//    }
//    @Override
//    public int getItemViewType(int position) {
//        Task task = todolist.get(position);
//        String tasktitle = task.getTitle();
//        return R.layout.item_view;
//    }
//
//
//    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist,parent,false);
//        TodoViewHolder holder = new TodoViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(TodoViewHolder holder, int position) {
//        Task item = todolist.get(position);
//        String taskname = item.getTitle();
//        holder.task.setText(item.getTitle());
////        holder.task.setChecked(item.getStatus());
//        holder.tasktimerlink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Timer Options")
//                        .setMessage("Choose an option:")
//                        .setPositiveButton("Countdown Timer", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Perform action for Option 1
//                                Intent intent = new Intent(context,timer.class);
//                                intent.putExtra("Task Name" ,taskname);
//                                context.startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Pomodoro Timer", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Perform action for Option 2 (e.g., start Activity 2)
//                                Intent intent = new Intent(context, PomodoroTimer.class);
//                                context.startActivity(intent);
//                            }
//                        })
//                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//
//                        .setCancelable(true)
//                        .show();
//
//            }
//        });
//
////        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                item.setStatus(isChecked);
////
////            }
////        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return todolist.size();
//    }
//
//
//}
