package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
public class ViewHolder extends RecyclerView.ViewHolder {
    TextView box1View, box2View;

    String username,date,tag;
    public CardView cardView;
    boolean status;


    public ViewHolder(View itemView) {
        super(itemView);
        box1View = itemView.findViewById(R.id.titlebox);
        box2View = itemView.findViewById(R.id.box2);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
