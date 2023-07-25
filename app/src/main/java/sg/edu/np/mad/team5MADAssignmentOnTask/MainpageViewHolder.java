package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainpageViewHolder extends RecyclerView.ViewHolder {
    public String collaborators;
    CheckBox task;
    CardView cardView;


    TextView categoryText, collaboratorsText;

    boolean status;
    String username, date, tag, title, type;


    MainpageViewHolder(View view) {
        super(view);
        task = view.findViewById(R.id.mainpagecheckbox);
        categoryText = view.findViewById(R.id.taskCategoryText);
        collaboratorsText = view.findViewById(R.id.taskCollaboratorsText);
        cardView = itemView.findViewById(R.id.main_container2);

    }
}
