package sg.edu.np.mad.madassignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment implements TaskDataHolder.TaskDataCallback {
    private RecyclerView taskRecyclerview;
    private TodolistAdaptor taskadapter;
    private List<Task> tasklist;
    private String date;

    private FloatingActionButton addtask;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance() {
        return new TodoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        taskRecyclerview = view.findViewById(R.id.FragtasksRecyclerView);
        tasklist = new ArrayList<>();
        addtask = view.findViewById(R.id.fragaddtask);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(today);
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("USERNAME");
        TaskDataHolder.getInstance().fetchUserTasks(username, new TaskDataHolder.TaskDataCallback() {
            @Override
            public void onTaskDataFetched(List<Task> tasks) {
                tasklist = tasks;
                taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                taskRecyclerview.setAdapter(new TodolistAdaptor(getContext(), tasklist));


            }
        });

        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTask.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("DATE", date);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        tasklist = tasks;

        taskRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        taskRecyclerview.setAdapter(new TodolistAdaptor(getContext(), tasklist));
        taskadapter.notifyDataSetChanged();
    }
}
