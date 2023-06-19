package sg.edu.np.mad.madassignment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView box1View, box2View;

    String username,date,tag,title;

    boolean status;
    FloatingActionButton expandButton;
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;

    public ViewHolder(View itemView) {
        super(itemView);
        box1View = itemView.findViewById(R.id.titlebox);
        box2View = itemView.findViewById(R.id.box2);
        expandButton = itemView.findViewById(R.id.expandtaskbutton);
        editButton = itemView.findViewById(R.id.edittaskbutton);
        deleteButton = itemView.findViewById(R.id.deletetaskbutton);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editButton.getVisibility() == View.VISIBLE) {
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                } else {
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start the EditTask activity
                        Context context = v.getContext();
                        Intent toEditTask = new Intent(context, EditTask.class);
                        toEditTask.putExtra("USERNAME", username);
                        toEditTask.putExtra("DATE", date);
                        toEditTask.putExtra("TAG", tag);
                        toEditTask.putExtra("STATUS", status);
                        context.startActivity(toEditTask);
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("Moving to delete task", "Move to delete task");
                        Context context = v.getContext();
                        Intent toDeleteTask = new Intent(context, RemoveTasks.class);
                        toDeleteTask.putExtra("USERNAME", username);
                        toDeleteTask.putExtra("DATE", date);
                        toDeleteTask.putExtra("TAG", tag);
                        toDeleteTask.putExtra("STATUS", status);
                        toDeleteTask.putExtra("TITLE",title);
                        context.startActivity(toDeleteTask);

                    }
                });
            }
        });


    }
}
