package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DoneTasksFragment extends Fragment implements TaskDataHolder.TaskDataCallback {

    private RecyclerView taskRecyclerview;
    private DoneTasksAdapter taskadapter;
    private List<Task> tasklist;
    private DatabaseReference userTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_done_tasks, container, false);
        taskRecyclerview = view.findViewById(R.id.donerecycler);
        tasklist = new ArrayList<>();
        // Retrieve the username from the Intent
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("USERNAME");
        TaskDataHolder.getInstance().fetchUserTasks(username, this);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private Drawable archiveIcon = ContextCompat.getDrawable(getContext(), R.drawable.baseline_archive_24);
            private Drawable trashBinIcon = ContextCompat.getDrawable(getContext(), R.drawable.baseline_delete_24);
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
                if (direction == ItemTouchHelper.LEFT) {
                    taskadapter.removeItem(position);

                    // Remove the task from the Firebase database using the tag
                    userTask = FirebaseDatabase.getInstance().getReference("Task");
                    userTask.child(tag).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    taskadapter.removeItem(position);

                    Toast.makeText(getContext(), "Task archived successfully", Toast.LENGTH_SHORT).show();
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
                        int iconLeft = itemView.getLeft() + (int) (0.2 * itemWidth); // Icon appears sooner
                        int iconRight = iconLeft + intrinsicWidth;
                        int iconBottom = iconTop + intrinsicHeight;

                        // Set the bounds for the icon and draw it on the canvas
                        archiveIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        archiveIcon.draw(c);

                        // Change background color to green
                        paint.setColor(Color.GREEN);
                        c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom(), paint);
                    } else {
                        // Swiping to the left (delete)
                        int intrinsicWidth = trashBinIcon.getIntrinsicWidth();
                        int intrinsicHeight = trashBinIcon.getIntrinsicHeight();

                        // Calculate the position to draw the icon
                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconRight = itemView.getRight() - (int) (0.2 * itemWidth); // Icon appears sooner
                        int iconLeft = iconRight - intrinsicWidth;
                        int iconBottom = iconTop + intrinsicHeight;

                        // Set the bounds for the icon and draw it on the canvas
                        trashBinIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        trashBinIcon.draw(c);

                        // Change background color to red
                        paint.setColor(Color.RED);
                        c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
                    }
                }
            }



        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(taskRecyclerview);

        return view;


    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = tasks;
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        taskadapter = new DoneTasksAdapter(getContext(), tasklist);
        taskRecyclerview.setAdapter(taskadapter);
    }
}

//public class DoneTasksFragment extends Fragment implements TaskDataHolder.TaskDataCallback {
//    private RecyclerView taskRecyclerview;
//    private TodolistAdaptor taskadapter;
//    private List<Task> tasklist;
//
//    String selectedDate, username, tag, tasktitle;
//
//    Boolean status;
//
//    DatabaseReference userTask;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_done_tasks, container, false);
//        taskRecyclerview = view.findViewById(R.id.donerecycler);
//        tasklist = new ArrayList<>();
//        Intent intent = getActivity().getIntent();
//        username = intent.getStringExtra("USERNAME");
//        selectedDate = intent.getStringExtra("DATE");
//        tag = intent.getStringExtra("TAG");
//        status = intent.getBooleanExtra("STATUS", false);
//        tasktitle = intent.getStringExtra("TITLE");
//        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
//            @Override
//            public void onTaskDataFetched(List<Task> tasks) {
//                tasklist = tasks;
//                taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//                taskRecyclerview.setAdapter(new DoneTasksAdapter(getContext(), tasklist));
//            }
//        });
//
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                userTask = FirebaseDatabase.getInstance().getReference("Task");
//                userTask.child(tag).removeValue()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(taskRecyclerview);
//
//        return view;
//    }
//
//    @Override
//    public void onTaskDataFetched(List<Task> tasks) {
//        tasklist = tasks;
//        // Initialize taskadapter before setting it to the RecyclerView
//        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        taskRecyclerview.setAdapter(new DoneTasksAdapter(getContext(), tasklist));
//        taskadapter.notifyDataSetChanged();
//    }
//}