package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemoveTasks extends AppCompatActivity {
    private Button deletebutton;
    private TextView taskname, cancelText;
    String selectedDate, username, tag, tasktitle;

    Boolean status;

    DatabaseReference userTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_tasks);
        Log.v("DeleteTask", "Entered Delete Task");
        cancelText = findViewById(R.id.canceldeletetext);
        selectedDate = getIntent().getStringExtra("DATE");
        username = getIntent().getStringExtra("USERNAME");
        tag = getIntent().getStringExtra("TAG");
        status = getIntent().getBooleanExtra("STATUS", false);
        tasktitle = getIntent().getStringExtra("TITLE");
        taskname = findViewById(R.id.deletetaskname);
        deletebutton = findViewById(R.id.deletetaskbutton);
        taskname.setText("Delete  " + tasktitle + "  ?");
        userTask = FirebaseDatabase.getInstance().getReference("Task");

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTask.child(tag).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                            }

                        });


            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
