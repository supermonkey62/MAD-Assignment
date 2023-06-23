package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class CalendarViewHolder extends RecyclerView.ViewHolder {

    TextView dayDate, completed, incomplete;

    public CalendarViewHolder(View itemView){
        super(itemView);
        dayDate = itemView.findViewById(R.id.dayDate);
        completed = itemView.findViewById(R.id.completed);
        incomplete = itemView.findViewById(R.id.incomplete);

    }
}