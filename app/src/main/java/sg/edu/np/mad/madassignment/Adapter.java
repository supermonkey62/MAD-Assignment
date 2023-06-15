package sg.edu.np.mad.madassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<Event> eventList;

    public Adapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;

    }

    @Override
    public int getItemViewType(int position) {
        Event event = eventList.get(position);
        String task = event.getTitle();
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
        Event event = eventList.get(position);
        holder.box1View.setText(event.getDate());
        holder.box2View.setText(event.getDate());

        // Do this: From date derive day, and also take the day date from the full date

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
