package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class archivetasks extends AppCompatActivity implements TaskDataHolder.TaskDataCallback{

    private RecyclerView recyclerView;

    private archivetaskadaptor archivetaskadaptor;

    private List<Task> tasklist;
    private DatabaseReference userTask;

    float existingTimeSpent;

    private ImageButton backbttn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivetasks);
        recyclerView = findViewById(R.id.ArchivetasksRecyclerView);
        backbttn = findViewById(R.id.btnGoBack2);
        tasklist = new ArrayList<>();
        String username = getIntent().getStringExtra("USERNAME");
        TaskDataHolder.getInstance().fetchUserTasks(username, this);

        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private Drawable archiveIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_unarchive_24);
            private Drawable trashBinIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_delete_24);

            //            private int iconMargin = getResources().getDimensionPixelSize(R.dimen.icon_margin);
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task swipedTask = tasklist.get(position);
                String tag = swipedTask.getTag();
                String taskTitle = swipedTask.getTitle();
                String selectedDate = swipedTask.getDate();
                Boolean status = swipedTask.getStatus();
                String collaborators = swipedTask.getCollaborators();
                if (direction == ItemTouchHelper.LEFT) {
                    archivetaskadaptor.removeItem(position);
                    userTask = FirebaseDatabase.getInstance().getReference("Task");
                    userTask.child(tag).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else if (direction == ItemTouchHelper.RIGHT) {
                    archivetaskadaptor.removeItem(position);
                    userTask = FirebaseDatabase.getInstance().getReference("Task");

                    userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Task existingtask = dataSnapshot.getValue(Task.class);
                                existingTimeSpent = existingtask.getTimespent();
                                int existingsession = existingtask.getSessions();
                                String category = existingtask.getCategory();
                                Log.v("Username", tag);
                                Task newTask = new Task(username, taskTitle, selectedDate, tag, status, existingTimeSpent, existingsession, category, collaborators, false);
                                userTask.child(tag).setValue(newTask);


                            } else {
                                Log.v("TaskCount", tag + " does not  exists.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.v("LoginPage", "Error: " + databaseError.getMessage());
                        }
                    });


                    Toast.makeText(getApplicationContext(), "Task unarchived successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getHeight();
                int itemWidth = itemView.getWidth();
                Paint paint = new Paint();

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 0) {
                        // Swiping to the right (archive)
                        int intrinsicWidth = archiveIcon.getIntrinsicWidth();
                        int intrinsicHeight = archiveIcon.getIntrinsicHeight();

                        // Calculate the position to draw the icon
                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconLeft = itemView.getLeft() + (int) (0.2 * itemWidth);
                        int iconRight = iconLeft + intrinsicWidth;
                        int iconBottom = iconTop + intrinsicHeight;
                        // Change background color to green
                        paint.setColor(0xFFCCFFCC);
                        c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom(), paint);

                        // Set the bounds for the icon and draw it on the canvas
                        archiveIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        archiveIcon.draw(c);

                    } else {
                        // Swiping to the left (delete)
                        int intrinsicWidth = trashBinIcon.getIntrinsicWidth();
                        int intrinsicHeight = trashBinIcon.getIntrinsicHeight();

                        // Calculate the position to draw the icon
                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconRight = itemView.getRight() - (int) (0.2 * itemWidth); // Icon appears sooner
                        int iconLeft = iconRight - intrinsicWidth;
                        int iconBottom = iconTop + intrinsicHeight;
                        // Change background color to red
                        paint.setColor(0xFFFFCDD2);
                        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);

                        // Set the bounds for the icon and draw it on the canvas
                        trashBinIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        trashBinIcon.draw(c);


                    }
                }
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



    }





    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = new ArrayList<>();
        for (Task task : tasks) {
            if (task.archive == true) {
                tasklist.add(task);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        archivetaskadaptor = new archivetaskadaptor(getApplicationContext(),tasklist);
        recyclerView.setAdapter(archivetaskadaptor);


    }


}
