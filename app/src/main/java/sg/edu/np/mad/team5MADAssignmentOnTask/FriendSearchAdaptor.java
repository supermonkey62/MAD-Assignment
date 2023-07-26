package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendSearchAdaptor extends RecyclerView.Adapter<FriendSearchAdaptor.MyViewHolder> {

    private List<User> userList;
    private List<String> friendsSet;

    private String currentUsername;
    private FriendViewModel friendViewModel;
    private UserDataHolder userDataHolder;
    private DatabaseReference userRef;
    private UserDataHolder.UserResultDataCallback userResultDataCallback;

    public FriendSearchAdaptor(List<User> userList, List<String> friendsSet, String currentUsername) {
        this.userList = userList;
        this.friendsSet = friendsSet;
        this.currentUsername = currentUsername;
        this.userResultDataCallback = userResultDataCallback;
        userDataHolder = UserDataHolder.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void updateData(List<User> userList, List<String> friendsSet) {
        this.userList = userList;
        this.friendsSet = friendsSet;
        this.userResultDataCallback = userResultDataCallback;
    }

    public void updateUserList(List<User> filteredList) {
        userList = filteredList;
    }

    public void updateFriendsSet(List<String> friendsSet) {
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

        if (friendsSet != null) {
            if (friendsSet.contains(user.getUsername())) {
                holder.friendStatus.setText("Friend");
                holder.friendStatus.setTextColor(Color.GREEN);
                holder.addFriendIcon.setVisibility(View.GONE);
                holder.removeFriendBtn.setVisibility(View.VISIBLE);
            } else {
                holder.friendStatus.setText("");
                holder.addFriendIcon.setVisibility(View.VISIBLE);
                holder.removeFriendBtn.setVisibility(View.GONE);
            }
        } else {
            Log.d("FriendAdapter", "FriendsSet is null");
            holder.friendStatus.setText("");
            holder.addFriendIcon.setVisibility(View.VISIBLE);
            holder.removeFriendBtn.setVisibility(View.GONE);
        }

        if (userResultDataCallback != null) {
            userResultDataCallback.onUserResultDataFetched(user.getDisplayname(), friendsSet);
        }

        // Fetch user data from Firebase and display it
        userDataHolder.fetchUserDataForResult(user.getUsername(), new UserDataHolder.UserResultDataCallback() {
            @Override
            public void onUserResultDataFetched(String displayname, List<String> friendSet) {
                if (displayname != null) {
                    holder.displayName.setText(displayname);
                }
            }
        });

        // Handle the "Add friend" button click
        holder.addFriendIcon.setOnClickListener(view -> {
            if (friendsSet != null && user != null && user.getUsername() != null) {
                // Add the user as a friend in your database (Firebase or any other)
                // Update the friendsSet and refresh the adapter
                friendsSet.add(user.getUsername());
                notifyDataSetChanged();
                userDataHolder.addFriend(currentUsername, user.getUsername());
                updateFriendListInFirebase(friendsSet);
            }
        });

        // Handle the "Remove" button click
        holder.removeFriendBtn.setOnClickListener(view -> {
            if (friendsSet != null && user != null && user.getUsername() != null) {
                // Remove the user from friends in your database (Firebase or any other)
                // Update the friendsSet and refresh the adapter
                friendsSet.remove(user.getUsername());
                notifyDataSetChanged();
                user.setFriend(false);
                userDataHolder.removeFriend(currentUsername, user.getUsername());
                updateFriendStatusInFirebase(user.getUsername(), false);
            }
        });
    }



    private void updateFriendListInFirebase(List<String> friendsSet) {
        // Get a reference to the current user's friend list in Firebase
        DatabaseReference currentUserFriendListRef = userRef.child(currentUsername).child("friendsList");

        // Convert the Set to a List to save it in Firebase
        List<String> friendsList = new ArrayList<>(friendsSet);

        // Update the friend list in Firebase
        currentUserFriendListRef.setValue(friendsList)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FriendViewModel.getInstance().setFriendsSet(friendsSet);
                    } else {
                    }
                });
    }


    private void updateFriendStatusInFirebase(String friendUsername, boolean isFriend) {
        // Update the friend status in Firebase for the current user
        DatabaseReference currentUserFriendRef = userRef.child(currentUsername).child("friendsList").child(friendUsername);
        currentUserFriendRef.setValue(isFriend)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    } else {
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

