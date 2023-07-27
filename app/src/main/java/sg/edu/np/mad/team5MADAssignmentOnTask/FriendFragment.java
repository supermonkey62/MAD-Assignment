package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FriendFragment extends Fragment implements UserDataHolder.UserResultDataCallback {
    private String currentUsername;


    private MutableLiveData<List<User>> userList;
    private MutableLiveData<List<String>> friendsSet;

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
        currentUsername = getActivity().getIntent().getStringExtra("USERNAME");
        adapter = new FriendSearchAdaptor(new ArrayList<>(), "", currentUsername);
        recyclerView.setAdapter(adapter);

        observeViewModel();
        fetchAndSetFriendsListFromFirebase();

        if (currentUsername == null) {
            Log.e("FriendFragment", "Current username is null");
            return null;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FriendFragment", "DataSnapshot: " + dataSnapshot.toString());
                GenericTypeIndicator<HashMap<String, User>> userType = new GenericTypeIndicator<HashMap<String, User>>() {};
                HashMap<String, User> userMap = dataSnapshot.getValue(userType);

                if (userMap != null) {
                    List<User> userList = new ArrayList<>(userMap.values());

                    if (userList != null) {
                        List<User> filteredUserList = new ArrayList<>();
                        for (User user : userList) {
                            if (user != null && currentUsername != null && !user.getUsername().equals(currentUsername)) {
                                filteredUserList.add(user);
                            }
                        }
                        friendViewModel.getUserListLiveData().setValue(filteredUserList);
                        fetchFriendsSet();
                    }
                }

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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            fetchFriendsSet();
            fetchAndSetFriendsListFromFirebase();
        }
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
                // Get the hashmap of users
                GenericTypeIndicator<HashMap<String, User>> typeIndicator = new GenericTypeIndicator<HashMap<String, User>>() {};
                HashMap<String, User> usersMap = dataSnapshot.getValue(typeIndicator);

                if (usersMap != null) {
                    // Convert hashmap to list of users
                    List<User> userList = new ArrayList<>(usersMap.values());

                    // Remove the current user from the list
                    Iterator<User> iterator = userList.iterator();
                    while (iterator.hasNext()) {
                        User user = iterator.next();
                        if (user.getUsername().equals(currentUsername)) {
                            iterator.remove();
                            break;
                        }
                    }

                    // Update the LiveData with the user list
                    friendViewModel.getUserListLiveData().setValue(userList);
                }

                fetchFriendsSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
            }
        });
    }


    @Override
    public void onUserResultDataFetched(String displayname, String friendSet) {
        if (userList != null && adapter != null) {
            // Update the adapter with the friendsSet as a comma-separated string
            adapter.updateFriendsSet(TextUtils.join(",", friendsSet.getValue()));

            // Iterate through each user in the userList to fetch and set their displayname
            List<User> userListData = userList.getValue();
            if (userListData != null) {
                for (User user : userListData) {
                    fetchAndSetDisplayName(user);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchAndSetFriendsListFromFirebase() {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String friendsListString = dataSnapshot.getValue(String.class);
                    if (friendsListString == null || friendsListString.isEmpty()) {
                        // If the string is empty, there are no friends, so create an empty list
                        friendsSet.setValue(new ArrayList<>());
                    } else {
                        // Convert the comma-separated string to a List
                        List<String> friendsList = new ArrayList<>(Arrays.asList(friendsListString.split(",")));
                        friendsSet.setValue(friendsList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
            }
        });
    }


    private void fetchFriendsSet() {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String friendsListString = dataSnapshot.getValue(String.class);
                    if (friendsListString == null || friendsListString.isEmpty()) {
                        // If the string is empty, there are no friends, so create an empty list
                        friendsSet.setValue(new ArrayList<>());
                    } else {
                        // Convert the comma-separated string to a List
                        List<String> friendsList = new ArrayList<>(Arrays.asList(friendsListString.split(",")));
                        friendsSet.setValue(friendsList);

                        // Print out the contents of the friendsList for debugging purposes
                        Log.d("FriendFragment", "Friends List: " + friendsList);
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
            public void onUserResultDataFetched(String displayname, String friendsSet) {
                if (displayname != null) {
                    user.setDisplayname(displayname);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void observeViewModel() {
        userList = friendViewModel.getUserListLiveData();
        friendsSet = friendViewModel.getFriendsSetLiveData();

        userList.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                if (userList != null && adapter != null) {
                    // Update the adapter with the new userList
                    adapter.updateData(userList, TextUtils.join(",", friendsSet.getValue()));
                    adapter.notifyDataSetChanged();
                }
            }
        });

        friendsSet.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> friendsSet) {
                if (friendsSet != null && adapter != null) {
                    // Update the friendsSet in the adapter when it changes
                    adapter.updateFriendsSet(TextUtils.join(",", friendsSet));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }







}



