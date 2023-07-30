package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    public String collaborators;
    CheckBox task;
    FloatingActionButton tasktimerlink;

    TextView dateoftask, collaboratorsTextView , categoryTextView;
    int sessions;

    boolean status;
    String username, date, tag,title,type,category;



    TodoViewHolder(View view) {
        super(view);
        task = view.findViewById(R.id.checkbox);
        tasktimerlink = view.findViewById(R.id.tasktimer);
        dateoftask = view.findViewById(R.id.taskdatetodo);
        collaboratorsTextView = view.findViewById(R.id.taskCollaboratorsText2);
        categoryTextView = view.findViewById(R.id.taskCategoryText);

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
        intent.putExtra("USERNAME", username);
        intent.putExtra("DATE", date);
        intent.putExtra("TAG", tag);
        intent.putExtra("STATUS", status);
        intent.putExtra("TITLE",title);
        intent.putExtra("TYPE",type);
        intent.putExtra("CATEGORY",category);
        intent.putExtra("COLLABORATORS", collaborators);


        context.startActivity(intent);
    }

    private void startActivity2() {

        Context context = itemView.getContext();
        Intent intent = new Intent(context, timer.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("DATE", date);
        intent.putExtra("TAG", tag);
        intent.putExtra("STATUS", status);
        intent.putExtra("TITLE",title);
        intent.putExtra("TYPE",type);
        intent.putExtra("CATEGORY",category);
        intent.putExtra("COLLABORATORS", collaborators);

        context.startActivity(intent);
    }


}




//public class TodoViewHolder extends RecyclerView.EventViewHolder {
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







