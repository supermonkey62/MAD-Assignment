package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void addFriend(String currentUsername, String friendUsername) {
        // Update the friendsList for the current user by adding the friend's username
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");
        currentUserRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                String currentValue = currentData.getValue(String.class);
                String updatedValue;
                if (currentValue == null || currentValue.isEmpty()) {
                    updatedValue = friendUsername;
                } else {
                    updatedValue = currentValue + "," + friendUsername;
                }
                currentData.setValue(updatedValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                if (databaseError != null) {
                    Log.e("UserDataHolder", "Error adding friend in Firebase", databaseError.toException());
                } else {
                    Log.d("UserDataHolder", "Friend added in Firebase");
                }
            }
        });
    }

    public void removeFriend(String currentUsername, String friendUsername) {
        // Update the friendsList for the current user by removing the friend's username
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");
        currentUserRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                String currentValue = currentData.getValue(String.class);
                if (currentValue == null || currentValue.isEmpty()) {
                    return Transaction.success(currentData);
                }
                List<String> currentFriendsList = new ArrayList<>(Arrays.asList(currentValue.split(",")));
                currentFriendsList.remove(friendUsername);
                String updatedValue = TextUtils.join(",", currentFriendsList);
                currentData.setValue(updatedValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                if (databaseError != null) {
                    Log.e("UserDataHolder", "Error removing friend in Firebase", databaseError.toException());
                } else {
                    Log.d("UserDataHolder", "Friend removed in Firebase");
                }
            }
        });
    }


    public void fetchUserDataForUser(String username, UserResultDataCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String displayname = dataSnapshot.child("displayname").getValue(String.class);
                String friendList = dataSnapshot.child("friendList").getValue(String.class);

                if (displayname != null) {
                    callback.onUserResultDataFetched(displayname, friendList);
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
                    String friendsSet = dataSnapshot.child("friendsList").getValue(String.class);
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
        void onUserResultDataFetched(String displayname, String friendList);
    }
}



