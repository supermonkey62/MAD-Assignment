package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoneTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneTasksFragment extends Fragment implements TaskDataHolder.TaskDataCallback {
    private RecyclerView taskRecyclerview;
    private TodolistAdaptor taskadapter;
    private List<Task> tasklist;
    private String date;

    private FloatingActionButton addtask;

    public DoneTasksFragment() {
        // Required empty public constructor
    }

    public static DoneTasksFragment newInstance() {
        return new DoneTasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done_tasks, container, false);
        taskRecyclerview = view.findViewById(R.id.donerecycler);
        tasklist = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("USERNAME");
        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                tasklist = tasks;
                taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                taskRecyclerview.setAdapter(new DoneTasksAdapter(getContext(), tasklist));
            }
        });

        return view;
    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = tasks;
        // Initialize taskadapter before setting it to the RecyclerView
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        taskRecyclerview.setAdapter(new DoneTasksAdapter(getContext(), tasklist));
        taskadapter.notifyDataSetChanged();
    }
}