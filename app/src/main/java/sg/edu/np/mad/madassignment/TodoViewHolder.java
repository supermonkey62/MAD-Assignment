package sg.edu.np.mad.madassignment;

import android.view.View;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    CheckBox task;

    TodoViewHolder(View view){
        super(view);
        task = view.findViewById(R.id.checkbox);
    }

    public void bind(TodoModel task) {
    }
}
