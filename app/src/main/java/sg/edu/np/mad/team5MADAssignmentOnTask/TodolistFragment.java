package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TodolistFragment extends Fragment {
    private Switch todoswitch;
    private ImageView backhome;

    private Button taskanalysis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_todolist_fragmentholder, container, false);

        todoswitch = view.findViewById(R.id.todotaskswitch);
        backhome = view.findViewById(R.id.backhome);
        taskanalysis = view.findViewById(R.id.button3);
        String username = getActivity().getIntent().getStringExtra("USERNAME");
        taskanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent task = new Intent(getContext(),TaskAnalysis.class);
                task.putExtra("USERNAME",username);
                startActivity(task);
            }
        });

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish(); // Use requireActivity() to finish the activity
            }
        });

        todoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FragmentManager fragmentManager = getChildFragmentManager(); // Use getChildFragmentManager() for Fragments
                Fragment fragment = isChecked ? new DoneTasksFragment() : new TodoFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.todofragmentContainerView, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                String switchName = isChecked ? "To-Do Tasks" : "Done Tasks";
                todoswitch.setText(switchName);
            }
        });

        // Set the initial fragment based on the switch state
        boolean switchState = todoswitch.isChecked();
        Fragment initialFragment = switchState ? new DoneTasksFragment() : new TodoFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.todofragmentContainerView, initialFragment)
                .commit();

        String initialSwitchName = switchState ? "To-Do Tasks" : "Done Tasks";
        todoswitch.setText(initialSwitchName);

        return view;
    }
}
