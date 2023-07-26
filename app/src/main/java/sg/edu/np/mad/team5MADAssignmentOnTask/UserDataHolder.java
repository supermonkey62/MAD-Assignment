package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDataHolder {
    private static UserDataHolder instance;
    private  String Displayname;

    private  String Image_URI;
    private DatabaseReference userRef;

    private UserDataHolder() {
        Displayname = new String();

        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public static synchronized UserDataHolder getInstance() {
        if (instance == null) {
            instance = new UserDataHolder();
        }
        return instance;
    }

    public void addFriend(String username, String friendUsername) {
        // Fetch the user's current friendsList from Firebase
        DatabaseReference friendListRef = FirebaseDatabase.getInstance().getReference("Users").child("user_id").child("friendList");

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                    List<String> friendsList = dataSnapshot.child("friendsList").getValue(List.class);
                    if (friendsList == null) {
                        friendsList = new ArrayList<>();
                    }

                    // Add the new friend to the list if not already present
                    if (!friendsList.contains(friendUsername)) {
                        friendsList.add(friendUsername);
                    }

                    // Update the friendsList in Firebase
                    userRef.child(username).child("friendsList").setValue(friendsList)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Friend added successfully
                                    Log.d("UserDataHolder", "Friend added: " + friendUsername);
                                } else {
                                    // Error adding friend
                                    Log.e("UserDataHolder", "Error adding friend: " + friendUsername);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        friendListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> friendList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendUsername = snapshot.getKey();
                        friendList.add(friendUsername);
                    }
                }

                friendListRef.setValue(friendList)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseUpdate", "FriendList updated to List successfully.");
                            } else {
                                Log.e("FirebaseUpdate", "Error updating FriendList to List.", task.getException());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseRead", "Error reading FriendList data.", databaseError.toException());
            }
        });


    }

    public void removeFriend(String currentUser, String friendToRemove) {
        // Get a reference to the current user's friendsList in Firebase
        DatabaseReference currentUserFriendListRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUser).child("friendsList");

        // Remove the friend from the friend list in Firebase
        currentUserFriendListRef.child(friendToRemove).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Friend removed successfully
                        Log.d("UserDataHolder", "Friend removed: " + friendToRemove);
                    } else {
                        // Error removing friend
                        Log.e("UserDataHolder", "Error removing friend: " + friendToRemove);
                    }
                });
    }

    public void fetchUserDataForUser(String username, UserResultDataCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    // Set the username explicitly (since it's not included in the dataSnapshot.getValue())
                    user.setUsername(username);
                    callback.onUserResultDataFetched(user.getDisplayname(), user.getFriendList());
                } else {
                    callback.onUserResultDataFetched(null, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
                callback.onUserResultDataFetched(null, null);
            }
        });
    }

    public void fetchUserData(String username, final UserDataCallback callback) {
        userRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Displayname = dataSnapshot.child("displayname").getValue(String.class);
                    Log.v("User Display Name", "Display Updated" + Displayname);


                }
                // Invoke the callback method with the retrieved taskList
                callback.onUserDataFetched(Displayname);

                Log.v("callback Value",Displayname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public void fetchUserDataForResult(String username, UserResultDataCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    String displayname = user.getDisplayname();
                    // Use GenericTypeIndicator to retrieve the List<String> data
                    GenericTypeIndicator<List<String>> friendsListType = new GenericTypeIndicator<List<String>>() {};
                    List<String> friendsSet = dataSnapshot.child("friendsList").getValue(friendsListType);
                    callback.onUserResultDataFetched(displayname, friendsSet);
                } else {
                    callback.onUserResultDataFetched(null, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if data retrieval is canceled or fails
                callback.onUserResultDataFetched(null, null);
            }
        });
    }

    public void fetchUserTasks(String username, View.OnClickListener onClickListener) {

    }

    public interface UserDataCallback {
        void onUserDataFetched(String displayname);

    }

    public interface UserResultDataCallback {
        void onUserResultDataFetched(String displayname, List<String> friendSet);
    }
}



