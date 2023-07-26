package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment implements UserDataHolder.UserResultDataCallback {
    private String currentUsername;
    private MutableLiveData<List<User>> userList = new MutableLiveData<>();
    private MutableLiveData<List<String>> friendsSet = new MutableLiveData<>(new ArrayList<>());



    private FriendSearchAdaptor adapter;
    private FriendViewModel friendViewModel;
    private FriendViewModel sharedFriendViewModel;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        recyclerView = view.findViewById(R.id.usersResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friendViewModel = new ViewModelProvider(requireActivity(), new FriendViewModel.Factory()).get(FriendViewModel.class);
        sharedFriendViewModel = ((Stage2MainPage) getActivity()).getSharedFriendViewModel();
        userList = friendViewModel.getUserListLiveData();
        friendsSet = friendViewModel.getFriendsSetLiveData();
        currentUsername = getActivity().getIntent().getStringExtra("USERNAME");

        observeViewModel();

        if (currentUsername == null) {
            Log.e("FriendFragment", "Current username is null");
            return null;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && !user.getUsername().equals(currentUsername)) {
                        userList.add(user);
                    }
                }
                friendViewModel.getUserListLiveData().setValue(userList);
                fetchUserListFromFirebase();
                fetchFriendsSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
            }
        });


        SearchView friendSearch = view.findViewById(R.id.friendSearch);
        friendSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user submits
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the userList based on the search query
                List<User> filteredList = filterUserList(newText);
                // Update the adapter with the filtered list
                adapter.updateUserList(filteredList);
                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return view;
    }

    private List<User> filterUserList(String query) {
        List<User> userList = friendViewModel.getUserListLiveData().getValue();
        if (userList == null) {
            // Return an empty list or handle the null case appropriately
            return new ArrayList<>();
        }

        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            // Match the query against username, displayname, or any other relevant fields
            if (user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                    user.getDisplayname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }


    private void fetchUserListFromFirebase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && currentUsername != null && !user.getUsername().equals(currentUsername)) {
                        userList.add(user);
                    }
                }

                friendViewModel.getUserListLiveData().setValue(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
            }
        });
    }

    @Override
    public void onUserResultDataFetched(String displayname, List<String> friendSet) {
        if (adapter != null) {
            // Update the adapter with the friends list
            adapter.updateFriendsSet(friendSet);

            // Iterate through each user in the userList to fetch and set their displayname
            List<User> userListData = userList.getValue();
            if (userListData != null) {
                for (User user : userListData) {
                    fetchAndSetDisplayName(user);
                }
            }
        }
    }


    private void fetchFriendsSet() {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use GenericTypeIndicator to retrieve the List<String> data
                    GenericTypeIndicator<List<String>> friendsListType = new GenericTypeIndicator<List<String>>() {};
                    List<String> friendsList = dataSnapshot.getValue(friendsListType);
                    if (friendsList == null) {
                        friendsList = new ArrayList<>();
                    }
                    // Update the LiveData with the retrieved friends list
                    friendsSet.setValue(friendsList);

                    // Print out the contents of the friendsList for debugging purposes
                    Log.d("FriendFragment", "Friends List:");
                    for (String friend : friendsList) {
                        Log.d("FriendFragment", friend);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
            }
        });
    }









    private void fetchAndSetDisplayName(User user) {
        // Create an instance of UserDataHolder
        UserDataHolder userDataHolder = UserDataHolder.getInstance();

        // Fetch user data for the specific user
        userDataHolder.fetchUserDataForUser(user.getUsername(), new UserDataHolder.UserResultDataCallback() {
            @Override
            public void onUserResultDataFetched(String displayname, List<String> friendsSet) {
                if (displayname != null) {
                    user.setDisplayname(displayname);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void observeViewModel() {
        currentUsername = getActivity().getIntent().getStringExtra("USERNAME");

        userList = friendViewModel.getUserListLiveData();
        friendsSet = friendViewModel.getFriendsSetLiveData();

        userList.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                if (userList != null) {
                    if (adapter == null) {
                        adapter = new FriendSearchAdaptor(userList, friendsSet.getValue(), currentUsername);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateUserList(userList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        friendsSet.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> friendsSet) {
                if (friendsSet != null) {
                    if (adapter != null) {
                        adapter.updateFriendsSet(friendsSet);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }



}



