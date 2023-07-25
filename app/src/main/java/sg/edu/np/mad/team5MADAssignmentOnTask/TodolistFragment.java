package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TodolistFragment extends Fragment {
    private Switch todoswitch;
    private ImageView backhome,ham;

    private Button taskanalysis;

    String username;

    Context context = getContext();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_todolist_fragmentholder, container, false);
        Context context = view.getContext();

        todoswitch = view.findViewById(R.id.todotaskswitch);
        backhome = view.findViewById(R.id.backhome);
        ham = view.findViewById(R.id.Todohamburg);
        String username = getActivity().getIntent().getStringExtra("USERNAME");


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });

        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TodolistFragment", "showPopup() method called");
                showPopup(v,username,context);
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

    public void showPopup(View view, String username,Context context) {
        Log.v("popup",  "ENTERED POPUP");

        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.taskhamburg, null);

        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        LinearLayout icon1Layout = popupView.findViewById(R.id.icon1_layout);
        LinearLayout icon2Layout = popupView.findViewById(R.id.archive);
        LinearLayout layout = popupView.findViewById(R.id.layout);

        icon1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskAnalysis.class);
                intent.putExtra("USERNAME", username);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });
        popupWindow.showAsDropDown(view, 0, 20);


    }

    }
