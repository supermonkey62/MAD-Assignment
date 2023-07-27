package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.team5MADAssignmentOnTask.databinding.ActivityStage2MainPageBinding;


public class Stage2MainPage extends AppCompatActivity implements HomeFragment.OnDateSelectedListener, ViewModelStoreOwner {

    private ViewModelStore viewModelStore = new ViewModelStore();

    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
    ActivityStage2MainPageBinding binding;
    private String username;

    private FriendViewModel sharedFriendViewModel;

    private List<User> userList = new ArrayList<>();
    private List<String> friendsSet = new ArrayList<>();

    private String datetoday;

    FloatingActionButton fab;

    String selectedDateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage2MainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedFriendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        // Retrieve the extra string "USERNAME" from the intent
        username = getIntent().getStringExtra("USERNAME");

        Date today = new Date();
        String pattern = "dd/MM/yyyy";

        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);

        datetoday = dateFormatter.format(today);


        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.timer) {
                Bundle extras = new Bundle();
                extras.putString("USERNAME", username);
                replaceFragment(new TodolistFragment());
            } else if (item.getItemId() == R.id.friends) {
                replaceFragment(new FriendFragment());
            } else if (item.getItemId() == R.id.profile) {
                Fragment homeFragment = new HomeFragment();
                Bundle extras = new Bundle();
                extras.putString("USERNAME", username);
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

        addTaskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username and selectedDateString from the arguments or wherever you have them

                // Create an Intent to launch the activity where you want to pass the data
                Intent intent = new Intent(Stage2MainPage.this, AddTask.class);

                if (selectedDateString == null || selectedDateString.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    selectedDateString = dateFormat.format(new Date()); // Set it to today's date
                }

                // Add the username and selectedDateString as extras to the Intent
                intent.putExtra("USERNAME", username);
                intent.putExtra("DATE", selectedDateString);

                // Start the activity with the Intent
                startActivity(intent);
            }
        });

        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stage2MainPage.this, AddEvent.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);

            }
        });


    }

    public FriendViewModel getSharedFriendViewModel() {
        return sharedFriendViewModel;
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

    public void launchTaskCalendarActivity(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).launchTaskCalendarActivity(view);
        }
    }


    @Override
    public void onDateSelected(String selectedDate) {
        // Handle the selected date here in Stage2MainPage
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Log.v("Stage2MainPage", "Selected Date: " + selectedDate);
        selectedDateString = selectedDate;
        // You can also convert the selected date back to a Date object if needed
        try {
            Date date = dateFormat.parse(selectedDate);
            // Do something with the Date object
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

