package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendSearchAdaptor extends RecyclerView.Adapter<FriendSearchAdaptor.MyViewHolder> {

    private List<User> userList;
    private String friendsSet;

    private String currentUsername;
    private UserDataHolder userDataHolder;
    private DatabaseReference userRef;

    public FriendSearchAdaptor(List<User> userList, String friendsSet, String currentUsername) {
        this.userList = userList;
        this.friendsSet = friendsSet;
        this.currentUsername = currentUsername;
        userDataHolder = UserDataHolder.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void updateData(List<User> userList, String friendsSet) {
        this.userList = userList;
        this.friendsSet = friendsSet;
        notifyDataSetChanged();
    }

    public void updateUserList(List<User> filteredList) {
        userList = filteredList;
    }

    public void updateFriendsSet(String friendsSet) {
        this.friendsSet = friendsSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_cardholder, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.displayName.setText(user.getDisplayname());
        holder.removeFriendBtn.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.gray));

        // Set the profile picture to the ImageView using Glide
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(user.getImageURI()))
                .into(holder.profilePic);

        // Check if the user is a friend based on the friendList
        List<String> friendList = new ArrayList<>();
        if (friendsSet != null && !friendsSet.isEmpty()) {
            String[] friendsArray = friendsSet.split(",");
            friendList = Arrays.asList(friendsArray);
        }

        if (friendList.contains(user.getUsername())) {
            // User is a friend
            holder.friendStatus.setText("Friend");
            holder.friendStatus.setTextColor(Color.GREEN);
            holder.addFriendIcon.setVisibility(View.GONE);
            holder.removeFriendBtn.setVisibility(View.VISIBLE);
        } else {
            // User is not a friend
            holder.friendStatus.setText("");
            holder.addFriendIcon.setVisibility(View.VISIBLE);
            holder.removeFriendBtn.setVisibility(View.GONE);
            holder.addFriendIcon.setColorFilter(Color.GRAY);
        }

        // Fetch user data from Firebase and display it
        userDataHolder.fetchUserDataForResult(user.getUsername(), new UserDataHolder.UserResultDataCallback() {
            @Override
            public void onUserResultDataFetched(String displayname, String friendSet) {
                if (displayname != null) {
                    holder.displayName.setText(displayname);
                }
            }
        });


        holder.addFriendIcon.setOnClickListener(view -> {
            if (!friendsSet.contains(user.getUsername())) {
                friendsSet = friendsSet.isEmpty() ? user.getUsername() : friendsSet + "," + user.getUsername();

                holder.friendStatus.setText("Friend");
                holder.friendStatus.setTextColor(Color.GREEN);
                holder.addFriendIcon.setVisibility(View.GONE);
                holder.removeFriendBtn.setVisibility(View.VISIBLE);

                notifyDataSetChanged();
                userDataHolder.addFriend(currentUsername, user.getUsername());
                updateFriendListInFirebase(friendsSet);
            }
        });

        holder.removeFriendBtn.setOnClickListener(view -> {
            if (friendsSet.contains(user.getUsername())) {
                List<String> friendsList = new ArrayList<>(Arrays.asList(friendsSet.split(",")));
                friendsList.remove(user.getUsername());
                friendsSet = TextUtils.join(",", friendsList);

                holder.friendStatus.setText("");
                holder.addFriendIcon.setVisibility(View.VISIBLE);
                holder.removeFriendBtn.setVisibility(View.GONE);
                holder.addFriendIcon.setColorFilter(Color.GRAY);

                notifyDataSetChanged();
                userDataHolder.removeFriend(currentUsername, user.getUsername());
                updateFriendListInFirebase(friendsSet);
            }
        });
    }

    private void updateFriendListInFirebase(String updatedFriendsSet) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");

        currentUserRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                String currentValue = currentData.getValue(String.class);
                if (currentValue == null) {
                    // If the current value is null, set the updated value directly
                    currentData.setValue(updatedFriendsSet);
                } else {
                    // If the current value is not null, split it into a set to ensure uniqueness
                    List<String> currentFriendsList = new ArrayList<>(Arrays.asList(currentValue.split(",")));
                    List<String> updatedFriendsList = new ArrayList<>(Arrays.asList(updatedFriendsSet.split(",")));

                    // Add new friends to the current friends list
                    for (String friend : updatedFriendsList) {
                        if (!currentFriendsList.contains(friend)) {
                            currentFriendsList.add(friend);
                        }
                    }

                    String updatedValue = TextUtils.join(",", currentFriendsList);
                    currentData.setValue(updatedValue);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                if (databaseError != null) {
                    Log.e("FriendFragment", "Error updating friends list in Firebase", databaseError.toException());
                } else {
                    Log.d("FriendFragment", "Friends list updated in Firebase");
                }
            }
        });
    }


    private void updateFriendsListAndUI(String updatedFriendsSet, MyViewHolder holder, User user) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(currentUsername).child("friendsList");

        currentUserRef.setValue(updatedFriendsSet, (error, ref) -> {
            if (error == null) {
                // Friend list updated successfully in Firebase, update the UI
                holder.friendStatus.setText("Friend");
                holder.friendStatus.setTextColor(Color.GREEN);
                holder.addFriendIcon.setVisibility(View.GONE);
                holder.removeFriendBtn.setVisibility(View.VISIBLE);
            } else {
                // Handle error if there is an issue updating the friend list in Firebase
                Log.e("FriendSearchAdaptor", "Error updating friends list in Firebase", error.toException());
            }
        });
    }





    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView displayName;
        TextView friendStatus;
        ImageView profilePic;
        ImageView addFriendIcon;
        Button removeFriendBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.display_name_result);
            friendStatus = itemView.findViewById(R.id.friend_status);
            profilePic = itemView.findViewById(R.id.result_pic);
            addFriendIcon = itemView.findViewById(R.id.add_friend_icon);
            removeFriendBtn = itemView.findViewById(R.id.remove_friend_btn);
        }
    }
}
