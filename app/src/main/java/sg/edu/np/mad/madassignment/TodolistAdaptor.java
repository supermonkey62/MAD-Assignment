package sg.edu.np.mad.madassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        holder.task.setText(item.getTitle());
        holder.task.setChecked(item.getStatus());
    }


    @Override
    public int getItemCount() {
        return todolist.size();
    }


}
