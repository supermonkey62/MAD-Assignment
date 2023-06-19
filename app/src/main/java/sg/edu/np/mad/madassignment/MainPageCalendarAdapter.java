package sg.edu.np.mad.madassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
            String[] separatetrue = counts[1].trim().split(" ");
            String[] separatefalse = counts[0].trim().split(" ");
            holder.completed.setText(separatetrue[0] + " Incomplete");
            holder.incomplete.setText(separatefalse[0] + " Completed");
        } else if (counts.length == 1) {
            String[] separate = counts[0].trim().split(" ");
            if (separate[1].equals("true")) {
                holder.incomplete.setText(separate[0] + " Inomplete");
                holder.completed.setText("");
            } else {
                holder.incomplete.setText(separate[0] + " Completed");
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




