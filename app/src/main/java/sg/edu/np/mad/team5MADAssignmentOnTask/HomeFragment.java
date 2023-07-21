package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class HomeFragment extends Fragment implements TaskDataHolder.TaskDataCallback,UserDataHolder.UserDataCallback{

    List<Task> taskList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        String username = getArguments().getString("USERNAME");

        return rootView;
    }

    public static HomeFragment newInstance(String username) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", username);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onTaskDataFetched(List<Task> tasks) {

    }

    @Override
    public void onUserDataFetched(String displayname) {

    }

}