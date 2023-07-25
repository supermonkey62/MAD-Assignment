package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchForUsers extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView userRecyclerView;
    private Button addButton;

    private List<User> userList;
    private UserAdapter userAdapter;

    private DatabaseReference usersRef;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_users);

        searchView = findViewById(R.id.searchView);
        userRecyclerView = findViewById(R.id.userRecyclerView);
        addButton = findViewById(R.id.addButton);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        username = getIntent().getStringExtra("USERNAME");

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userAdapter);

        // Load all users initially
        loadAllUsers();

        // Handle search query changes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing when submitting the query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the user list based on the search query
                filterUsers(newText);
                return true;
            }
        });

        addButton.setOnClickListener(view -> {
            List<User> selectedUsers = userAdapter.getSelectedUsers();

            // Log the usernames of selected users
            for (User user : selectedUsers) {
                Log.v("SearchForUsers", "Selected User: " + user.getUsername());
            }

            // Convert the list of selected users to a JSON string
            String selectedUsersJson = new Gson().toJson(selectedUsers);

            // Save the JSON string to SharedPreferences
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("SELECTED_USERS", selectedUsersJson);
            editor.apply();

            // Finish this activity to go back to the previous activity
            finish();
        });

    }

    private void loadAllUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    // Exclude the current user (optional)
                    if (!user.getUsername().equals(username)) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if loading users fails
            }
        });
    }

    private void filterUsers(String query) {
        if (TextUtils.isEmpty(query)) {
            userAdapter.setFilteredList(userList);
        } else {
            List<User> filteredList = new ArrayList<>();
            for (User user : userList) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
            userAdapter.setFilteredList(filteredList);
        }
    }


}
