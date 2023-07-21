package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.team5MADAssignmentOnTask.databinding.ActivityStage2MainPageBinding;

public class Stage2MainPage extends AppCompatActivity {

    ActivityStage2MainPageBinding binding;
    private String username;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage2MainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the extra string "USERNAME" from the intent
        username = getIntent().getStringExtra("USERNAME");

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.timer) {
                replaceFragment(new TimerFragment());
            } else if (item.getItemId() == R.id.friends) {
                replaceFragment(new FriendFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        fab = findViewById(R.id.fab);
        FloatingActionButton addTaskFAB = findViewById(R.id.addTaskFAB);
        FloatingActionButton addEventFAB = findViewById(R.id.addEventFAB);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Toggle visibility of addTaskFAB and addEventFAB
                if (addTaskFAB.getVisibility() == View.VISIBLE && addEventFAB.getVisibility() == View.VISIBLE) {
                    addTaskFAB.setVisibility(View.GONE);
                    addEventFAB.setVisibility(View.GONE);
                } else {
                    addTaskFAB.setVisibility(View.VISIBLE);
                    addEventFAB.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Pass the username to the fragment using a Bundle
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", username);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void previousWeekAction(View view) {
        // Handle previous week action here
        // You can get the current fragment and call the corresponding method
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).previousWeekAction(view);
        }
    }

    public void nextWeekAction(View view) {
        // Handle next week action here
        // You can get the current fragment and call the corresponding method
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).nextWeekAction(view);
        }
    }
}

