package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.team5MADAssignmentOnTask.R;

public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView titlebox, datebox, timebox;

    String username,date,tag;
    public CardView cardView;
    boolean status;


    public EventViewHolder(View itemView) {
        super(itemView);
        titlebox = itemView.findViewById(R.id.titlebox);
        datebox = itemView.findViewById(R.id.datebox);
        timebox = itemView.findViewById(R.id.timebox);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
