package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MainpageViewHolder extends RecyclerView.ViewHolder {
    CheckBox task;


    TextView dateoftask;

    boolean status;
    String username, date, tag, title, type;


    MainpageViewHolder(View view) {
        super(view);
        task = view.findViewById(R.id.mainpagecheckbox);
        dateoftask = view.findViewById(R.id.mainpagetaskdatetodo);
    }
}
