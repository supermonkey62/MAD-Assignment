package sg.edu.np.mad.madassignment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    CheckBox task;
    FloatingActionButton tasktimerlink;


    TodoViewHolder(View view){
        super(view);
        task = view.findViewById(R.id.checkbox);
        tasktimerlink = view.findViewById(R.id.tasktimer);

    }

    public CheckBox getTaskCheckBox() {
        return task;
    }






}
