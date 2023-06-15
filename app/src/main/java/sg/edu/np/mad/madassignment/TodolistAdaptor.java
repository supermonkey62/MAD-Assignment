package sg.edu.np.mad.madassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
    private ArrayList<TodoModel> taskList;

    public TodolistAdaptor(ArrayList<TodoModel> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        TodoModel task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(ArrayList<TodoModel> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }
}

//public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
//    private ArrayList<TodoModel> todolist;
//    private TodoList activity;
//
//    public TodolistAdaptor(TodoList activity) {
//        this.activity = activity;
//        this.todolist = new ArrayList<>();
//    }
//
//    @NonNull
//    @Override
//    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist, parent, false);
//        return new TodoViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
//        TodoModel item = todolist.get(position);
//        holder.task.setText(item.getTask());
//        holder.task.setChecked(item.isStatus());
//        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                item.setStatus(isChecked);
//            }
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return todolist.size();
//    }
//
//    public void setTaskListFromDatabase(ArrayList<TodoModel> taskList) {
//        this.todolist = taskList;
//        notifyDataSetChanged();
//    }
//}


//public class TodolistAdaptor extends RecyclerView.Adapter<TodoViewHolder> {
//    private ArrayList<TodoModel> todolist;
//    private TodoList activity;
//
//    public TodolistAdaptor(TodoList activity){
//        this.todolist = todolist;
//    }
//    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklist,parent,false);
//        TodoViewHolder holder = new TodoViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(TodoViewHolder holder, int position) {
//        TodoModel item = todolist.get(position);
//        holder.task.setText(item.getTask());
//        holder.task.setChecked(item.isStatus());
//
//
//    }
//
//    public void setTask(ArrayList<TodoModel> todolist){
//        if (todolist != null) {
//            this.todolist = todolist;
//        } else {
//            this.todolist = new ArrayList<>();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (todolist == null) {
//            return 0; // or return a default value
//        }
//        return todolist.size();
//    }
//
//
//
//
//}
