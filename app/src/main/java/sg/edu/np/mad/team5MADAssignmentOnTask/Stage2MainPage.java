package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.List;

import sg.edu.np.mad.team5MADAssignmentOnTask.databinding.ActivityStage2MainPageBinding;

public class Stage2MainPage extends AppCompatActivity implements TaskDataHolder.TaskDataCallback,UserDataHolder.UserDataCallback{

    ActivityStage2MainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStage2MainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        String password = getIntent().getStringExtra("PASSWORD");
        String username = getIntent().getStringExtra("USERNAME");

        UserDataHolder.getInstance().fetchUserData(username, this);
        TaskDataHolder.getInstance().fetchUserTasks(username, this);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.friends) {
                replaceFragment(new FriendFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        String username = getIntent().getStringExtra("USERNAME");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment instanceof HomeFragment) {
            fragment = HomeFragment.newInstance(username);
        }

        else if (fragment instanceof  ProfileFragment){
            fragment = ProfileFragment.newInstance(username);

        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onTaskDataFetched(List<Task> tasks) {

    }

    @Override
    public void onUserDataFetched(String displayname) {

    }
}