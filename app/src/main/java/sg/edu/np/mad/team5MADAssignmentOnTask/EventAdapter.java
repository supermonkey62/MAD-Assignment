package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;

    private List<Event> eventList;

    private SelectListener listener;

    private DatabaseReference userEvent;


    public EventAdapter(Context context, List<Event> eventList, SelectListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
        userEvent = FirebaseDatabase.getInstance().getReference("Event");

    }

    @Override
    public int getItemViewType(int position) {
        Event event = eventList.get(position);
        return R.layout.item_view;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(viewType, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.titlebox.setText(event.getTitle());

        // Convert the start and end dates to the desired format (e.g., "24 Jul")
        String formattedStartDate = formatDate(event.getStartDate());
        String formattedEndDate = formatDate(event.getEndDate());

        // Set the date box with the formatted start and end dates
        holder.datebox.setText(formattedStartDate + " - " + formattedEndDate);
        holder.timebox.setText(event.getStartTime() + " - " + event.getEndTime());
        holder.username = event.getUsername();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the onTaskItemClick method of the click listener with the clicked task
                listener.onItemClicked(eventList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private String formatDate(String dateString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        try {
            Date date = inputDateFormat.parse(dateString);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString; // Return the original date string if parsing fails
    }


}
