package sg.edu.np.mad.madassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    CheckBox task;
    FloatingActionButton tasktimerlink;

    TextView dateoftask;

    boolean status;
    String username, date, tag,title,type;

    FloatingActionButton expandButton;
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;


    TodoViewHolder(View view) {
        super(view);
        task = view.findViewById(R.id.checkbox);
        tasktimerlink = view.findViewById(R.id.tasktimer);
        expandButton = itemView.findViewById(R.id.expandtaskbutton1);
        editButton = itemView.findViewById(R.id.edittaskbutton1);
        deleteButton = itemView.findViewById(R.id.deletetaskbutton1);
        dateoftask = view.findViewById(R.id.taskdatetodo);


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
                        Context context = v.getContext();
                        Intent toEditTask = new Intent(context, EditTask.class);
                        toEditTask.putExtra("USERNAME", username);
                        toEditTask.putExtra("DATE", date);
                        toEditTask.putExtra("TAG", tag);
                        toEditTask.putExtra("STATUS", status);
                        context.startActivity(toEditTask);
                    }
                });
            }
        });


        tasktimerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
    }

    public CheckBox getTaskCheckBox() {
        return task;
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Pomodoro Timer", "Countdown Timer", "Cancel"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                startActivity1();
                                break;
                            case 1:
                                startActivity2();
                                break;
                            case 2:

                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void startActivity1() {

        Context context = itemView.getContext();
        Intent intent = new Intent(context, PomodoroTimer.class);
        intent.putExtra("TITLE", task.getText());
        context.startActivity(intent);
    }

    private void startActivity2() {

        Context context = itemView.getContext();
        Intent intent = new Intent(context, timer.class);
        intent.putExtra("TTITLE", task.getText());
        context.startActivity(intent);
    }


}




//public class TodoViewHolder extends RecyclerView.ViewHolder {
//    CheckBox task;
//    FloatingActionButton tasktimerlink;
//
//
//    TodoViewHolder(View view){
//        super(view);
//        task = view.findViewById(R.id.checkbox);
//        tasktimerlink = view.findViewById(R.id.tasktimer);
//
//
//
//    }
//
//    public CheckBox getTaskCheckBox() {
//        return task;
//    }







