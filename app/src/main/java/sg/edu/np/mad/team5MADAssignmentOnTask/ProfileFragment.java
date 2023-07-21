package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {

    Button timer;

    Button analysis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        timer = rootView.findViewById(R.id.temptaskbutton);
        analysis = rootView.findViewById(R.id.button4);
        String username = getArguments().getString("USERNAME");
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todo = new Intent(getContext(),TodolistFragmentholder.class);
                todo.putExtra("USERNAME",username);
                startActivity(todo);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todo = new Intent(getContext(),TaskAnalysis.class);
                todo.putExtra("USERNAME",username);
                startActivity(todo);

            }
        });
        return rootView;


    }
    public static ProfileFragment newInstance(String username) {
       ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", username);
        fragment.setArguments(args);
        return fragment;
    }


}