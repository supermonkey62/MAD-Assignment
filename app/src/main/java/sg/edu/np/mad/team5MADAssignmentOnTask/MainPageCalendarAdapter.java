package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.team5MADAssignmentOnTask.CalendarViewHolder;
import sg.edu.np.mad.team5MADAssignmentOnTask.R;

public class MainPageCalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private Context context;
    private List<String> dateList;
    private List<String> taskCountList;

    public MainPageCalendarAdapter(Context context, List<String> dateList, List<String> taskCountList) {
        this.context = context;
        this.dateList = dateList;
        this.taskCountList = taskCountList;
    }

    @Override
    public int getItemViewType(int position) {
        // Customize this as per your needs
        return R.layout.main_page_calendar_item_view;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(viewType, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        String date = dateList.get(position);
        String taskCount = taskCountList.get(position);

        holder.dayDate.setText(date);

        String[] counts = taskCount.split(",");

        if (counts.length >= 2) {
            String[] separate1 = counts[1].trim().split(" ");
            String[] separate2 = counts[0].trim().split(" ");
            if (separate1[1].equals("true")){
                holder.completed.setText(separate1[0] + " Completed");
                holder.completed.setTextColor(Color.BLACK); // Set text color to black
                holder.incomplete.setText(separate2[0] + " Incomplete");
                holder.incomplete.setTextColor(Color.BLACK); // Set text color to black
            }
            else {
                holder.incomplete.setText(separate1[0] + " Incomplete");
                holder.incomplete.setTextColor(Color.BLACK); // Set text color to black
                holder.completed.setText(separate2[0] + " Completed");
                holder.completed.setTextColor(Color.BLACK); // Set text color to black
            }
        } else if (counts.length == 1) {
            String[] separate = counts[0].trim().split(" ");
            if (separate[1].equals("true")) {
                holder.incomplete.setText(separate[0] + " Completed");
                holder.incomplete.setTextColor(Color.BLACK); // Set text color to black
                holder.completed.setText("");
            } else {
                holder.incomplete.setText(separate[0] + " Incomplete");
                holder.incomplete.setTextColor(Color.BLACK); // Set text color to black
                holder.completed.setText("");
            }
        } else {
            holder.completed.setText("");
            holder.incomplete.setText("");
        }
    }


    @Override
    public int getItemCount() {
        return dateList.size();
    }
}




