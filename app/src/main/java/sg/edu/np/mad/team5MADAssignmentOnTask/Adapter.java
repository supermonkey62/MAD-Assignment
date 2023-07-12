package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    final Context context;
    final List<Task> taskList;



    public Adapter(Context context, List<Task> eventList) {
        this.context = context;
        this.taskList = eventList;

    }

    @Override
    public int getItemViewType(int position) {
        Task task = taskList.get(position);
        return R.layout.item_view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(viewType, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.box1View.setText(task.getTitle());
        holder.box2View.setText(task.getType());
        holder.username = task.getUsername();
        holder.date = task.getDate();
        holder.tag = task.getTag();
        holder.status = task.getStatus();
        if(holder.status == true)
        {
            holder.statusView.setText("Completed");
        }
        else{
            holder.statusView.setText(("Incomplete"));
        }
        holder.title = task.getTitle();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
