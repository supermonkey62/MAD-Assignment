package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoneTasksAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private List<Task> taskList;

    public DoneTasksAdapter(Context context, List<Task> eventList) {
        this.context = context;
        this.taskList = eventList;

    }



    public int getItemViewType(int position) {
        Task task = taskList.get(position);
        String tasktitle = task.getTitle();
        return R.layout.item_view;
    }



    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.username = task.getUsername();
        holder.date = task.getDate();
        holder.tag = task.getTag();
        holder.category = task.getCategory();
        holder.status = task.getStatus();
        holder.titlebox.setText(task.getTitle());
        holder.datebox.setText(task.getDate());
        holder.timebox.setText(holder.category);

        if (task.getStatus() == true) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.titlebox.setText(task.getTitle());
            holder.datebox.setText(task.getDate());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }



    }
    public void removeItem(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }







    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
