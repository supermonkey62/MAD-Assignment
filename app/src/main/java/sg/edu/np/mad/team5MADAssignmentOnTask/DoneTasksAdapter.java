package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoneTasksAdapter extends RecyclerView.Adapter<ViewHolder> {

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
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.username = task.getUsername();
        holder.date = task.getDate();
        holder.tag = task.getTag();
        holder.status = task.getStatus();
        holder.box1View.setText(task.getTitle());
        holder.box2View.setText(task.getDate());

        if (task.getStatus() == true) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.box1View.setText(task.getTitle());
            holder.box2View.setText(task.getDate());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }



    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
